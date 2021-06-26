package hw1;

import java.util.Stack;

public class Calculator {
    static char [] operatorSet = {'+', '-', '/', '*', 'x', '^'};
    static char [] characterSet = {'0', '1', '2', '3', '4','5','6','7','8','9', '.', '(', ')'};
    Tree tree = null;
    //Parameter: Infix String
    public Calculator(String infix){

        //Create a Validated String
        if(infix == ""){
            //Should Throw Exception
            return;
        }
        if((infix = validateModifyString(infix)) == null){
            //Error Occurred
            //Should Throw Exception
            return;
        }

        //Implement Binary Tree
        tree = new Tree(infix);
    }

    //Remove White Spaces and Tabs, Extent String When Necessary, Validate Parentheses and Characters, Validate Characters
    //Return a Reference to the String on Success
    //Return NULL on Error
    private String validateModifyString(String infix){
        //Create a Stack to Store the Expansion Characters
        Stack<Character> expansionStack = new Stack<Character>();

        //Create Stack to Count Parentheses
        Stack<Character> parenthesesStack = new Stack<Character>();

        //Create Expansion String Based on the Expansion Instance
        String expansionString = "", expansionInstance = "";

        //Remove All Whitespaces from Infix
        infix = infix.replaceAll(" ", "");

        //Remove All Tabs from Infix
        infix = infix.replaceAll("\t", "");

        if(infix.charAt(infix.length() - 1) == '\\' || infix.charAt(infix.length() - 2) == '\\'){
            System.out.println("[ERROR] Invalid expansion expression");
            return null;
        }
        //Extent Operation When '\<OPERATOR><INTEGER>' is Present
        for(int i = 0; i < infix.length() - 2; i++){
            if (infix.charAt(i) == '\\') {
                for(int j = i + 2; j < infix.length() && !validOperator(infix.charAt(j)); j++){
                    // For Input "\?<DOUBLE>" Creates an Error
                    if(infix.charAt(j) == '.'){
                        System.out.println("[ERROR] Invalid expansion expression");
                        return null;
                    }
                }
                //Check Characters after '\'
                if (validOperator(infix.charAt(i + 1)) && Character.isDigit(infix.charAt(i + 2))) {
                    //Extent Here
                    for(int j = i - 1; j > -1; j--){
                        //Parentheses Count using a Stack, this could also be done with an integer counter but with a
                        //stack more complexity can be added later, for instance [] and {} can be added to the
                        //equation
                        if(infix.charAt(j) == ')'){
                            parenthesesStack.push(infix.charAt(j));
                        }
                        else if(parenthesesStack.isEmpty()) {
                            //Implement Higher Priority for '\' Operations using an Opening '(' and Increment i
                            infix = insertString(infix, "(", j); //High Priority
                            i++;
                            break;
                        }
                        //End of Parentheses Count
                        else if(infix.charAt(j) == '('){
                            parenthesesStack.pop();
                        }
                        expansionStack.push(infix.charAt(j));
                    }

                    //Instance of the Expansion String
                    //Example: For (2 + 3)\4 the Expansion Instance is (2+3)
                    expansionInstance += infix.charAt(i+1);
                    while (!expansionStack.isEmpty()){
                        expansionInstance += expansionStack.pop();
                    }

                    //Number of Times that the Expansion Instance Occurs
                    for (int expansions = 0; expansions < Integer.parseInt(Character.toString(infix.charAt(i+2))) - 1; expansions++){
                        expansionString += expansionInstance;
                    }

                    //Find Substring to Replace (\?X must be replaced with N times the expansion string)
                    String regex = "\\\\";
                    regex += Character.toString(infix.charAt(i)) + Character.toString(infix.charAt(i + 1)) + infix.charAt(i + 2);
                    //Infix Replace "\?X" with Expansion String (N-1 * expansion instance)
                    infix = infix.replaceFirst(regex, expansionString + ")");

                    //Reset
                    expansionString = "";
                    expansionInstance = "";
                    i = i + 2;

                    continue;
                } else {
                    System.out.println("[ERROR] Invalid expansion expression");
                    return null;
                }
            }
        }

        //Validate Consecutive Characters
        if(consecutiveCharacters(infix)){
            return null;
        }

        //Validate Opening and Closing Parentheses
        if(!validParentheses(infix)){
            return null;
        }

        //Validate Characters
        if(characterCheck(infix) >= 0){
            return null;
        }

        //Add two Parentheses for Tree Implementation
        infix =  "(" + infix + ")";
        return infix;
    }

    //Search for Character in the Operators Array
    //Return True on Success
    //Return False on Error
    private boolean validOperator(char character){
        for(Character i : operatorSet){
            if(i == character)
                return true;
        }
        return false;
    }
    //Find Valid Parentheses with the Help of a Stack
    //Return True on Success
    //Return False on Error
    private boolean validParentheses(String infix){
        //Create a CharacterStack
        Stack<Character> stack = new Stack<>();

        //Push when Opening
        //Pop when Closing
        char character;
        for(int i = 0; i < infix.length(); i++){
            character = infix.charAt(i);
            if(character == '('){
                stack.push(character);
            }
            else if(character == ')'){
                try {
                    stack.pop();
                }
                catch (Exception e) {
                    System.out.println("[ERROR] Closing unopened parenthesis");
                    return false;
                }
            }
        }
        if(stack.size() > 0){
            System.out.println("[ERROR] Not closing opened parenthesis");
            return false;
        }
        return true;
    }
    //Validate Characters in Operators and Character Sets Accordingly
    //Return -1 on Success
    //Return the Index of the Non-Valid Character on Error
    private int characterCheck(String infix){
        boolean found;
        for(int i = 0; i < infix.length(); i++){
            found = false;

            //Search In Operator Set
            for(Character j : operatorSet){
                if(infix.charAt(i) == j){
                    found = true;
                    break;
                }
            }
            //Search In Character Set
            if(!found){
                for(Character j : characterSet){
                    if(infix.charAt(i) == j){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    System.out.println("[ERROR] Invalid character");
                    return i;
                }
            }
        }
        return -1;
    }
    //Validate Consecutive Characters with validOperator()
    //Return True If Error Exists
    //Return False If No Errors Exist
    private boolean consecutiveCharacters(String infix){
        for(int i = 0; i < infix.length() - 1; i++){
            if(validOperator(infix.charAt(i)) && validOperator(infix.charAt(i + 1))){
                System.out.println("[ERROR] Two consecutive operands");
                return true;
            }
            else if(validOperator(infix.charAt(i)) && infix.charAt(i + 1) == ')'){
                System.out.println("[ERROR] Operand appears before closing parenthesis");
                return true;
            }
            else if(infix.charAt(i) == '(' && validOperator(infix.charAt(i + 1))){
                System.out.println("[ERROR] Operand appears after opening parenthesis");
                return true;
            }
        }
        return false;
    }
     String insertString(String infix, String insert, int index){
        // Create a new string
        String string = infix.substring(0, index + 1)
                + insert
                + infix.substring(index + 1);

        // return the modified String
        return string;
    }
}
