#include <stdio.h>

// Returns the month corresponding to its numerical value
// Ex: 1 returns January, 2 returns February, etc.

int main()
{
    int monthSelected;
    printf("Please enter a number corresponding to a month [1 - Jan, 2 - Feb, etc.]\n");
    scanf("%d", &monthSelected);

    while(monthSelected < 1 || monthSelected > 12)
    {
        printf("Please select a valid month number.\n");
        scanf("%d", &monthSelected);
    }

    char* months[12] = {
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                        };

    printf("You selected %s.\n", months[monthSelected - 1]);
    return 0;
}