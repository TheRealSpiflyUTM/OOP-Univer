import java.util.Scanner;

import Logger.LogLevel;
import Logger.Logger;

public class Main {
    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);
        // System.out.print("Log value: TRACE, INFO , WARN , ERROR | Dafault: TRACE");
        // String lvlInput = scanner.nextLine().toUpperCase().trim();
        // LogLevel selLevel = LogLevel.TRACE;
        
        // if(!lvlInput.isEmpty()){
        //     try{
        //         selLevel = LogLevel.valueOf(lvlInput);
        //     }catch (Error e){
        //         System.out.println("Invalid level, using TRACE");
        //     }
        // }

        // Logger.setLevel(selLevel);

        // System.out.print("Enter parentheses: ");
        // String input = scanner.nextLine();  
        
        // scanner.close();
        Logger logger = new Logger("Sempai");
        logger.log(LogLevel.INFO, "Petuhul {petrea} {0} este {1}", new Object[]{"Petrea" , 67});
    }
}