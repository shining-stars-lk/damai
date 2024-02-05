-- 票档数量的key
local ticket_remain_number_hash_key = KEYS[1]
-- 没有售卖的座位key
local seat_no_sold_hash_key = KEYS[2]
-- 锁定的座位key
local seat_lock_hash_key = KEYS[3]
-- 类型 1 用户选座位 2自动匹配座位
local type = tonumber(KEYS[4])
-- 要购买的票档 包括票档id和票档数量
local ticket_count_list = cjson.decode(ARGV[1]) 
-- 过滤后符合条件可以购买的座位集合
local purchase_seat_list = {}
-- 入参座位价格总和
local total_seat_dto_price = 0
-- 缓存座位价格总和
local total_seat_vo_price = 0
-- 匹配座位算法 
local function find_adjacent_seats(all_seats, seat_count)
    local adjacent_seats = {}

    -- 对可用座位排序
    table.sort(all_seats, function(s1, s2)
        if s1.rowCode == s2.rowCode then
            return s1.colCode < s2.colCode
        else
            return s1.rowCode < s2.rowCode
        end
    end)

    -- 寻找相邻座位
    for i = 1, #all_seats - seat_count + 1 do
        local seats_found = true
        for j = 0, seat_count - 2 do
            local current = all_seats[i + j]
            local next = all_seats[i + j + 1]

            if not (current.rowCode == next.rowCode and next.colCode - current.colCode == 1) then
                seats_found = false
                break
            end
        end
        if seats_found then
            for k = 0, seat_count - 1 do
                table.insert(adjacent_seats, all_seats[i + k])
            end
            return adjacent_seats
        end
    end
    -- 如果没有找到，返回空列表
    return adjacent_seats
end

-- 入参座位存在
if (type == 1) then
    for index,ticket_count in ipairs(ticket_count_list) do
        -- 入参座位的票档id
        local ticket_category_id = ticket_count.ticketCategoryId
        -- 入参座位的票档数量
        local count = ticket_count.ticketCount
        -- 从缓存中获取相应票档数量
        local remain_number_str = redis.call('hget', ticket_remain_number_hash_key, tostring(ticket_category_id))
        -- 如果为空直接返回
        if (remain_number_str == nil) then
            return string.format('{"%s": %d}', 'code', 40010)
        end
        local remain_number = tonumber(remain_number_str)
        -- 入参座位的票档数量大于缓存中获取相应票档数量，说明票档数量不足，直接返回
        if (count > remain_number) then
            return string.format('{"%s": %d}', 'code', 40011)
        end
    end
    -- 入参座位集合
    local seat_dto_list = cjson.decode(ARGV[2])
    for index,seat_dto in ipairs(seat_dto_list) do
        -- 入参座位id
        local id = seat_dto.id
        -- 入参座位价格
        local seat_dto_price = seat_dto.price
        -- 根据座位id从缓存中没有售卖的座位
        local seat_vo_str = redis.call('hget', seat_no_sold_hash_key, tostring(id))
        -- 如果从缓存中为空，则直接返回
        if (seat_vo_str == nil) then
            return string.format('{"%s": %d}', 'code', 40001)
        end
        local seat_vo = cjson.decode(seat_vo_str)
        -- 如果从缓存查询的座位状态是锁定的，直接返回
        if (seat_vo.sellStatus == 2) then
            return string.format('{"%s": %d}', 'code', 40002)
        end
        -- 如果从缓存查询的座位状态是已经售卖的，直接返回
        if (seat_vo.sellStatus == 3) then
            return string.format('{"%s": %d}', 'code', 40003)
        end
        table.insert(purchase_seat_list,seat_vo)
        -- 入参座位价格累加
        total_seat_dto_price = total_seat_dto_price + seat_dto_price
        -- 缓存座位价格累加
        total_seat_vo_price = total_seat_vo_price + seat_vo.price
        if (total_seat_dto_price > total_seat_vo_price) then
            return string.format('{"%s": %d}', 'code', 40008)
        end
    end
end
-- 入参座位不存在
if (type == 2) then
    -- 这里的外层循环其实就一次
    for index,ticket_count in ipairs(ticket_count_list) do
        -- 入参选择的票档id
        local ticket_category_id = ticket_count.ticketCategoryId
        -- 入参选择的票档数量
        local count = ticket_count.ticketCount
        -- 从缓存中获取相应票档数量
        local remain_number_str = redis.call('hget', ticket_remain_number_hash_key, tostring(ticket_category_id))
        -- 如果为空直接返回
        if (remain_number_str == nil) then
            return string.format('{"%s": %d}', 'code', 40010)
        end
        local remain_number = tonumber(remain_number_str)
        -- 入参的票档数量大于缓存中获取相应票档数量，说明票档数量不足，直接返回
        if (count > remain_number) then
            return string.format('{"%s": %d}', 'code', 40011)
        end
        -- 获取没有售卖的座位集合
        local seat_vo_no_sold_str_list = redis.call('hvals',seat_no_sold_hash_key)
        local filter_seat_vo_no_sold_list = {}
        -- 这里遍历的原因，座位集合是以hash存储在缓存中，而每个座位是字符串，要把字符串转成对象
        for index,seat_vo_no_sold_str in ipairs(seat_vo_no_sold_str_list) do
            local seat_vo_no_sold = cjson.decode(seat_vo_no_sold_str)
            -- 根据入参选择的票档过滤出相应的没有售卖的座位
            if (seat_vo_no_sold.ticketCategoryId == ticket_category_id) then
                table.insert(filter_seat_vo_no_sold_list,seat_vo_no_sold)
            end
        end
        -- 利用算法自动根据人数和票档进行分配相邻座位
        purchase_seat_list = find_adjacent_seats(filter_seat_vo_no_sold_list,count)
        if (#purchase_seat_list < count) then
            return string.format('{"%s": %d}', 'code', 40004)
        end
    end
end
-- 经过以上的验证，说明座位和票档数量是够用的，下面开始真正的锁定座位和扣除票档数量操作
local seat_id_list = {}
local seat_data_list = {}
for index,seat in ipairs(purchase_seat_list) do
    local seat_id = seat.id
    table.insert(seat_id_list,tostring(seat_id))
    table.insert(seat_data_list,tostring(seat_id))
    seat.sellStatus = 2
    table.insert(seat_data_list,cjson.encode(seat))
end
-- 扣票档数量
for index,ticket_count in ipairs(ticket_count_list) do
    -- 票档id
    local ticket_category_id = ticket_count.ticketCategoryId
    -- 票档数量
    local count = ticket_count.ticketCount
    redis.call('hincrby',ticket_remain_number_hash_key,ticket_category_id,"-" .. count)
end
-- 将没有售卖的座位删除
redis.call('hdel',seat_no_sold_hash_key,unpack(seat_id_list))
-- 再将座位数据添加到锁定的座位中
redis.call('hmset',seat_lock_hash_key,unpack(seat_data_list))

return '{"code": 0, "purchaseSeatList": ' .. cjson.encode(purchase_seat_list) .. '}'




