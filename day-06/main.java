import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Vector2 {
    public int x = 0, y = 0;
    public Vector2(int p_X, int p_Y) {
        x = p_X;
        y = p_Y;
    }
    public Vector2(Vector2 vec) {
        x = vec.x;
        y = vec.y;
    }
};

enum Direction {
    Up,
    Down,
    Left,
    Right;

    public int getIndex() {
        switch (this) {
            case Direction.Up ->  {
                return 0;
            }
            case Direction.Down ->  {
                return 1;
            }
            case Direction.Left ->  {
                return 2;
            }
            case Direction.Right ->  {
                return 3;
            }
        }
        return -1;
    }

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
    final private boolean [][][]p_GuardVisitedDir;

    final private Vector2 p_GuardStartPos = new Vector2(0,0);
    private Vector2 p_GuardPos = new Vector2(0,0);
    private Direction p_GuardDir = Direction.Up;

    Map(int width, int height) {
        p_Map = new char[width][height];
        p_GuardVisited = new boolean[width][height];
        p_GuardVisitedDir = new boolean[width][height][4];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p_Map[x][y] = '.';
                p_GuardVisited[x][y] = false;

                for (int i = 0; i < 4; i++) {
                    p_GuardVisitedDir[x][y][i] = false;
                }
            }
        }
    }

    public List<Vector2> getTravelArray() {
        List<Vector2> list = new ArrayList<>();

        int width = getWidth();
        int height = getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (p_GuardVisited[x][y]) {
                    list.add(new Vector2(x, y));
                }
            }
        }
        return list;
    }

    public void clearTravel() {
        for (int y = 0; y < getWidth(); y++) {
            for (int x = 0; x < getHeight(); x++) {
                p_GuardVisited[x][y] = false;

                for (int i = 0; i < 4; i++) {
                    p_GuardVisitedDir[x][y][i] = false;
                }
            }
        }
        p_GuardPos.x = p_GuardStartPos.x;
        p_GuardPos.y = p_GuardStartPos.y;
        p_GuardDir = Direction.Up;
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
                p_GuardVisitedDir[target.x][target.y][p_GuardDir.getIndex()] = true;
                p_GuardPos = target;
            }
            case '#', 'O' -> {
                p_GuardDir = p_GuardDir.rotated();
                step();
            }
            default -> {}
        }

        return true;
    }

    public boolean setTile(int x, int y, char tile) {
        if (tile != '^') {
            if (p_GuardStartPos.x == x && p_GuardStartPos.y == y) {
                return false;
            }

            p_Map[x][y] = tile;
        }
        else {
            p_GuardVisited[x][y] = true;
            p_GuardPos.x = x;
            p_GuardPos.y = y;
            p_GuardStartPos.x = x;
            p_GuardStartPos.y = y;
        }

        return true;
    }

    public int getHeight() {
        return p_Map[0].length;
    }

    public int getWidth() {
        return p_Map.length;
    }

    public boolean hasVisitedTile(Direction asDir) {
        Vector2 target = asDir.getDir();
        target.x += p_GuardPos.x;
        target.y += p_GuardPos.y;

        if (target.x < 0 || target.y < 0) {
            return false;
        }
        if (target.x >= getWidth() || target.y >= getHeight()) {
            return false;
        }

        if (p_Map[target.x][target.y] == '#') {
            return hasVisitedTile(asDir.rotated());
        }

        if (p_GuardVisitedDir[target.x][target.y][asDir.getIndex()]) {
            return true;
        }

        return false;
    }

    public boolean doesGuardLoop() {
        while (step()) {
            if (
                hasVisitedTile(p_GuardDir)
            ) {
                return true;
            }
        }

        return false;
    }

    @Override public String toString() {
        String out = "";
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (p_GuardPos.x == x && p_GuardPos.y == y) {
                    out += p_GuardDir.getChar();
                    continue;
                }
                if (p_GuardVisited[x][y]) {
                    char c = 'X';
                    if (p_GuardVisitedDir[x][y][0]) {
                        c = '|';
                    }
                    if (p_GuardVisitedDir[x][y][1]) {
                        if (c != 'X') {
                            c = '+';
                        } 
                        else {
                            c = ':';
                        }
                    }
                    if (p_GuardVisitedDir[x][y][2]) {
                        if (c != 'X') {
                            c = '+';
                        } 
                        else {
                            c = '~';
                        }
                    }
                    if (p_GuardVisitedDir[x][y][3]) {
                        if (c != 'X') {
                            c = '+';
                        } 
                        else {
                            c = '-';
                        }
                    }
                    out += c;
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
            File file = new File(fpath);
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
        {
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
        }

        while (map.step()) {}
        System.out.println(map.toString());

        System.out.println("Part1 result: " + map.getTravelDistance());
        List<Vector2> traveled = map.getTravelArray();
        int loop_variations = 0;
        for (Vector2 pos : traveled) {
            map.clearTravel();
            if (!map.setTile(pos.x, pos.y, '#')) {
                continue;
            }
            map.setTile(pos.x, pos.y, '#');
            if (map.doesGuardLoop()) {
                loop_variations += 1;
            }
            map.setTile(pos.x, pos.y, '.');
        }
        System.out.println("Part2 result: " + loop_variations);
    }
};
