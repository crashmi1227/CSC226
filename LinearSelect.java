//Template is created by Zhuoli Xiao, on Sept. 19st, 2016.
//Only used for Lab 226, 2016 Fall. 
//All Rights Reserved. 

//modified by Rich Little on Sept. 23, 2016

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.util.Random;


public class LinearSelect {
	//Function to invoke quickSelect
	public static int LS(int[] S, int k){
        if (S.length==1)
        	return S[0];
       
        return linearSelect(0,S.length-1,S,k);


	}
    
    //do quickSelect in a recursive way 
    private static int linearSelect(int left,int right, int[] array, int k){
    	//if there is only one element now, just record.
    	if (left>=right){
    		return array[left];
    	}
    	//do the partition 
    	int p=pickCleverPivot(left,right,array);
    	int eIndex=partition(left,right,array,p);
    	//after the partition, do following ops
    	if (k<=eIndex){
    		return linearSelect(left,eIndex-1,array,k);
    	}else if(k==eIndex+1){
    		return array[eIndex];
    	}else{
    		return linearSelect(eIndex+1,right,array,k);
    	}

    }
	
    //do Partition with a pivot
    private static int partition(int left, int right, int[] array, int pIndex){
    	//move pivot to last index of the array
    	swap(array,pIndex,right);

    	int p=array[right];
    	int l=left;
    	int r=right-1;
  
    	while(l<=r){
    		while(l<=r && array[l]<=p){
    			l++;
    		}
    		while(l<=r && array[r]>=p){
    			r--;
    		}
    		if (l<r){
    			swap(array,l,r);
    		}
    	}

        swap(array,l,right);
    	return l;
    }
	
	 public static int getMedian(int left, int right, int [] array) {
    	boolean swapped=true;
    	int[] values= new int [right-left+1];
    	for (int i=left; i<=right;i++) {
    		values[i-left]=array[i];
    	}
    	while (swapped) {
    		swapped=false;
    		for (int i=0;i<values.length-1;i++) {
    			if (values[i]>values[i+1]) {
    				swap(values,i,i+1);
    				swapped=true;
    			}
    		}
    	}
    	int median = values[(values.length-1)/2];
    	for (int i=left;i<=right;i++) {
    		if (array[i]==median) {
    			return i;
    		}
    	}
    	return -1;
    }
	
	public static int determineEnd(int i, int right){
			if(i+4<=right){
				int e = i+4;
				return e;
			}else{
				int e = right;
				return e;
			}
	}

    //Pick a random pivot to do the QuickSelect
	private static int pickCleverPivot(int left, int right, int [] array){
		int n = right-left+1;
		if (n<=5) {
			return getMedian(left,right,array);
		}

		int count=0;
		for (int i=left;i<=right;i+=5) {
			int end=determineEnd(i, right);
			int index=getMedian(i,end,array);
			swap(array,index,count+left);
			count++;
		}

		return pickCleverPivot(left,left+count-1,array);		
	}
	/*private static int pickRandomPivot(int left, int right){
		int index=0;
		Random rand= new Random();
		index = left+rand.nextInt(right-left+1);
		return index;  
	}*/
	
	//swap two elements in the array
	private static void swap(int[]array, int a, int b){
 		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}


	/* main()
	   Contains code to test the QuickSelect. Nothing in this function
	   will be marked. You are free to change the provided code to test your
	   implementation, but only the contents of the QuickSelect class above
	   will be considered during marking.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);
		
		int k = inputVector.get(0);

		int[] array = new int[inputVector.size()-1];

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i+1);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.nanoTime();

		int kthsmallest = LS(array,k);

		long endTime = System.nanoTime();

		long totalTime = (endTime-startTime);

		System.out.printf("The %d-th smallest element in the input list of size %d is %d.\n",k,array.length,kthsmallest);
		System.out.printf("Total Time (nanoseconds): %d\n",totalTime);
	}
}

