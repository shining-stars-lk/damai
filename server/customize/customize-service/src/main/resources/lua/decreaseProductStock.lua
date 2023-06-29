local keyArray = KEYS
local stockArray = ARGV
local rollBackKeyArray = {}
local rollBackStockArray = {}
local value = 1;

for i = 1, #keyArray do
    local key = keyArray[i]
    local stock = stockArray[i]
    local count = tonumber(redis.call('decrby', key, stock))
    table.insert(rollBackKeyArray,key)
    table.insert(rollBackStockArray,stock)
    if count < 0 then
        value = -1
        break
    end
end
if value == -1 then
    for i = 1, #rollBackKeyArray do
        local key = rollBackKeyArray[i]
        local stock = rollBackStockArray[i]
        redis.call('incrby', key, stock)
    end
end
return value