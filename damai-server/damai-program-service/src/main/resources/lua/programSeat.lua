local seat_no_sold_resolution_hash_key = KEYS[1]
local seat_lock_resolution_hash_key = KEYS[2]
local seat_sold_resolution_hash_key = KEYS[3]

local seat_list = {}

local seat_no_sold_resolution_list = redis.call('hvals', seat_no_sold_resolution_hash_key)
if seat_no_sold_resolution_list then
    for index, seat in ipairs(seat_no_sold_resolution_list) do
        table.insert(seat_list,seat)
    end
end
local seat_lock_resolution_list = redis.call('hvals', seat_lock_resolution_hash_key)
if seat_lock_resolution_list then
    for index, seat in ipairs(seat_lock_resolution_list) do
        table.insert(seat_list,seat)
    end
end
local seat_sold_resolution_list = redis.call('hvals', seat_sold_resolution_hash_key)
if seat_sold_resolution_list then
    for index, seat in ipairs(seat_sold_resolution_list) do
        table.insert(seat_list,seat)
    end
end
return seat_list