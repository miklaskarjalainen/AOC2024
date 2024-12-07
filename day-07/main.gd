extends SceneTree

func get_lines() -> PackedStringArray:
	var file = FileAccess.open("res://input.txt", FileAccess.READ)
	var content = file.get_as_text()
	return content.split("\n", false)

func _recursive(target: int, lhs: int, args: Array[int]) -> int:
	if args.is_empty():
		return lhs
	
	var rhs: int = args.pop_front()
	
	var addition := lhs + rhs
	var result := _recursive(target, addition, args.duplicate())
	if result == target:
		return result
	var multiply := lhs * rhs
	result = _recursive(target, multiply, args.duplicate())
	if result == target:
		return result
	
	return -1

func has_operators(target: int, args: Array[int]) -> bool:
	var lhs: int = args.pop_front()
	print(args)
	return _recursive(target, lhs, args) == target

func _initialize() -> void:
	var lines := get_lines()
	var result := 0
	for line in lines:
		var arguments: PackedStringArray = line.split(" ", false)
		var target = int(arguments[0].replace(":", ""))
		
		var args: Array[int] = []
		for i in range(1, arguments.size()):
			args.push_back(int(arguments[i]))
		
		if has_operators(target, args):
			result += target
	print("Part1 result: ", result)
	
	quit()
