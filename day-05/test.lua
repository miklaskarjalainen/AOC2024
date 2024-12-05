
file = io.open ("./input.txt", "r")

line = file:read("l")

rules = {}
order = {}

while line ~= nil do
    if line == "" then
        goto continue
    end

    if string.find(line, '|') ~= nil then
        local first = tonumber(string.sub(line, 0, 2))
        local second = tonumber(string.sub(line, 4, 5))
        
        if rules[first] == nil then
            rules[first] = { second }
        else
            rules[first][#rules[first]+1] = second
        end
    else
        local last_idx = 1
        while true do
            local num = tonumber(string.sub(line, last_idx, last_idx+1))
            last_idx = last_idx + 3
            if num == nil then
                break
            end
            
            order[#order+1] = num
        end        

        
        
    end
    


    -- print(line)
    
    
    ::continue::
    line = file:read("l")
end

contents = file:read("l")

function serializeTable(val, name, skipnewlines, depth)
    skipnewlines = skipnewlines or false
    depth = depth or 0

    local tmp = string.rep(" ", depth)

    if name then tmp = tmp .. name .. " = " end

    if type(val) == "table" then
        tmp = tmp .. "{" .. (not skipnewlines and "\n" or "")

        for k, v in pairs(val) do
            tmp =  tmp .. serializeTable(v, k, skipnewlines, depth + 1) .. "," .. (not skipnewlines and "\n" or "")
        end

        tmp = tmp .. string.rep(" ", depth) .. "}"
    elseif type(val) == "number" then
        tmp = tmp .. tostring(val)
    elseif type(val) == "string" then
        tmp = tmp .. string.format("%q", val)
    elseif type(val) == "boolean" then
        tmp = tmp .. (val and "true" or "false")
    else
        tmp = tmp .. "\"[inserializeable datatype:" .. type(val) .. "]\""
    end

    return tmp
end

print(serializeTable(rules))
print(serializeTable(order))

io.close(file)

