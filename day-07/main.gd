extends SceneTree

func get_lines() -> PackedStringArray:
	var file = FileAccess.open("res://input.txt", FileAccess.READ)
	var content = file.get_as_text()
	return content.split("\n", false)

func _recursive(target: int, lhs: int, args: Array[int], part2: bool) -> int:
	if args.is_empty():
		return lhs
	
	var rhs: int = args.pop_front()
	
	var addition := lhs + rhs
	var result := _recursive(target, addition, args.duplicate(), part2)
	if result == target:
		return result
	var multiply := lhs * rhs
	result = _recursive(target, multiply, args.duplicate(), part2)
	if result == target:
		return result
	
	# If it works, it works :p
	var concatenation := int(str(lhs) + str(rhs))
	result = _recursive(target, concatenation, args.duplicate(), part2)
	if result == target and part2:
		return result
	
	return -1

func has_operators(target: int, args: Array[int], part2: bool) -> bool:
	var lhs: int = args.pop_front()
	print(args)
	return _recursive(target, lhs, args, part2) == target

func _initialize() -> void:
	var lines := get_lines()
	var p1_result := 0
	var p2_result := 0
	
	for line in lines:
		var arguments: PackedStringArray = line.split(" ", false)
		var target = int(arguments[0].replace(":", ""))
		
		var args: Array[int] = []
		for i in range(1, arguments.size()):
			args.push_back(int(arguments[i]))
		
		if has_operators(target, args.duplicate(), false):
			p1_result += target
		if has_operators(target, args, true):
			p2_result += target
	
	print("Part1 result: ", p1_result)
	print("Part2 result: ", p2_result)
	
	quit()
