// See https://aka.ms/new-console-template for more information
Console.WriteLine("Hello, World!");

string[] reports = File.ReadAllLines("input2.txt");

Int64 safe_count = 0;

foreach (string report in reports) {
    bool safe = true;
    Int64? last_num = null; 
    bool? decreasing = null;


    string[] levels = report.Split(' ');

    foreach (string level in levels) {
        Console.Write(level + " ");


        Int64 num = Int64.Parse(level);
        if (last_num == null) {
            last_num = num;
            continue;
        }

        Int64 difference = num - (Int64)last_num;
        last_num = num;
        if (Math.Abs(difference) < 1 || Math.Abs(difference) > 3) {
            safe = false;
            break;
        }

        bool is_decreasing = difference < 0;
        if (decreasing == null) {
            decreasing = is_decreasing;
            continue;
        }
        else if (is_decreasing != decreasing) {
            safe = false;
            break;
        }

    }



    if (safe) {
        Console.WriteLine("Safe!");
        safe_count++;
    }
    else {
        Console.WriteLine("UnSafe!");
    }

}
Console.WriteLine(safe_count);


