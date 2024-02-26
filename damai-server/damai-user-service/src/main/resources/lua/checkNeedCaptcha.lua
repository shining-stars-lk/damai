-- 计数器的键
local counter_count_key = KEYS[1]
-- 时间戳的键
local counter_timestamp_key = KEYS[2]
-- 校验验证码id的键
local verify_captcha_id = KEYS[3]
-- 每秒最大请求次数
local verify_captcha_threshold = tonumber(ARGV[1])
-- 当前时间戳
local current_time_millis = tonumber(ARGV[2])
-- 校验验证码id过期时间
local verify_captcha_id_expire_time = tonumber(ARGV[3])
-- 时间窗口大小，1000毫秒，即1秒
local differenceValue = 1000
-- 获取当前计数和上次重置时间
local count = tonumber(redis.call('get', counter_count_key) or "0")
local lastResetTime = tonumber(redis.call('get', counter_timestamp_key) or "0")
-- 检查时间窗口是否已过，如果是，则重置计数和时间
if current_time_millis - lastResetTime >= differenceValue then
    count = 0
    redis.call('set', counter_count_key, count)
    redis.call('set', counter_timestamp_key, current_time_millis)
end
-- 更新计数
count = count + 1
-- 判断是否超过限制
if count > verify_captcha_threshold then
    -- 超过限制，可以在这里记录日志（注意：Redis自身不支持日志记录到文件，需要另外处理）
    -- 重置计数和时间戳
    count = 0
    redis.call('set', counter_count_key, count)
    redis.call('set', counter_timestamp_key, current_time_millis)
    redis.call('set', verify_captcha_id,'yes')
    redis.call('expire',verify_captcha_id,verify_captcha_id_expire_time)
    return 'true'
end
-- 未超过限制，更新计数
redis.call('set', counter_count_key, count)
redis.call('set',verify_captcha_id,'no')
redis.call('expire',verify_captcha_id,verify_captcha_id_expire_time)
return 'false'
