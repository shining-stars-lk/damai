-- 票档数量的key
local program_ticket_remain_number_hash_key = KEYS[1]
local seat_hash_key_del = KEYS[2]
local seat_hash_key_add = KEYS[3]
-- 票档数量数据
local ticket_category_list = cjson.decode(ARGV[1]) 
-- 如果是订单创建，那么就扣除未售卖的座位id
-- 如果是订单取消，那么就扣除锁定的座位id
local seat_id_list = cjson.decode(ARGV[2])
-- 如果是订单创建的操作，那么添加到锁定的座位数据
-- 如果是订单订单的操作，那么添加到未售卖的座位数据
local seat_data_list = cjson.decode(ARGV[3])

-- 如果是订单创建，则扣票档数量
-- 如果是订单取消，则恢复票档数量
for index,increase_data in ipairs(ticket_category_list) do
    local ticket_category_id = increase_data.ticketCategoryId
    local increase_count = increase_data.count
    redis.call('HINCRBY',program_ticket_remain_number_hash_key,ticket_category_id,increase_count)
end
-- 如果是订单创建,将没有售卖的座位删除，再将座位数据添加到锁定的座位中
-- 如果是订单取消,将锁定的座位删除，再将座位数据添加到没有售卖的座位中
redis.call('HDEL',seat_hash_key_del,unpack(seat_id_list))
redis.call('HMSET',seat_hash_key_add,unpack(seat_data_list))


