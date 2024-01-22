local program_ticket_remain_number_hash_key = KEYS[1]
local program_seat_no_sold_hash_key = KEYS[2]
local program_seat_lock_hash_key = KEYS[3]
local deduct_data_list = cjson.decode(ARGV[1])
local purchase_seat_id_list = cjson.decode(ARGV[2])
local seat_lock_data_list = cjson.decode(ARGV[3])
-- 扣库存
for index,deduct_data in ipairs(deduct_data_list) do
    local ticket_category_id = deduct_data.ticketCategoryId
    local purchase_count = deduct_data.purchaseCount
    redis.call('HINCRBY',program_ticket_remain_number_hash_key,ticket_category_id,purchase_count)
end
-- 将没有售卖的座位集合进行扣除
redis.call('HDEL',program_seat_no_sold_hash_key,unpack(purchase_seat_id_list))
-- 将锁定的座位集合添加
redis.call('HMSET',program_seat_lock_hash_key,unpack(seat_lock_data_list))
