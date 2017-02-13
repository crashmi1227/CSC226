import java.util.*;
import java.io.*;

public class RedBlackBST{
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;
    private class Node{
        int key;
        Node left, right; 
        int N; 
        boolean color;

        Node(int key, int N, boolean color){
            this.key = key;
            this.N = N;
            this.color = color;
        }
    }

    private boolean isRed(Node x){
        if(x == null) return false;
        return x.color == RED;
    }

    private Node rotateLeft(Node h){
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left)+ size(h.right);
        return x;
    }

    private Node rotateRight(Node h){
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left)
        + size(h.right);
        return x;
    }

    private void flipColors(Node h){
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }


    public int size(){ 
        return size(root); 
    }

    private int size(Node x){
        if (x == null) return 0;
        else return x.N;
    }

    public void put(int key){
        root = put(root, key);
        root.color= BLACK;
    }

    private Node put(Node h, int key){
        if(h == null) return new Node(key, 1, RED);

        if(key < h.key) h.left = put(h.left, key);
        else if(key > h.key) h.right = put(h.right, key);
        else h.key = key;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    public double percentRed(){
        int r = countRed(root);
        return (double)r/root.N;
    }

    private int countRed(Node n){
        if(n == null) return 0;

        int r1= countRed(n.left);
        int r2= countRed(n.right);

        int sum = r1+r2;
        if(isRed(n)) sum++;
        return sum;
    }

    public static void main(String args[])throws IOException{
        if(args.length == 0){
            
            Random r = new Random();
            int n = 10000;

            for(int j=0; j<3; j++){
                long startTime = System.nanoTime();

                RedBlackBST bst = new RedBlackBST();

                for(int i =0; i<n; i++){
                    bst.put(new Integer(r.nextInt()));
                }
                System.out.printf("Read %d values.\n",n);
                System.out.println(bst.percentRed());
                n=n*10;

		        long endTime = System.nanoTime();
		        long totalTime = (endTime-startTime);
		        System.out.printf("Total Time (nanoseconds): %d\n",totalTime);
            }


             
        }else{
            long startTime = System.nanoTime();

            Scanner s = new Scanner(new File(args[0]));
            RedBlackBST bst = new RedBlackBST();
            while(s.hasNext()){
                int val = s.nextInt();
                bst.put(new Integer(val));
            }

            s.close();            
            System.out.println(bst.percentRed());

            long endTime = System.nanoTime();
            long totalTime = (endTime - startTime);
            System.out.printf("Total Time (nanoseconds): %d\n",totalTime);

        }
    }
}