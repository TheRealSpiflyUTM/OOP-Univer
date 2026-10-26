import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Log value: TRACE, INFO , WARN , ERROR | Dafault: TRACE");
        String lvlInput = scanner.nextLine().toUpperCase().trim();
        LogLevel selLevel = LogLevel.TRACE;
        
        if(!lvlInput.isEmpty()){
            try{
                selLevel = LogLevel.valueOf(lvlInput);
            }catch (Error e){
                System.out.println("Invalid level, using TRACE");
            }
        }

        Logger.setLevel(selLevel);

        System.out.print("Enter parentheses: ");
        String input = scanner.nextLine();
        
        Paranthesis phar = new Paranthesis(input);
        boolean pharOutput = phar.getAnswere();
        System.out.println(pharOutput);

        scanner.close();
    }
}