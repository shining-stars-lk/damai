local operate_order_status = tonumber(KEYS[1])
local un_lock_seat_id_json_array = cjson.decode(ARGV[1])
local add_seat_data_json_array = cjson.decode(ARGV[2])
for index, un_lock_seat_id_json_object in pairs(un_lock_seat_id_json_array) do
    local program_seat_hash_key = un_lock_seat_id_json_object.programSeatLockHashKey
    local un_lock_seat_id_list = un_lock_seat_id_json_object.unLockSeatIdList
    redis.call('HDEL',program_seat_hash_key,unpack(un_lock_seat_id_list))    
end

for index, add_seat_data_json_object in pairs(add_seat_data_json_array) do
    local seat_hash_key_add = add_seat_data_json_object.seatHashKeyAdd
    local seat_data_list = add_seat_data_json_object.seatDataList
    redis.call('HMSET',seat_hash_key_add,unpack(seat_data_list))
end

if (operate_order_status == 2) then
    local ticket_category_list = cjson.decode(ARGV[3])
    for index,increase_data in ipairs(ticket_category_list) do
        local program_ticket_remain_number_hash_key = increase_data.programTicketRemainNumberHashKey
        local ticket_category_id = increase_data.ticketCategoryId
        local increase_count = increase_data.count
        redis.call('HINCRBY',program_ticket_remain_number_hash_key,ticket_category_id,increase_count)
    end
end