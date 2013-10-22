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
		} else {      
			char c;  
			int digitCount = 0;  
			int upperCaseCount = 0;
			int lowerCaseCount = 0;
			for (int i = 0; i < password.length(); i++) {  
				c = password.charAt(i);  
				if (!Character.isLetterOrDigit(c)) {          
					return false;  
				} else if (Character.isDigit(c)) {  
					digitCount++;     
				}
				if(Character.isLowerCase(c)){
					lowerCaseCount++;
				}
				else{
					upperCaseCount++;
				}


			}
			
			if (digitCount < 2 || lowerCaseCount == 0 || upperCaseCount == 0)   {     
				return false;  
			}  
		}  
		return true;  
	}  


}
