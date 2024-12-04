#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>
#include <string.h>

char* read_file_contents(const char* fpath) {
    // Open with error handling
    FILE *f = fopen(fpath, "rb");
    if (f == NULL) {
        printf("Could not fopen path '%s'\n", fpath);
        return NULL;
    }

    // Seek the end of file
    fseek(f, 0, SEEK_END);
    size_t fsize = ftell(f);
    fseek(f, 0, SEEK_SET);  /* same as rewind(f); */

    // Create a buffer and read the file contents into the buffer.
    char *string = malloc(fsize + 1);
    unsigned long r = fread(string, fsize, 1, f);
    fclose(f);

    // Terminate with null
    string[fsize] = '\0';
    return string;
}

char* contents = NULL;
size_t len = 0;

bool eat_if(char target, size_t* i) {
    if (contents[*i] == target) {
        (*i)++;
        return true;
    }
    return false;
}

int eat_digit(char c) {
    switch (c) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            return c - '0';

        default:
            return -1;
    }
}

int eat_number(size_t* i) {
    int result = 0;

    for (int j = 0; j < 3; j++) {
        int digit = eat_digit(contents[*i]);
        if (digit == -1) {
            break;
        }
        (*i)++;

        result *= 10;
        result += digit;
    }

    return result;
}

int64_t solve(bool part2) {
    contents = read_file_contents("./input.txt");
    len = strlen(contents);

    int64_t result = 0;
    bool enabled = true;

    for (size_t i = 0; i < len; ) {
        char c = contents[i];
        if (eat_if('d', &i)) {
            if (eat_if('o', &i)) {
                if (eat_if('(', &i)) {
                    if (eat_if(')', &i)) {
                        enabled = true;
                    }
                }

                else if (eat_if('n', &i)) {
                    if (eat_if('\'', &i)) {
                        if (eat_if('t', &i)) {
                            if (eat_if('(', &i)) {
                                if (eat_if(')', &i)) {
                                    enabled = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!enabled && part2) {
            i++;
            continue;
        }

        if (!eat_if('m', &i)) {
            i++;
            continue;
        }

        if (!eat_if('u', &i)) {
            continue;
        }
        if (!eat_if('l', &i)) {
            continue;
        }
        if (!eat_if('(', &i)) {
            continue;
        }

        int arg1 = eat_number(&i);
        if (!arg1) {
            continue;
        }

        if (!eat_if(',', &i)) {
            continue;
        }

        int arg2 = eat_number(&i);
        if (!arg2) {
            continue;
        }
        if (!eat_if(')', &i)) {
            continue;
        }
        result += arg1 * arg2;
    }

    free(contents);
    return result;
}

int main(void) {
    int64_t p1_result = solve(false);
    int64_t p2_result = solve(true);

    printf("Part1 result: %li\n", p1_result);
    printf("Part2 result: %li\n", p2_result);
    return 0;
}
