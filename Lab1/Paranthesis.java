public class Paranthesis {
    private String message;

    Paranthesis(String message){
        this.message = message;
    }

    public boolean getAnswere(){
        boolean boolResponse = false; 
        String response = "The action was done unsucsesfull";

        Logger logger = new Logger("Parentheses");

        int leftOpenCounter = 0; // )
        int rightOpenCounter = 0; // (

        int msgLength = message.length();
        for(int i = 0 ; i < msgLength ; i++){
            if(message.charAt(i) == '('){
                rightOpenCounter += 1;
            }
            else if(message.charAt(i) == ')'){
                leftOpenCounter += 1;

                // Check if there are equal left and right brackets
                if(leftOpenCounter > rightOpenCounter){
                    response = "Invalid Imput, there are more ) then ( at the " + String.valueOf(i+1) + "charcher";
                    logger.log( LogLevel.ERROR, response);
                    return false;
                }
            }
            else{
                logger.log(LogLevel.ERROR, "There is an invalid character");
                return false;
            }
        }
        if(rightOpenCounter == leftOpenCounter){
            boolResponse = true;
            response = "The action was done succsefull";
            logger.log(LogLevel.INFO, response);
        }
        if(rightOpenCounter > leftOpenCounter){
            response = "There are more ( then )";
            logger.log(LogLevel.ERROR, response);
        }

        return boolResponse;
    }
}
