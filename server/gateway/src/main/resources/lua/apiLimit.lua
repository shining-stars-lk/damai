local trigger_result = 0
local trigger_call_Stat = 0
local api_count = 0
local threshold = 0
local api_rule_type = tonumber(KEYS[1])
local rule_key = KEYS[2]
local rule_stat_time = KEYS[3]
local rule_threshold = tonumber(KEYS[4])
local rule_effective_time = KEYS[5]
local rule_limit_key = KEYS[6]
local z_set_key = KEYS[7]

local z_set_score = ARGV[1]
local message_index = -1

local count = tonumber(redis.call('incrby', rule_key, 1))
if (count == 1) then
    redis.call('expire', rule_key, rule_stat_time)
end
if ((count - rule_threshold) >= 0) then
    if (redis.call('exists', rule_limit_key) == 0) then
        redis.call('set', rule_limit_key, rule_limit_key)
        redis.call('expire', rule_limit_key, rule_effective_time)
        trigger_call_Stat = 1
        local z_set_member = z_set_score .. "_" .. tostring(count)
        redis.call('zadd',z_set_key,z_set_score,z_set_member)
    end
    trigger_result = 1
end

if (redis.call('exists', rule_limit_key) == 1) then
    trigger_result = 1
end
api_count = count
threshold = rule_threshold

if (api_rule_type == 2) then
    local depth_rule_size = tonumber(KEYS[8])
    local parameter_index = 9
    local time_parameter_index = 2
    for index = 1, depth_rule_size do
        local current_Time = tonumber(ARGV[1])
        local start_time_window = tonumber(ARGV[time_parameter_index])
        local end_time_window = tonumber(ARGV[time_parameter_index+1])
        local depth_rule_stat_time = tonumber(KEYS[parameter_index])
        local depth_rule_threshold = tonumber(KEYS[parameter_index+1])
        local depth_rule_effective_time = KEYS[parameter_index+2]
        local depth_rule_limit_key = KEYS[parameter_index+3]

        threshold = depth_rule_threshold

        if (current_Time > start_time_window) then
            redis.call('zremrangebyscore',0,start_time_window - 1000)
        end

        if (current_Time >= start_time_window and current_Time <= end_time_window) then
            local z_set_min_score = start_time_window;
            local z_set_max_score = current_Time;
            if ((current_Time - start_time_window) > depth_rule_stat_time * 1000) then
                z_set_min_score = current_Time - (depth_rule_stat_time * 1000)
            end
            local rule_trigger_count = tonumber(redis.call('zcount',z_set_key,z_set_min_score,z_set_max_score))
            api_count = rule_trigger_count
            if ((rule_trigger_count - depth_rule_threshold) >= 0) then
                if (redis.call('exists', depth_rule_limit_key) == 0) then
                    redis.call('set', depth_rule_limit_key, depth_rule_limit_key)
                    redis.call('expire', depth_rule_limit_key, depth_rule_effective_time)
                    trigger_result = 1
                    trigger_call_Stat = 2
                    message_index = index - 1
                    return {trigger_result,trigger_call_Stat,api_count,threshold,message_index}
                end
            end
            if (redis.call('exists', depth_rule_limit_key) == 1) then
                trigger_result = 1
                message_index = index - 1
                return {trigger_result,trigger_call_Stat,api_count,threshold,message_index}
            end
        end
        parameter_index = parameter_index + 4
        time_parameter_index = time_parameter_index + 2
    end
end
return {trigger_result,trigger_call_Stat,api_count,threshold,message_index}
