#include <stdlib.h>
#include <stdio.h>
#include <string.h>

// Recursive binary search over an array of strings

int binarySearch(char *strArray[], int lowIdx, int highIdx, char *target)
{
    int midIdx;

    midIdx = lowIdx + (highIdx - lowIdx) / 2;

    if (lowIdx > highIdx)
    {
        return -1; // word not found
    }
    if (strcmp(strArray[midIdx], target) == 0)
    {
        return midIdx;
    }

    if (strcmp(strArray[midIdx], target) > 0)
    {
        return binarySearch(strArray, lowIdx, midIdx - 1, target);
        // recurse left
    }
    else
    {
        return binarySearch(strArray, midIdx + 1, highIdx, target);
        // recurse right
    }
}

int main()
{
    char *words[] = {
        "allow", "apple", "awesome", "beautiful", "beds", "cats", "color", "daisy", "deer",
        "effort", "empathy", "favorite", "great", "higher", "hollow", "illustrious", "imagine",
        "jet", "juggle", "kind", "kitten", "less", "longer", "music", "northern", "open", "proud",
        "prudent", "quiet", "riot"};

    int arrLength = sizeof(words) / sizeof(words[0]);

    printf("Please select a word you would like the index for:\n");

    for (int i = 0; i < arrLength; i++)
    {
        printf("%s\n", words[i]);
    }

    printf("\nSelection:\n");

    char *input = malloc(256);
    scanf("%256s", input);

    int indexOfWord = binarySearch(words, 0, arrLength, input);

    if (indexOfWord != -1)
    {
        printf("Index of '%s': %d\n", input, indexOfWord);
    }
    else
    {
        printf("'%s' was not found in the array of strings.\n", input);
    }

    free(input);

    return 0;
}