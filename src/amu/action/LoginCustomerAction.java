package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;
import amu.security.BCrypt;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import com.sun.xml.rpc.util.Constants;
import com.sun.xml.ws.runtime.dev.Session;

class LoginCustomerAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> values = new HashMap<String, String>();
		request.setAttribute("values", values);
		if (ActionFactory.hasKey(request.getParameter("from"))) {
			values.put("from", request.getParameter("from"));
		}

		if (request.getMethod().equals("POST")) {

			Map<String, String> messages = new HashMap<String, String>();
			request.setAttribute("messages", messages);

			CustomerDAO customerDAO = new CustomerDAO();
			Customer customer = customerDAO.findByEmail(request.getParameter("email"));

			if (customer != null) {
				values.put("email", request.getParameter("email"));

				if (customer.getActivationToken() == null) {
					if (BCrypt.checkpw(request.getParameter("password"), customer.getPassword())){							//customer.getPassword().equals(CustomerDAO.hashPassword(request.getParameter("password")))) {

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
				        	return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
				        }

						HttpSession session = request.getSession(true);
						session.invalidate();
						session = request.getSession();
						session.setAttribute("customer", customer);
						if (ActionFactory.hasKey(request.getParameter("from"))) {
							return new ActionResponse(ActionResponseType.REDIRECT, request.getParameter("from"));
						}
					} else { // Wrong password
						messages.put("emailOrpassword", "Wrong email or password.");
					}
				} else { // customer.getActivationToken() != null
					return new ActionResponse(ActionResponseType.REDIRECT, "activateCustomer");
				}
			} else { // findByEmail returned null -> no customer with that email exists
				messages.put("emailOrpassword", "Wrong email or password.");
			}

			// Forward to login form with error messages
			return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
		}
        
		return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
	}
}
