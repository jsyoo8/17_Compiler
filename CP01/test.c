#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 
#include <time.h> 
enum bazValue {TRUE,FALSE,BAZ}; 
int main(void){ 
srand(time(NULL)); 
char tempGetValue[30] = "\0"; 
int tempShowValue = 0;
enum bazValue x=TRUE;
if(x==TRUE) { 
printf("true");
}
if(x==TRUE) {printf("true");} 
else if(x==FALSE) {printf("false");} 
else if(x==BAZ) {tempShowValue=rand()%2; if(tempShowValue) {printf("false");} else {printf("true");};} 
else {printf("YOU ARE WRONG!");}
return 1;}