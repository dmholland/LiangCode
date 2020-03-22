import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


class AVLtree {

    ArrayList<String> listing = new ArrayList<String>();

    class AVLNode {
        AVLNode right, left;
        String data;
        int height;


        public AVLNode() {                           //Default Node Constructor
            left = null;
            right = null;
            data = "";
            height = 0;
        }

        AVLNode(String data) {
            left = null;
            right = null;
            this.data = data;
            height = 0;
        }//End Node Constructors

    }//end of AVL Node

    private AVLNode root;

    AVLtree() {
        root = null;
    }

    public boolean empty() {                      //checks if root is null
        return root == null;
    }

    public void makeEmpty() {
        root = null;
    }

    private int height(AVLNode node) {             //strange notation
        return node == null ? -1 : node.height;
    }

    private int max(int lhs, int rhs) {
        return lhs > rhs ? lhs : rhs;
    }


    void insert(String data) {                  //insertion method
        root = insert(data, root);
    }//--------------------------------Insert

    private AVLNode insert(String x, AVLNode node) {

        if (node == null) {
            node = new AVLNode(x);
        } else if ((x.compareTo(node.data)) < 0) {//x<node.data
            node.left = insert(x, node.left);
            if (height(node.left) - height(node.right) == 2) {
                if ((x.compareTo(node.data)) < 0)
                    node = rotateWithLeftChild(node);
                else
                    node = doubleRotateWithLeftChild(node);
            }

        } // End of lesser if
        else if ((x.compareTo(node.data)) > 0) {
            node.right = insert(x, node.right);
            if (height(node.left) - height(node.right) == 2) {
                if ((x.compareTo(node.data)) > 0)
                    node = rotateWithRightChild(node);
                else
                    node = doubleRotateWithRightChild(node);
            }

        }//End of greater if
        else ;
        node.height = max(height(node.left), height(node.right)) + 1;

        return node;
    }//end of insertion method

    private AVLNode rotateWithLeftChild(AVLNode node) {// This simply rotates the left child to parent positiion, right child is still a child
        AVLNode newTop = node.left;
        node.left = newTop.right;
        newTop.right = node;
        node.height = max(height(node.left), height(node.right)) + 1;
        newTop.height = max(height(newTop.left), newTop.height) + 1;
        return newTop;

    }//End of rotate with left child

    private AVLNode rotateWithRightChild(AVLNode node) {
        AVLNode newTop = node.right;
        node.right = newTop.left;
        newTop.left = node;
        node.height = max(height(node.left), height(node.right)) + 1;
        newTop.height = max(height(newTop.left), newTop.height) + 1;
        return newTop;

    }// End of rotate with right child

    private AVLNode doubleRotateWithLeftChild(AVLNode node) {
        node.left = (rotateWithRightChild(node.left));
        return rotateWithLeftChild(node);
    } //End double rotate with left child

    private AVLNode doubleRotateWithRightChild(AVLNode node) {
        node.right = (rotateWithLeftChild(node.right));
        return rotateWithRightChild(node);
    }//End of double rotate with right child

    public int count() {
        return countNodes(root);

    }//end of count method

    private int countNodes(AVLNode node) {

        if (node == null) return 0;
        int l = 1;
        l += countNodes(node.left);
        l += countNodes(node.right);
        return l;
    }

    public boolean search(String value) {

        return search(root, value);
    }// End of search

    private boolean search(AVLNode node, String value) {
        boolean found = false;
        while ((node != null) && !found) {
            String nodeval = node.data;
            if (nodeval.compareTo(value) < 0) //n<v
                node = node.left;
            else if (nodeval.compareTo(value) > 0)//n>v
                node = node.right;
            else {
                found = true;
                break;
            }//If not bigger or smaller, then conditions met, break
            found = search(node, value);  //After assignement do search on assigned node
        }//End of while statement

        return found;
    }//End of private search

    public ArrayList<String> inorder() {
        return inorder(root);

    }

    private ArrayList<String> inorder(AVLNode r) {
        if (r != null) {
            inorder(r.left);
            listing.add(r.data);
            //System.out.print(r.data + " ");
            inorder(r.right);
        }
        return listing;
    }

    /* Function for preorder traversal */
    public void preorder() {
        preorder(root);
    }

    private void preorder(AVLNode r) {
        if (r != null) {
            System.out.print(r.data + " ");
            preorder(r.left);
            preorder(r.right);
        }
    }

    /* Function for postorder traversal */
    public void postorder() {
        postorder(root);
    }

    private void postorder(AVLNode r) {
        if (r != null) {
            postorder(r.left);
            postorder(r.right);
            System.out.print(r.data + " ");
        }
    }


    /* Class AVL Tree Test */
}//end AVLtree

public class Hangman {

    //-------------------------------------------------------------------------------------------------Main Program---------
    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Code\\JavaLiang\\Hangman\\src\\Hung.txt");
        AVLtree tree;
        tree = new AVLtree();
        insertAVLtree(diction(file), tree);
        String word = randomWord(tree);
        try {
            hangMan(word);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//End of main method

    //------------------------------------------------------------------------------------------------Methods--------------
    static ArrayList<String> diction(File file) throws IOException {
        ArrayList<String> dictionary = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader((new FileReader(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String s : line.toLowerCase().split("\\s+")) {
                    dictionary.add(s);
                }  //For each split from line, add to arraylist
            }//End of while loop
        }//End of try block


        return dictionary;
    }// End of diction

    static void insertAVLtree(ArrayList<String> diction, AVLtree tree) {
        int count = 0;
        for (String word : diction) {
            tree.insert(word);
            count++;
        }//End of For loop
        // System.out.println(count + " Words transferred to the AVL tree");
    }//End of insertAVL tree

    static String randomWord(AVLtree tree) {  //Pulls data from AVL tree

        ArrayList<String> list = tree.inorder();
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(list.size() - 1) + 1;
        return list.get(randomInt);
    }// end of random word

    public static void hangMan(String word1) throws Exception {
        String word= word1;
        int spaces = word.length();
        int count = word.length();
        ArrayList<String> wordBank = new ArrayList<String>(spaces);
        for (int i = 0; i < spaces; i++) {
            wordBank.add("*");
        }
        Scanner input = new Scanner(System.in);
        System.out.print("(Guess) Enter a letter in word ");
        for (String s : wordBank) {
            System.out.print(s);
        }
        System.out.print("\n");
        while (count != 0) {
            String guess=input.next();
            if (guess!="" && word.contains(guess)) {
                wordBank.set(word.indexOf(guess), guess);
                count--;
                word.replace(guess,"");
                System.out.println("Fantastic");
            }//End of if contains

            else {System.out.println("Nope no dice. ");}
            System.out.print("(Guess) Enter a letter in word ");
            for (String s : wordBank) {
                System.out.print(s);
            }
            }//end of while loop
            System.out.println(" Thank you for playing hangman for dummies, " + "\\u00A9" + " Holland LLC.");
        }//end of hangman

    }//End of Hangman class
