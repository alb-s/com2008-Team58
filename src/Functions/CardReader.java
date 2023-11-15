package Functions;
import java.util.*;

public class CardReader{

        enum CardType {
        MASTERCARD,
        VISA,
        AMERICANEXPRESS
        }       
	public void checkCard(){

        try (Scanner inputString = new Scanner(System.in)) {
            String cardReader = inputString.nextLine();

            if (isValidCardNumber(cardReader)){
                 CardType cardType = identifyCardType(cardReader);
                
                if (cardType != null){
                    System.out.println("Card Type: " + cardType);
                } 
                else{
                    System.out.println("Unable to identify card type");
                }
            } 
            else{
                System.out.println("Invalid credit card number");
            }
        }

    }

    private static boolean isValidCardNumber(String cardNumber){
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--){
            int digit = Character.getNumericValue(cardNumber.charAt(i));
        
            if (alternate){
                digit *= 2;
                if (digit > 9) {
                    digit = digit % 10 + 1;
                }
            }
        
            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    // Identify the card type based on the first digit(s)
    private static CardType identifyCardType(String cardNumber) {

        String firstChar = cardNumber.substring(0, 1);
        if (firstChar.equals("4")){
            return CardType.VISA;
        } 
        else if (firstChar.equals("5")){
            return CardType.MASTERCARD;
        } 
        else if (firstChar.equals("3")){
            // Note: American Express typically starts with 34 or 37
            if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
                return CardType.AMERICANEXPRESS;
            }
        }
        return null; // Unable to identify card type
    }
}