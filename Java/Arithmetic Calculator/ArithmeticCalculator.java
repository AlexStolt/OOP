package hw1;

import java.util.regex.Pattern;

public class ArithmeticCalculator {
    public static void main(String[] args) {
        //Scan a String From STDIO
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Expression: ");

        String infix = scanner.nextLine();
        Calculator calculator = new Calculator(infix);

        //Validate Arguments
        String arguments = scanner.nextLine();
        //Input must be like -<PARAMETER> with Optional Length
        if(!(Pattern.compile("-s ?").matcher(arguments).find() || Pattern.compile("-d ?").matcher(arguments).find() || Pattern.compile("-c ?").matcher(arguments).find())) {
            return;
        }

        //Remove '-' and ' '
        arguments = arguments.replaceAll("-", "");
        arguments = arguments.replaceAll(" ", "");

        //Implementation of a Menu
        if(calculator.tree != null && calculator.tree.root != null) {
            for (int i = 0; i < arguments.length(); i++) {
                switch (arguments.charAt(i)) {
                    case 'd': {
                        System.out.println(calculator.tree.toDotString());
                        break;
                    }
                    case 's': {
                        System.out.println("Postfix: " + calculator.tree.toString());
                        break;
                    }
                    case 'c': {
                        System.out.println("Result: " + calculator.tree.result());
                        break;
                    }
                    default: {
                        break;
                    }
                }

            }
        }
    }
}
