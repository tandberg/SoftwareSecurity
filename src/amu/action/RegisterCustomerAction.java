package amu.action;

import amu.Mailer;
import amu.database.CustomerDAO;
import amu.model.Customer;
import amu.model.ErrorMessage;
import amu.security.InputControl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;


class RegisterCustomerAction extends HttpServlet implements Action {
    
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	
    	
        if (request.getMethod().equals("POST")) {
            
        	//Validate captcha
			String remoteAddr = request.getRemoteAddr();
	        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
	        reCaptcha.setPrivateKey("6Le0JOkSAAAAAOz6HWJBdfLjj-0iuI0qrovO4DA5");
	        String challenge = request.getParameter("recaptcha_challenge_field");
	        String uresponse = request.getParameter("recaptcha_response_field");
	        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
			
	        if (reCaptchaResponse.isValid()) {
	        	//All is well
	        } else {
	        	//There is something rotten in the state of Denmark
	        	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
	        }
        	
        	
        	CustomerDAO customerDAO = new CustomerDAO();
        	if(request.getParameter("email") == null){
	        	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
        	}
            Customer customer = customerDAO.findByEmail(request.getParameter("email"));
            

            if (customer == null) {
            	if(!InputControl.isValidEmail(request.getParameter("email"))){
            		ErrorMessage error = new ErrorMessage("Denied", "Did you write an email?");
            		request.setAttribute("errorMessage", error);
                    return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            	}
            	if(!InputControl.isValidPassword(request.getParameter("password"))){
             		ErrorMessage error = new ErrorMessage("Denied", "The length needs to be more than 8. Your password needs to have 3 or more digits and upper/lower case letters");
            		request.setAttribute("errorMessage", error);
                    return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");            	
                }
                customer = new Customer();
                customer.setEmail(request.getParameter("email"));
                customer.setName(request.getParameter("name"));
                customer.setPassword(CustomerDAO.hashPassword(request.getParameter("password")));
                customer.setActivationToken(CustomerDAO.generateActivationCode());
                customer = customerDAO.register(customer);
                
                ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "activateCustomer");
                actionResponse.addParameter("email", customer.getEmail());
                
                StringBuilder sb = new StringBuilder();
                sb.append("Welcome to Amu-Darya, the really secure bookstore!\n\n");
                sb.append("To activate your account, click <a href='http://");
                sb.append(request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
                sb.append(actionResponse.getURL() + actionResponse.getParameterString());
                sb.append("&activationToken=" + customer.getActivationToken());
                sb.append("'>here</a>, or use this activation code: " + customer.getActivationToken());
               
                Mailer.send(customer.getEmail(), "Activation required", sb.toString());
 
                return actionResponse;
            } else {
                return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
            }
        }
        
        // Else we show the register form
        return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
    }
}
