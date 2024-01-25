local program_seat_lock_hash_key = KEYS[1]
local program_seat_no_sold_hash_key = KEYS[2]
local program_ticket_remain_number_hash_key = KEYS[3]
local un_lock_seat_id_list = cjson.decode(ARGV[1])
local seat_no_sold_data_list = cjson.decode(ARGV[2])
local increase_data_list = cjson.decode(ARGV[3])
-- 将锁定的座位集合进行扣除
redis.call('HDEL',program_seat_lock_hash_key,unpack(un_lock_seat_id_list))
-- 将没有售卖的座位集合添加
redis.call('HMSET',program_seat_no_sold_hash_key,unpack(seat_no_sold_data_list))
-- 恢复库存
for index,increase_data in ipairs(increase_data_list) do
    local ticket_category_id = increase_data.ticketCategoryId
    local increase_count = increase_data.increaseCount
    redis.call('HINCRBY',program_ticket_remain_number_hash_key,ticket_category_id,increase_count)
end