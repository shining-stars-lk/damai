local operate_order_status = tonumber(KEYS[1])
local program_seat_lock_hash_key = KEYS[2]
local program_seat_hash_key = KEYS[3]
local un_lock_seat_id_list = cjson.decode(ARGV[1])
local seat_data_list = cjson.decode(ARGV[2])
redis.call('HDEL',program_seat_lock_hash_key,unpack(un_lock_seat_id_list))
redis.call('HMSET',program_seat_hash_key,unpack(seat_data_list))
if (operate_order_status == 2) then
    local program_ticket_remain_number_hash_key = KEYS[4]
    local increase_data_list = cjson.decode(ARGV[3])
    for index,increase_data in ipairs(increase_data_list) do
        local ticket_category_id = increase_data.ticketCategoryId
        local increase_count = increase_data.count
        redis.call('HINCRBY',program_ticket_remain_number_hash_key,ticket_category_id,increase_count)
    end
end
