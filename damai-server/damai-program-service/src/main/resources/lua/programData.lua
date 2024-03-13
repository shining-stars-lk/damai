local program_ticket_remain_number_hash_key = KEYS[1]
local seat_hash_key_del = KEYS[2]
local seat_hash_key_add = KEYS[3]
local ticket_category_list = cjson.decode(ARGV[1]) 
local seat_id_list = cjson.decode(ARGV[2])
local seat_data_list = cjson.decode(ARGV[3])
for index,increase_data in ipairs(ticket_category_list) do
    local ticket_category_id = increase_data.ticketCategoryId
    local increase_count = increase_data.count
    redis.call('HINCRBY',program_ticket_remain_number_hash_key,ticket_category_id,increase_count)
end
redis.call('HDEL',seat_hash_key_del,unpack(seat_id_list))
redis.call('HMSET',seat_hash_key_add,unpack(seat_data_list))


