local snowflake_id_hash = KEYS[1]
local snowflake_work_id_key = KEYS[2]
local snowflake_data_center_id_key = KEYS[2]
local max_worker_id = ARGV[1]
local max_data_center_id = ARGV[1]
local return_worker_id = 0
local return_data_center_id = 0
local snowflake_work_id_flag = false
local snowflake_data_center_id_flag = false
if (redis.call('exists', snowflake_work_id_key) == 0) then
    redis.call('set',snowflake_work_id_key,0)
    snowflake_work_id_flag = true
end
if (redis.call('exists', snowflake_data_center_id_key) == 0) then
    redis.call('set',snowflake_data_center_id_key,0)
    snowflake_data_center_id_flag = true
end
if (snowflake_work_id_flag and snowflake_data_center_id_flag) then
    return {return_worker_id,return_data_center_id}
end
local snowflake_work_id = tonumber(redis.call('get',snowflake_work_id_key))
local snowflake_data_center_id = tonumber(redis.call('get',snowflake_data_center_id_key))

if (snowflake_work_id == max_worker_id) then
    redis.call('set',snowflake_data_center_id_key,0)
end
if (snowflake_data_center_id == max_data_center_id) then
    redis.call('set',snowflake_data_center_id,0)
end
if (snowflake_work_id ~= max_worker_id) then
    
end
if (snowflake_data_center_id ~= max_data_center_id) then

end