
contents = open("input.txt").readlines()

# Part 1
part1_count = 0
HEIGHT = len(contents)
for y in range(HEIGHT):
    WIDTH = len(contents[y])
    for x in range(WIDTH):
        if contents[y][x] != "X":
            continue 
        
        # Upwards
        if y >= 3:
            if contents[y-1][x] == "M" and contents[y-2][x] == "A" and contents[y-3][x] == "S":
                part1_count += 1
        # Downwards
        if y+3 < HEIGHT:
            if contents[y+1][x] == "M" and contents[y+2][x] == "A" and contents[y+3][x] == "S":
                part1_count += 1
        # Right
        if x+3 < WIDTH:
            if contents[y][x+1] == "M" and contents[y][x+2] == "A" and contents[y][x+3] == "S":
                part1_count += 1
        # Left
        if x >= 3:
            if contents[y][x-1] == "M" and contents[y][x-2] == "A" and contents[y][x-3] == "S":
                part1_count += 1

        # Right Up
        if x+3 < WIDTH and y >= 3:
            if contents[y-1][x+1] == "M" and contents[y-2][x+2] == "A" and contents[y-3][x+3] == "S":
                part1_count += 1
        # Right-Down
        if x+3 < WIDTH and y+3 < HEIGHT:
            if contents[y+1][x+1] == "M" and contents[y+2][x+2] == "A" and contents[y+3][x+3] == "S":
                part1_count += 1
        # Left Up
        if x >= 3 and y >= 3:
            if contents[y-1][x-1] == "M" and contents[y-2][x-2] == "A" and contents[y-3][x-3] == "S":
                part1_count += 1
        # Left Down
        if x >= 3 and y+3 < HEIGHT:
            if contents[y+1][x-1] == "M" and contents[y+2][x-2] == "A" and contents[y+3][x-3] == "S":
                part1_count += 1

print(f"part1 result: {part1_count}")


# Part 2

part2_count = 0
for y in range(1, HEIGHT-1):
    WIDTH = len(contents[y])
    for x in range(1, WIDTH-1):
        if contents[y][x] != "A":
            continue 

        # for _x in range(-1, 2, 2):
        #     for _y in range(-1, 2, 2):
        #         if contents[y + _y][x + _x] == "M":
        #             _m += 1
        #         elif contents[y + _y][x + _x] == "S":
        #             _s += 1
        # if _m == 2 and _s == 2:
        #   part2_count += 1

        # y first
        # (-1,-1).(-1, 1)
        # ( 1,-1).( 1, 1)

        # S.S
        # ...
        # M.M
        if contents[y - 1][x - 1] == "S" and contents[y - 1][x + 1] == "S":
            if contents[y + 1][x - 1] == "M" and contents[y + 1][x + 1] == "M":
                part2_count += 1
                continue
        
        # M.S
        # ...
        # M.S
        if contents[y - 1][x - 1] == "M" and contents[y - 1][x + 1] == "S":
            if contents[y + 1][x - 1] == "M" and contents[y + 1][x + 1] == "S":
                part2_count += 1
                continue
        
        # M.M
        # ...
        # S.S
        if contents[y - 1][x - 1] == "M" and contents[y - 1][x + 1] == "M":
            if contents[y + 1][x - 1] == "S" and contents[y + 1][x + 1] == "S":
                part2_count += 1
                continue
        
        # S.M
        # ...
        # S.M
        if contents[y - 1][x - 1] == "S" and contents[y - 1][x + 1] == "M":
            if contents[y + 1][x - 1] == "S" and contents[y + 1][x + 1] == "M":
                part2_count += 1
                continue

print(f"part2 result: {part2_count}")

