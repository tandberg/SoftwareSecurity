package amu.action;

import amu.database.CreditCardDAO;
import amu.model.CreditCard;
import amu.model.Customer;
import amu.model.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class DeleteCreditCardAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }

        CreditCardDAO creditCardDAO = new CreditCardDAO();
        CreditCard creditCard;
        
        int creditCardID = -1;
        try {
            creditCardID = Integer.parseInt(request.getParameter("id"));

		} catch (NumberFormatException e) {
        	ErrorMessage error = new ErrorMessage("403 Forbidden", "Something went wrong");
        	request.setAttribute("errorMessage", error);
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");		
        }
        if(creditCardID <= 0){
        	ErrorMessage error = new ErrorMessage("403 Forbidden", "Something went wrong");
        	request.setAttribute("errorMessage", error);
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        }
        creditCard = creditCardDAO.read(creditCardID);
        
        if(creditCard == null || !creditCardDAO.checkCreditCardAccess(customer.getId(), creditCard.getId())){
        	ErrorMessage error = new ErrorMessage("403 Forbidden", "You are not authorized to access this adress");
        	request.setAttribute("errorMessage", error);
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        }

        if (request.getMethod().equals("POST")) {
            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);

            if (creditCardDAO.delete(Integer.parseInt(request.getParameter("id")))) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            }

            messages.add("An error occured.");
        }

        // (request.getMethod().equals("GET")) 
        creditCard = creditCardDAO.read(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("creditCard", creditCard);
        return new ActionResponse(ActionResponseType.FORWARD, "deleteCreditCard");
    }
    
}
