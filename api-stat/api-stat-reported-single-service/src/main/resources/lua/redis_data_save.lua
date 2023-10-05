local oldMethodDetailIdRelKey = KEYS[1]
local methodDataStr = ARGV[1]
local methodData = cjson.decode(methodDataStr)
local exceptionFlag = tonumber(ARGV[2])
local oldMethodDetailDataStr = ''
local newAvgExecuteTime = ''


if (redis.call('exists', oldMethodDetailIdRelKey) == 0) then
    local oldMethodDetailData = {
        id = methodData.id,
        className = methodData.className,
        methodName = methodData.methodName,
        argumentCount = methodData.argumentCount,
        methodLevel = methodData.methodLevel,
        executeTime = methodData.executeTime,
        api = methodData.api,
        avgExecuteTime = methodData.executeTime,
        maxExecuteTime = methodData.executeTime,
        minExecuteTime = methodData.executeTime
    }
    oldMethodDetailDataStr = cjson.encode(oldMethodDetailData)
    newAvgExecuteTime = oldMethodDetailData.avgExecuteTime
else
    oldMethodDetailDataStr = redis.call('get',oldMethodDetailIdRelKey)
    local oldMethodDetailData = cjson.decode(oldMethodDetailDataStr)
    local avg1 = tonumber(methodData.executeTime)
    local avg2 = tonumber(oldMethodDetailData.avgExecuteTime)
    newAvgExecuteTime = (avg1 + avg2) / 2
    oldMethodDetailData.avgExecuteTime = newAvgExecuteTime

    local newMaxExecuteTime = oldMethodDetailData.maxExecuteTime
    if (methodData.executeTime > newMaxExecuteTime) then
        newMaxExecuteTime = methodData.executeTime
    end
    local newMinExecuteTime = oldMethodDetailData.minExecuteTime
    if (methodData.executeTime < newMinExecuteTime) then
        newMinExecuteTime = methodData.executeTime
    end
    oldMethodDetailData.maxExecuteTime = newMaxExecuteTime
    oldMethodDetailData.minExecuteTime = newMinExecuteTime
    if (exceptionFlag == 1) then
        oldMethodDetailData.exceptionCount = oldMethodDetailData.exceptionCount + 1
    end
    oldMethodDetailDataStr = cjson.encode(oldMethodDetailData)
end
redis.call('set',oldMethodDetailIdRelKey,oldMethodDetailDataStr)
return tostring(newAvgExecuteTime)