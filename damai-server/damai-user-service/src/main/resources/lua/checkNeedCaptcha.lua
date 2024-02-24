-- 计数器的键
local countKey = KEYS[1]
-- 时间戳的键
local timeKey = KEYS[2]
-- 是否需要校验验证码的键
local verifyCaptchaKey = KEYS[3]
-- 每秒最大请求次数
local maxRequestsPerSecond = tonumber(ARGV[1])
-- 当前时间戳
local currentTime = tonumber(ARGV[2])
-- 唯一标识
local id = tonumber(ARGV[3])
-- 时间窗口大小，1000毫秒，即1秒
local differenceValue = 1000
-- 获取当前计数和上次重置时间
local count = tonumber(redis.call('get', countKey) or "0")
local lastResetTime = tonumber(redis.call('get', timeKey) or "0")
-- 检查时间窗口是否已过，如果是，则重置计数和时间
if currentTime - lastResetTime >= differenceValue then
    count = 0
    redis.call('set', countKey, count)
    redis.call('set', timeKey, currentTime)
end
-- 更新计数
count = count + 1
-- 判断是否超过限制
if count > maxRequestsPerSecond then
    -- 超过限制，可以在这里记录日志（注意：Redis自身不支持日志记录到文件，需要另外处理）
    -- 重置计数和时间戳
    count = 0
    redis.call('set', countKey, count)
    redis.call('set', timeKey, currentTime)
    redis.call('hset',verifyCaptchaKey,id,'yes')
    return string.format('{"verifyCaptcha": %d, "id": %d}',1,id)
end
-- 未超过限制，更新计数
redis.call('set', countKey, count)
redis.call('hset',verifyCaptchaKey,id,'no')
return string.format('{"verifyCaptcha": %d, "id": %d}',0,id)
