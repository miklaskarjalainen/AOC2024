local function serializeTable(val, name, skipnewlines, depth)
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

local function has_value (tab, val)
    for index, value in ipairs(tab) do
        if value == val then
            return index
        end
    end

    return 0
end

local function parse_file(fPath)
    local file = io.open(fPath, "r")
    local line = file:read("l")
    
    local rules = {}
    local updates = {}

    while line ~= nil do
        if line == "" then
            goto continue
        end
    
        -- '92|23'
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
            local update = {}
    
            while true do
                local num = tonumber(string.sub(line, last_idx, last_idx+1))
                last_idx = last_idx + 3
                if num == nil then
                    break
                end
                
                update[#update+1] = num
            end
    
            updates[#updates+1] = update
        end
        ::continue::
        line = file:read("l")
    end

    io.close(file)

    return rules, updates
end

-- Fixes up to one rule breaking, and returns true if did something
local function correctly_ordered(rules, update)
    for i1, v1 in pairs(update) do
        local rule_array = rules[v1]
        if rule_array == nil then goto continue end

        for i2, v2 in pairs(update) do
            if v1 == v2 then goto c2 end
            local rule_idx = has_value(rule_array, v2)
            if rule_idx == 0 then goto c2 end

            -- Now we know that the v1 and v2 have a rule defined.
            if i1 > i2 then
                return false
            end 

            ::c2::
        end

        ::continue::
    end
    return true
end

-- Logic
local rules, updates = parse_file("./input.txt")
local result = 0

for _, update in pairs(updates) do
    if correctly_ordered(rules, update) then
        result = result + update[math.ceil(#update/2)]
        print("Correct: " .. _)
    end

    -- process_updates(rules, update)
end

print(serializeTable(updates))
print(result)


