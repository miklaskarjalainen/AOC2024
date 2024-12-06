import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import the File class
import java.io.FileReader;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

class Vector2 {
    public int x = 0, y = 0;
    Vector2(int p_X, int p_Y) {
        x = p_X;
        y = p_Y;
    }
};

enum Direction {
    Up,
    Right,
    Down,
    Left;

    public Vector2 getDir() {
        switch (this) {
            case Direction.Up ->  {
                return new Vector2(0, -1);
            }
            case Direction.Down ->  {
                return new Vector2(0, 1);
            }
            case Direction.Left ->  {
                return new Vector2(-1, 0);
            }
            case Direction.Right ->  {
                return new Vector2(1, 0);
            }
        }
        return new Vector2(0,0);
    }

    public char getChar() {
        switch (this) {
            case Direction.Up ->  {
                return '^';
            }
            case Direction.Down ->  {
                return 'V';
            }
            case Direction.Left ->  {
                return '<';
            }
            case Direction.Right ->  {
                return '>';
            }
        }
        return '?';
    }

    public Direction rotated() {
        switch (this) {
            case Direction.Up ->  {
                return Direction.Right;
            }
            case Direction.Down ->  {
                return Direction.Left;
            }
            case Direction.Left ->  {
                return Direction.Up;
            }
            case Direction.Right ->  {
                return Direction.Down;
            }
        }
        return Direction.Right;
    }
};

class Map {
    final private char [][]p_Map;

    final private boolean [][]p_GuardVisited;
    private Vector2 p_GuardPos = new Vector2(0,0);
    private Direction p_GuardDir = Direction.Up;

    Map(int width, int height) {
        p_Map = new char[width][height];
        p_GuardVisited = new boolean[width][height];
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p_Map[x][y] = '.';
                p_GuardVisited[x][y] = false;
            }
        }
    }

    public boolean step() {
        Vector2 target = p_GuardDir.getDir();
        target.x += p_GuardPos.x;
        target.y += p_GuardPos.y;

        if (target.x < 0 || target.y < 0) {
            return false;
        }
        if (target.x >= getWidth() || target.y >= getHeight()) {
            return false;
        }

        switch (p_Map[target.x][target.y]) {
            case '.' -> {
                p_GuardVisited[target.x][target.y] = true;
                p_GuardPos = target;
            }
            case '#' -> {
                p_GuardDir = p_GuardDir.rotated();
                step();
            }
            default -> {}
        }


        return true;
    }

    public void setTile(int x, int y, char tile) {
        if (tile != '^') {
            p_Map[x][y] = tile;
        }
        else {
            p_GuardVisited[x][y] = true;
            p_GuardPos.x = x;
            p_GuardPos.y = y;
        }
    }

    public int getHeight() {
        return p_Map[0].length;
    }

    public int getWidth() {
        return p_Map.length;
    }

    @Override public String toString() {
        String out = "";
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (p_GuardPos.x == x && p_GuardPos.y == y) {
                    out += p_GuardDir.getChar();
                    continue;
                }

                out += p_Map[x][y];
            }
            out += '\n';
        }
        return out;
    }

    public int getTravelDistance() {
        int visit = 0;
        for (boolean i[] : p_GuardVisited) {
            for (boolean b : i) {
                if (b) {
                    visit ++;
                }
            }
        }
        return visit;
    }
};

class AOC2024
{
    private static String read_file(String fpath) {
        try {
            File file = new File("./input.txt");
            Scanner scan = new Scanner(file);  
            scan.useDelimiter("\\Z");  
            return scan.next(); 
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            return "";
        }
    }

    public static void main(String []args) {
        String content = read_file("./input.txt");
        
        int height = (int)content.lines().count();
        int width = (int)content.length() / height;

        Map map = new Map(width,height);

        int x = 0;
        int y = 0;
        for (char c : content.toCharArray()) {
            switch (c) {
                case '#', '^' -> {
                    map.setTile(x, y, c);
                }
                case '\n' -> {
                    y += 1;
                    x = 0;
                    continue;
                }

                default ->  {
                }
            }

            x += 1;
        }

        while (map.step()) {}

        System.out.println(map.toString());
        System.out.println("Result: " + map.getTravelDistance());
    }
};
