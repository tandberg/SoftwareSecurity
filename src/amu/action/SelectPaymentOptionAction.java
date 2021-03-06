package amu.action;

import amu.database.AddressDAO;
import amu.database.CreditCardDAO;
import amu.model.Address;
import amu.model.Cart;
import amu.model.CreditCard;
import amu.model.Customer;
import amu.model.ErrorMessage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class SelectPaymentOptionAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Customer customer = (Customer) session.getAttribute("customer");

        if (cart == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
        }

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "selectPaymentOption");
            return actionResponse;
        }
        
        if (cart.getShippingAddress() == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "selectShippingAddress");
        }

        CreditCardDAO creditCardDAO = new CreditCardDAO();
        
        // Handle credit card selection submission
        if (request.getMethod().equals("POST")) {
        	if(request.getParameter("creditCardID") == null){
        		ErrorMessage error = new ErrorMessage("Denied", "Something is wrong");
        		request.setAttribute("errorMessage", error);
                return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        	}
        	try {
				Integer.parseInt(request.getParameter("creditCardID"));
			} catch (NumberFormatException e) {
        		ErrorMessage error = new ErrorMessage("Denied", "Ups, this id is not a number");
        		request.setAttribute("errorMessage", error);
                return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            }
            cart.setCreditCard(creditCardDAO.read(Integer.parseInt(request.getParameter("creditCardID"))));
            return new ActionResponse(ActionResponseType.REDIRECT, "reviewOrder");
        }
        
        List<CreditCard> creditCards = creditCardDAO.browse(customer);
        request.setAttribute("creditCards", creditCards);

        // Else GET request
        return new ActionResponse(ActionResponseType.FORWARD, "selectPaymentOption");
    }

}
