import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter parentheses: ");
        String input = scanner.nextLine();
        
        Paranthesis phar = new Paranthesis(input);
        boolean pharOutput = phar.getAnswere();
        System.out.println(pharOutput);

        scanner.close();
    }
}