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
local function correctly_ordered(rules, update, fix)
    for i1, v1 in pairs(update) do
        local rule_array = rules[v1]
        if rule_array == nil then goto continue end

        for i2, v2 in pairs(update) do
            if v1 == v2 then goto c2 end
            local rule_idx = has_value(rule_array, v2)
            if rule_idx == 0 then goto c2 end

            -- Now we know that the v1 and v2 have a rule defined.
            if i1 > i2 then
                if fix then
                    -- Just do a swap
                    update[i1], update[i2] = update[i2], update[i1]
                end

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
local p1_result = 0
local p2_result = 0

for _, update in pairs(updates) do
    if correctly_ordered(rules, update, false) then
        p1_result = p1_result + update[math.ceil(#update/2)]
    else
        while not correctly_ordered(rules, update, true) do end
        p2_result = p2_result + update[math.ceil(#update/2)]
    end

end

print("Part1: " .. p1_result)
print("Part2: " .. p2_result)
