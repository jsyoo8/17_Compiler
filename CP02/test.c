int array[2];

int percent(int a, int b){
   return a%b;
}

int main(void) {

	int result = 10;
	
	array[0] = 10;
	array[1] = 5;
	
	result = percent(array[0], array[1]);
	
if(result>0){
	print(array[0]);
	return 0;
	}
	print(array[1]);
	return 1;
}