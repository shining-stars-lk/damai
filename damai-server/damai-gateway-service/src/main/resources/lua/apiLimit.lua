local trigger_result = 0
local trigger_call_Stat = 0
local api_count = 0
local threshold = 0
local apiRule = cjson.decode(KEYS[1])
local api_rule_type = apiRule.apiRuleType
local rule_key = apiRule.ruleKey
local rule_stat_time = apiRule.statTime
local rule_threshold = apiRule.threshold
local rule_effective_time = apiRule.effectiveTime
local rule_limit_key = apiRule.ruleLimitKey
local z_set_key = apiRule.zSetRuleStatKey

local current_Time = apiRule.currentTime
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
        local z_set_member = current_Time .. "_" .. tostring(count)
        redis.call('zadd',z_set_key,current_Time,z_set_member)
    end
    trigger_result = 1
end

if (redis.call('exists', rule_limit_key) == 1) then
    trigger_result = 1
end
api_count = count
threshold = rule_threshold

if (api_rule_type == 2) then
    local depthRules = apiRule.depthRules
    for index,depth_rule in ipairs(depthRules)  do
        local start_time_window = depth_rule.startTimeWindowTimestamp
        local end_time_window = depth_rule.endTimeWindowTimestamp
        local depth_rule_stat_time = depth_rule.statTime
        local depth_rule_threshold = depth_rule.threshold
        local depth_rule_effective_time = depth_rule.effectiveTime
        local depth_rule_limit_key = depth_rule.depthRuleLimit

        threshold = depth_rule_threshold

        if (current_Time > start_time_window) then
            redis.call('zremrangebyscore',z_set_key,0,start_time_window - 1000)
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
                    message_index = index
                    return string.format('{"triggerResult": %d, "triggerCallStat": %d, "apiCount": %d, "threshold": %d, "messageIndex": %d}'
                    ,trigger_result,trigger_call_Stat,api_count,threshold,message_index)
                end
            end
            if (redis.call('exists', depth_rule_limit_key) == 1) then
                trigger_result = 1
                message_index = index
                return string.format('{"triggerResult": %d, "triggerCallStat": %d, "apiCount": %d, "threshold": %d, "messageIndex": %d}'
                ,trigger_result,trigger_call_Stat,api_count,threshold,message_index)
            end
        end
    end
end
return string.format('{"triggerResult": %d, "triggerCallStat": %d, "apiCount": %d, "threshold": %d, "messageIndex": %d}'
,trigger_result,trigger_call_Stat,api_count,threshold,message_index)
