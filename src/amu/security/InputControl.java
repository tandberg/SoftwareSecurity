package amu.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputControl {

	public static boolean isValidEmail(String email){
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern p = Pattern.compile(EMAIL_PATTERN);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	public static boolean isValidPassword(String password){
		if (password.length() < 8) {   
			return false;  
		}
		return true;  
	}
	public static boolean isValidCreditCardNumber(String creditCardNumber){
		String NUMBERS = "0123456789";
		if(creditCardNumber.length() != 16){
			return false;
		}
		for (int i = 0; i < creditCardNumber.length(); i++) {
			if(!NUMBERS.contains(creditCardNumber.charAt(i) + "")){
				return false;
			}
		}
		return true;
		
	}
	public static boolean isValidName(String name) {
		if(name.length() == 0){
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			if(!Character.isLetter(name.charAt(i))){
				if(!Character.isSpaceChar(name.charAt(i))){
					return false;
				}
			}
		}
		return true; 
	}


}
