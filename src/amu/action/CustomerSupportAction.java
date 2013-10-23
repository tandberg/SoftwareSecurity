package amu.action;

import amu.Mailer;
import amu.model.Customer;
import amu.model.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

class CustomerSupportAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "customerSupport");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {
			
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
        		ErrorMessage error = new ErrorMessage("Denied", "Are you an robot?");
        		request.setAttribute("errorMessage", error);
                return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");	        }
        	
        	if(request.getParameter("department") == null || 
                    request.getParameter("subject") == null || 
                    request.getParameter("content") == null ||
                    request.getParameter("fromAddr") == null ||
                    request.getParameter("fromName") == null){
        		
        		ErrorMessage error = new ErrorMessage("Denied", "What are you up to? :(");
        		request.setAttribute("errorMessage", error);
                return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        	}
            Mailer.send(request.getParameter("department"), 
                    request.getParameter("subject"), 
                    request.getParameter("content"), 
                    request.getParameter("fromAddr"), 
                    request.getParameter("fromName"));
            // TODO: Send receipt to customer
            return new ActionResponse(ActionResponseType.REDIRECT, "customerSupportSuccessful");
        } 

        return new ActionResponse(ActionResponseType.FORWARD, "customerSupport");
    }
}
