package hw1;

import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Pattern;

public class Tree {
    Stack<Character> characters;
    Stack<Node> nodes;
    Node root = null;
    StringBuilder dot = new StringBuilder("graph ArithmeticExpressionTree {\n");    //Dot File

    public Tree(String infix){
        //Create Character Stack
        characters = new Stack<>();

        //Create a Node Stack
        nodes = new Stack<>();

        root = build(infix);

    }
    private Node build(String infix){
        String floats = "";

        //Hash Map (Trying to Implement a Dictionary like Data Structure)
        HashMap<Character, Integer> priority = new HashMap<Character, Integer>();
        priority.put(')', 0);
        priority.put('+', 1);
        priority.put('-', 1);
        priority.put('*', 2);
        priority.put('x', 2);
        priority.put('/', 2);
        priority.put('^', 2);

        //A Modified Implementation of the 2 Stack Infix to Postfix Algorithm to Prioritize Operations
        int i = 0;
        while (i < infix.length()) {
            if (infix.charAt(i) == '(') {
                characters.push(infix.charAt(i));
            }
            else if (Character.isDigit(infix.charAt(i))) {
                //Parse Floating Point Digits
                //While -+/* is Not Encountered Build a Float as a String
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')){
                    floats += infix.charAt(i);
                    i++;
                }
                //Create Node and Push On Node Stack
                nodes.push(new Node(floats));
                floats = "";

                //Continue After Float
                i--;
            }
            else if (priority.get(infix.charAt(i)) > 0){
                //Priority Counting
                while (!characters.isEmpty() && characters.peek() != '('
                        && ((infix.charAt(i) != '^' && priority.get(characters.peek()) >= priority.get(infix.charAt(i)))
                            || (infix.charAt(i) == '^' && priority.get(characters.peek()) > priority.get(infix.charAt(i))))){

                    //Create New Root
                    Node parent = new Node(characters.peek().toString());
                    characters.pop();


                    //Right Child
                    Node right = nodes.peek();
                    nodes.pop();

                    //Left Child
                    Node left = nodes.peek();
                    nodes.pop();

                    parent.left = left;
                    parent.right = right;

                    nodes.push(parent);
                }
                characters.push(infix.charAt(i));
            }
            else if(infix.charAt(i) == ')'){
                while (!characters.isEmpty() && characters.peek() != '('){
                    //Create New Root
                    Node parent = new Node(characters.peek().toString());
                    characters.pop();


                    //Right Child
                    Node right = nodes.peek();
                    nodes.pop();

                    //Left Child
                    Node left = nodes.peek();
                    nodes.pop();

                    parent.left = left;
                    parent.right = right;

                    nodes.push(parent);
                }
                characters.pop();
            }
            i++;
        }
        try {
            return nodes.peek();
        }
        catch (Exception exception){
            return null;
        }
    }

    //HW REQUIREMENT
    public String toString(){
        StringBuilder postfix = new StringBuilder();
        postorder(root, postfix);
        postfix.deleteCharAt(0);
        postfix.deleteCharAt(postfix.length() - 1);

        return postfix.toString();
    }

    //Traverse Tree with Post-Order to create a String
    static void postorder(Node root, StringBuilder postfix){
        if (root != null){
            postfix.append("(");
            postorder(root.left, postfix);
            postorder(root.right, postfix);
            postfix.append(root.data + ")");
        }
    }


    //Traverse Tree with Pre-Order Traversal and Append to Dot File
    private StringBuilder preorder(Node root) {
        dot.append("\t" + root.hashCode());
        dot.append(String.format(" [label=\"%s\"]\n", root.data));

        //Try to Go Left
        if(root.left != null){
            dot.append(String.format("\t%s -- %s\n", root.hashCode(), root.left.hashCode()));
            preorder(root.left);
        }

        //Try to Go Right
        if(root.right != null){
            dot.append(String.format("\t%s -- %s\n", root.hashCode(), root.right.hashCode()));
            preorder(root.right);
        }

        return dot;
    }
    //HW REQUIREMENT
    //Create Dot File with Preorder Traversal
    String toDotString(){
        return preorder(root).toString() + "}";
    }

    //Traverse Using Pre-Order and Calculate Result
    double preorderCalculator(Node root){
        if(Pattern.compile("\\+?|-?|/?|\\*?|x?|\\^").matcher(root.data).find()){
            switch (root.data.charAt(0)){
                case '+': {
                    return preorderCalculator(root.left) + preorderCalculator(root.right);
                }
                case '-': {
                    return preorderCalculator(root.left) - preorderCalculator(root.right);
                }
                case '*':
                case 'x': {
                    return preorderCalculator(root.left) * preorderCalculator(root.right);
                }
                case '/': {
                    return preorderCalculator(root.left) / preorderCalculator(root.right);
                }
                case '^': {
                    return Math.pow(preorderCalculator(root.left), preorderCalculator(root.right));
                }
            }
        }
        return Double.parseDouble(root.data);
    }
    //HW REQUIREMENT
    double result(){
        return preorderCalculator(root);
    }
}
