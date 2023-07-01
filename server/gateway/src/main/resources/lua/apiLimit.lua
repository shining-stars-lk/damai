local result = 0
local haveException = 0
local ruleStatisticTime = KEYS[2]
local ruleThresholdValue = tonumber(KEYS[3])
local ruleLimitTime = KEYS[4]
local count = tonumber(redis.call('incrby', KEYS[1], 1))
if (count == 1) then
    redis.call('expire', KEYS[1], ruleStatisticTime)
end
if ((count - ruleThresholdValue) >= 0) then
    haveException = 1
end
if (haveException == 1) then
    redis.call('set', KEYS[5], KEYS[5])
    redis.call('expire', KEYS[5], ruleLimitTime)
    result = 1
    return {result,count,ruleThresholdValue}
end
if (redis.call('exists', KEYS[5]) == 1) then
    result = 1
end
return {result,count,ruleThresholdValue}