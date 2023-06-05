local count = tonumber(redis.call('incrby', KEYS[1], ARGV[1]))
if (count == 1) then
    redis.call('expire', KEYS[1], ARGV[2])
end
return count