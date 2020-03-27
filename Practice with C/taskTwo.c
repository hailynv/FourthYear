#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

// Generating random strings and allocating them to an array
// Then we iterate through the array to get the desired elements.

char * randStringGen(int stringLength)
{
    char letters[26] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                        'w', 'x', 'y', 'z'}; // character library
    
    int randCharIndex, index;
    char * returnString = malloc(sizeof(char) * (stringLength + 1)); // allocating space for the random string

     for (index = 0; index < stringLength; index++) // generating a random string
    {
        randCharIndex = rand() % 26;                // picking a random character index
        returnString[index] = letters[randCharIndex]; // adding random character to a string
    }

    return returnString;
}

int main()
{
    int index, strLength;
    char * randStrings[10]; // array of random strings
    srand(time(0));

    printf("All elements: \n");
    for(index = 0; index < 10; index++)
    {
        strLength = ((rand() % 11) + 1); // random string length [1-10]
        randStrings[index] = randStringGen(strLength); // adding randomly generated strings to the array
        printf("\tElement %d: %s\n", index, randStrings[index]); 
    }

    printf("\nElements at even indexes: \n"); //output 1

    for(index = 0; index < 10; index++)
    {
        if (index % 2 == 0) // if we're at an even index it'll print the string
        {
            printf("\tElement %d: %s\n", index, randStrings[index]);
        }
    }

    printf("\nElements of even length: \n"); // output 2
    for (index = 0; index < 10; index++)
    {
        if (strlen(randStrings[index]) % 2 == 0) // if a string's length is even print the string
        {
            printf("\tElement %d: %s\n", index, randStrings[index]);
        }
    }

    printf("\nElements in reverse order: \n"); // output 3
    for (index = 9; index >= 0; index--)
    {
        printf("\tElement %d: %s\n", index, randStrings[index]);
    }

    printf("\nFirst and last elements: \n"); // output 4
    printf("\tFirst: %s\n", randStrings[0]);
    printf("\tLast: %s\n", randStrings[9]);
    
    return 0;
}
