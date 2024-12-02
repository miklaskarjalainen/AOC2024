string[] reports = File.ReadAllLines("input.txt");

Int64 part1_count = 0;
Int64 part2_count = 0;

bool part1_valid(List<Int64> levels) {
    bool decreasing = true;

    for (int i = 1; i < levels.Count; i++) {
        Int64 diff = levels[i-1] - levels[i];
        if (Math.Abs(diff) < 1 || Math.Abs(diff) > 3) {
            return false;
        }

        bool is_decreasing = diff < 0;

        if (i == 1) {
            decreasing = is_decreasing;
            continue;
        }
        
        if (decreasing != is_decreasing) {
            return false;
        }
    }

    return true;
}

bool part2_valid(List<Int64> levels) {
    for (int i = 0; i < levels.Count; i++) {
        List<Int64> dup = [.. levels];
        dup.RemoveAt(i);

        if (part1_valid(dup)) {
            return true;
        }
    }
    return false;
}

foreach (string report in reports) {
    string[] levels = report.Split(' ');

    List<Int64> int_levels = new();
    foreach (string level in levels) {
        int_levels.Add(Int64.Parse(level));
    }

    if (part1_valid(int_levels)) {
        part1_count++;
        part2_count++;
        continue;
    }
    if (part2_valid(int_levels)) {
        part2_count++;
        continue;
    }
}

Console.WriteLine($"Part1 results: {part1_count}");
Console.WriteLine($"Part2 results: {part2_count}");


