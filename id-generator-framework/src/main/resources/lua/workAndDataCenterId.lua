-- redis中work_id的key
local snowflake_work_id_key = KEYS[1]
-- redis中data_center_id的key
local snowflake_data_center_id_key = KEYS[2]
-- worker_id的最大阈值
local max_worker_id = tonumber(ARGV[1])
-- data_center_id的最大阈值
local max_data_center_id = tonumber(ARGV[2])
-- 返回的work_id
local return_worker_id = 0
-- 返回的data_center_id
local return_data_center_id = 0
-- work_id初始化flag
local snowflake_work_id_flag = false
-- data_center_id初始化flag
local snowflake_data_center_id_flag = false
-- 构建并返回JSON字符串
local json_result = string.format('{"%s": %d, "%s": %d}',
        'workId', return_worker_id,
        'dataCenterId', return_data_center_id)

-- 如果work_id不存在，则将值初始化为0
if (redis.call('exists', snowflake_work_id_key) == 0) then
    redis.call('set',snowflake_work_id_key,0)
    snowflake_work_id_flag = true
end
-- 如果data_center_id不存在，则将值初始化为0
if (redis.call('exists', snowflake_data_center_id_key) == 0) then
    redis.call('set',snowflake_data_center_id_key,0)
    snowflake_data_center_id_flag = true
end
-- 如果work_id和data_center_id都是初始化了，那么执行返回初始化的值
if (snowflake_work_id_flag and snowflake_data_center_id_flag) then
    return json_result
end
-- 获得work_id的值
local snowflake_work_id = tonumber(redis.call('get',snowflake_work_id_key))
-- 获得data_center_id的值
local snowflake_data_center_id = tonumber(redis.call('get',snowflake_data_center_id_key))

-- 如果work_id的值达到了最大阈值
if (snowflake_work_id == max_worker_id) then
    -- 如果data_center_id的值也达到了最大阈值
    if (snowflake_data_center_id == max_data_center_id) then
        -- 将work_id的值初始化为0
        redis.call('set',snowflake_work_id_key,0)
        -- 将data_center_id的值初始化为0
        redis.call('set',snowflake_data_center_id_key,0)
    else
        -- 如果data_center_id的值没有达到最大值，将进行自增，并将自增的结果返回
        return_data_center_id = redis.call('incr',snowflake_data_center_id_key)
    end
else
    -- 如果work_id的值没有达到最大值，将进行自增，并将自增的结果返回
    return_worker_id = redis.call('incr',snowflake_work_id_key)
end
return string.format('{"%s": %d, "%s": %d}',
        'workId', return_worker_id,
        'dataCenterId', return_data_center_id)