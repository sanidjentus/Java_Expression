import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static double operator(double x, double y, char ch) {
        return switch (ch) {
            case '+' -> x + y;
            case '-' -> y - x;
            case '*' -> x * y;
            case '/' -> y / x;
            case '^' -> Math.pow(y, x);
            default -> -1;
        };
    }

    public static int priority(char ch){
        return switch (ch){
            case '(', ')' -> 1;
            case '+', '-' -> 2;
            case '*', '/', '^' -> 3;
            default -> -1;
        };
    }

    public static String postfixNote(String expr){
        Stack<Character> ch = new Stack<>();
        StringBuilder postfixNote = new StringBuilder();
        for (char symb : expr.toCharArray()) {
            if ((symb >= '0' && symb <= '9') || symb == ' ') {
                if (symb != ' ')
                    postfixNote.append(symb);
            }
            else {
                postfixNote.append(" ");
                if (priority(symb) == -1) return("err"); //Если ввели не число и не оператор
                else {
                    if (ch.isEmpty() || priority(symb) > priority(ch.peek())) ch.push(symb);
                    else {
                        postfixNote.append(" ");
                        switch (symb) {
                            case '(' -> ch.push(symb);
                            case ')' -> {
                                while (ch.peek() != '(') {
                                    postfixNote.append(ch.peek().toString());
                                    ch.pop();
                                    if (ch.isEmpty()) return ("err");
                                }
                                ch.pop();
                            }
                            default -> {
                                while (!ch.isEmpty() && priority(ch.peek()) >= priority(symb)) {
                                    postfixNote.append(ch.peek().toString());
                                    ch.pop();
                                }
                                ch.push(symb);
                            }
                        }
                        postfixNote.append(" ");
                    }
                }
            }
        }
        postfixNote.append(" ");
        while (!ch.isEmpty()) {
            postfixNote.append(ch.peek().toString());
            ch.pop();
        }
        return postfixNote.toString();
    }

    public static void calculate(String expr){
        Stack<Double> nums = new Stack<>();
        double tmp = 0;
        boolean flag = false;
        for (char symb : expr.toCharArray()) {
            if (symb >= '0' && symb <= '9') {
                tmp = tmp * 10 + Character.getNumericValue(symb);
                flag = true;
            } else {
                if (symb == ' ') {
                    if (flag) {
                        nums.push(tmp);
                        tmp = 0;
                        flag = false;
                    }
                } else {
                    if (nums.isEmpty()) {
                        System.out.println("Incorrect input");
                        return;
                    }
                    double tmp1 = nums.peek();
                    nums.pop();
                    if (nums.isEmpty()) {
                        System.out.println("Incorrect input");
                        return;
                    }
                    double tmp2 = nums.peek();
                    nums.pop();
                    nums.push(operator(tmp1, tmp2, symb));
                }
            }
        }
        double answer = nums.peek();
        nums.pop();
        if (!nums.isEmpty()) {
            System.out.println("Incorrect input");
            return;
        }
        System.out.println(answer);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String expr = scan.nextLine();
        String postfx = postfixNote(expr);

        System.out.println(postfx);
        if (postfx.equals("err"))
            System.out.println("Incorrect input");
        else {
            calculate(postfx);
        }
    }
}
