package util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionHandler {

    public void handleException(Exception e) {
        System.err.println("Faced with unprocessable exception: ");
        System.err.println("Name = " + e.getClass().getName());
        System.err.println("Message = " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}
