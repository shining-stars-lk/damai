local result = 0
local count = 0
local thresholdValue = 0
local markWord = '' 
if (redis.call('exists', KEYS[2]) == 1) then
    local statisticTime = redis.call('hget', KEYS[2], 'statisticTime')
    thresholdValue = tonumber(redis.call('hget', KEYS[2], 'thresholdValue'))
    local limitTime = redis.call('hget', KEYS[2], 'limitTime')
    markWord = redis.call('hget', KEYS[2], 'markWord')
    count = tonumber(redis.call('incrby', KEYS[1], 1))
    if (count == 1) then
        redis.call('expire', KEYS[1], statisticTime)
    end
    if (count >= thresholdValue) then
        redis.call('set', KEYS[3], KEYS[3])
        redis.call('expire', KEYS[3], limitTime)
    end
    if (redis.call('exists', KEYS[3]) == 1) then
        result = 1;
    end
end
return {result,count,thresholdValue,markWord}