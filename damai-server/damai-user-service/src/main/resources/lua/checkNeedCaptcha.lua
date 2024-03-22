local counter_count_key = KEYS[1]
local counter_timestamp_key = KEYS[2]
local verify_captcha_id = KEYS[3]
local verify_captcha_threshold = tonumber(ARGV[1])
local current_time_millis = tonumber(ARGV[2])
local verify_captcha_id_expire_time = tonumber(ARGV[3])
local always_verify_captcha = tonumber(ARGV[4])
local differenceValue = 1000
if always_verify_captcha == 1 then
    redis.call('set', verify_captcha_id,'yes')
    redis.call('expire',verify_captcha_id,verify_captcha_id_expire_time)
    return 'true'
end
local count = tonumber(redis.call('get', counter_count_key) or "0")
local lastResetTime = tonumber(redis.call('get', counter_timestamp_key) or "0")
if current_time_millis - lastResetTime > differenceValue then
    count = 0
    redis.call('set', counter_count_key, count)
    redis.call('set', counter_timestamp_key, current_time_millis)
end
count = count + 1
if count > verify_captcha_threshold then
    count = 0
    redis.call('set', counter_count_key, count)
    redis.call('set', counter_timestamp_key, current_time_millis)
    redis.call('set', verify_captcha_id,'yes')
    redis.call('expire',verify_captcha_id,verify_captcha_id_expire_time)
    return 'true'
end
redis.call('set', counter_count_key, count)
redis.call('set',verify_captcha_id,'no')
redis.call('expire',verify_captcha_id,verify_captcha_id_expire_time)
return 'false'
