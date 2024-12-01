#[derive(Debug)]
struct Pairs {
    left: Vec<i64>,
    right: Vec<i64>
}

fn parse_text(text: &String) -> Pairs {
    let rows: Vec<&str> = text.split("\r\n").collect();
    let mut result = Pairs { left: vec![], right: vec![] };

    for row in &rows {
        if row.len() == 0 {
            continue;
        }

        let splitted: Vec<&str> = row.split("   ").collect();
        result.left.push(str::parse(splitted[0]).expect("int parse error"));
        result.right.push(str::parse(splitted[1]).expect("int parse error"));
    }
    result
}

fn main() {
    let input = std::fs::read_to_string("./input.txt").err("not input file found!")

    let mut r = parse_text(&input);
    r.left.sort();
    r.right.sort();

    // Part 1, calculating to a vec since doing it into a variable is too hard. xd
    {
        let mut distances = vec![];
        for (left, right) in r.left.iter().zip(r.right.iter()) {
            distances.push((left-right).abs());
        }
        let result: i64 = distances.iter().sum();
        
        println!("part 1 result: {:#?}", result);
    }

    // Part 2, what is optimization?
    {
        
        let mut result: usize = 0;
        for left in r.left {
            let count = r.right.iter().filter(|e| return **e == left).count();
            result += left as usize * count;
        }
        
        println!("part 2 result: {:#?}", result);
    }
}
