package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;
import amu.model.ErrorMessage;
import amu.security.BCrypt;
import amu.security.InputControl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


class ChangePasswordAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "changePassword");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {
            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);
            
        	if(!InputControl.isValidPassword(request.getParameterValues("password")[1])){
         		ErrorMessage error = new ErrorMessage("Denied", "The length of your new password needs to be more than 8.");
        		request.setAttribute("errorMessage", error);
                return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");            	
            }

            String[] password = request.getParameterValues("password");

            if (BCrypt.checkpw(password[0], customer.getPassword())) {
                messages.add("Old password did not match. Please try again.");
                return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }
            
            // Validate that new email is typed in the same both times
            else if (password[1].equals(password[2]) == false) {
                messages.add("Password and repeated password did not match. Please try again.");
                return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }

            // Validation OK, do business logic
            CustomerDAO customerDAO = new CustomerDAO();
            customer.setPassword(CustomerDAO.hashPassword(password[1]));
            if (customerDAO.edit(customer) == false) {
                messages.add("An error occured.");
                return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }
            
            // Email change successful, return to viewCustomer
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");

        } 
        
        // (request.getMethod().equals("GET")) 
        return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
    }
}
