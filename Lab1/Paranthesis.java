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
                    Logger.setLevel(LogLevel.ERROR);
                    logger.log(response);
                    return false;
                }
            }
            else{
                Logger.setLevel(LogLevel.ERROR);
                logger.log("There is an invalid character at possition: " + String.valueOf(i) + " " + message.charAt(i) );
                return false;
            }
        }
        if(rightOpenCounter == leftOpenCounter){
            boolResponse = true;
            response = "The action was done succsefull";
            Logger.setLevel(LogLevel.INFO);
            logger.log(response);
        }
        if(rightOpenCounter > leftOpenCounter){
            response = "There are more ( then )";
            Logger.setLevel(LogLevel.ERROR);
            logger.log( response);
        }

        return boolResponse;
    }
}
