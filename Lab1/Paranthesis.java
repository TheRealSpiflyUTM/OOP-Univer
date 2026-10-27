public class Paranthesis {
    private String message;

    Paranthesis(String message){
        this.message = message;
    }

    public boolean getAnswere(){
        boolean boolResponse = false; 
        Logger logger = new Logger("Parentheses");

        int leftOpenCounter = 0; // )
        int rightOpenCounter = 0; // (

        int msgLength = message.length();
        logger.log(LogLevel.WARN , "Starting the verification message in the pharantheses");

        for(int i = 0 ; i < msgLength ; i++){

            logger.log(LogLevel.TRACE , "The " + i + " itteration");

            if(message.charAt(i) == '('){
                rightOpenCounter += 1;
            }
            else if(message.charAt(i) == ')'){
                leftOpenCounter += 1;

                // Check if there are equal left and right brackets
                logger.log(LogLevel.WARN , "Checking if nr.( > nr.)");
                if(leftOpenCounter > rightOpenCounter){
                    logger.log(LogLevel.ERROR , "Invalid Imput, there are more ) then ( at the " + String.valueOf(i+1) + "charcher");
                    return false;
                }
            }
            else{
                logger.log(LogLevel.ERROR , "There is an invalid character at possition: " + String.valueOf(i) + " " + message.charAt(i) );
                return false;
            }
        }
        if(rightOpenCounter == leftOpenCounter){
            boolResponse = true;
            logger.log(LogLevel.INFO, "The action was done succsefull");
        }
        if(rightOpenCounter > leftOpenCounter){
            logger.log( LogLevel.ERROR , "There are more ( then )");
        }

        return boolResponse;
    }
}
