local ticket_remain_number_hash_key = KEYS[1]
local seat_no_sold_hash_key = KEYS[2]
local type = tonumber(KEYS[2])
local ticket_count_list = cjson.decode(ARGV[1]) 
-- 要购买的座位
local purchase_seat_list = {}
-- 入参座位价格总和
local total_seat_dto_price = 0
-- 缓存座位价格总和
local total_seat_vo_price = 0
-- 入参座位存在
if (type == 1) then
    for index,ticket_count in ipairs(ticket_count_list) do
        -- 入参座位的票档id
        local ticket_category_id = ticket_count.ticketCategoryId
        -- 入参座位的票档数量
        local count = ticket_count.count
        -- 从缓存中获取相应票档数量
        local remain_number_str = redis.call('hget', ticket_remain_number_hash_key, tostring(ticket_category_id))
        -- 如果为空直接返回
        if (remain_number_str == nil) then
            return 1
        end
        local remain_number = tonumber(remain_number_str)
        -- 入参座位的票档数量大于缓存中获取相应票档数量，说明票档数量不足，直接返回
        if (count > remain_number) then
            return 1
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
            return 1
        end
        local seat_vo = cjson.decode(seat_vo_str)
        -- 如果从缓存查询的座位状态是锁定的，直接返回
        if (seat_vo.sellStatus == 2) then
            return 1
        end
        -- 如果从缓存查询的座位状态是已经售卖的，直接返回
        if (seat_vo.sellStatus == 3) then
            return 1
        end
        table.insert(purchase_seat_list,seat_vo)
        -- 入参座位价格累加
        total_seat_dto_price = total_seat_dto_price + seat_dto_price
        -- 缓存座位价格累加
        total_seat_vo_price = total_seat_vo_price + seat_vo.price
        if (total_seat_dto_price > total_seat_vo_price) then
            return 1
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
        local count = ticket_count.count
        -- 从缓存中获取相应票档数量
        local remain_number_str = redis.call('hget', ticket_remain_number_hash_key, tostring(ticket_category_id))
        -- 如果为空直接返回
        if (remain_number_str == nil) then
            return 1
        end
        local remain_number = tonumber(remain_number_str)
        -- 入参的票档数量大于缓存中获取相应票档数量，说明票档数量不足，直接返回
        if (count > remain_number) then
            return 1
        end
        local seat_vo_no_sold_str_list = redis.call('hvals',seat_no_sold_hash_key)
        local filter_seat_vo_no_sold_list = {}
        for index,seat_vo_no_sold_str in ipairs(seat_vo_no_sold_str_list) do
            local seat_vo_no_sold = cjson.decode(seat_vo_no_sold_str)
            if (seat_vo_no_sold.ticketCategoryId == ticket_category_id) then
                table.insert(filter_seat_vo_no_sold_list,seat_vo_no_sold)
            end
        end
        purchase_seat_list = findAdjacentSeatVos(filter_seat_vo_no_sold_list,count)
    end
end

return cjson.encode(purchase_seat_list)

function findAdjacentSeatVos(allSeats, seatCount)
    local adjacentSeats = {}

    -- 对可用座位排序
    table.sort(allSeats, function(s1, s2)
        if s1.rowCode == s2.rowCode then
            return s1.colCode < s2.colCode
        else
            return s1.rowCode < s2.rowCode
        end
    end)

    -- 寻找相邻座位
    for i = 1, #allSeats - seatCount + 1 do
        local seatsFound = true
        for j = 0, seatCount - 2 do
            local current = allSeats[i + j]
            local next = allSeats[i + j + 1]

            if not (current.rowCode == next.rowCode and next.colCode - current.colCode == 1) then
                seatsFound = false
                break
            end
        end
        if seatsFound then
            for k = 0, seatCount - 1 do
                table.insert(adjacentSeats, allSeats[i + k])
            end
            return adjacentSeats
        end
    end
    -- 如果没有找到，返回空列表
    return adjacentSeats
end


