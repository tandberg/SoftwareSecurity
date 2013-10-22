package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;
import amu.model.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ActivateCustomerAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// If email field is set, but token is not provided then this is a idempotent request
		if (request.getParameter("email") != null && request.getParameter("activationToken") == null) {
			request.setAttribute("email", request.getParameter("email"));
		}

		// If both fields are set, then this is a non-idempotent activation request
		if (request.getParameter("email") != null && request.getParameter("activationToken") != null) {
			CustomerDAO customerDAO = new CustomerDAO();
			Customer customer = customerDAO.findByEmail(request.getParameter("email"));
			
			/*Check if a user is already activated and create an error response if it is.*/
			if (customer != null && customer.getActivationToken() == null){
				ErrorMessage error = new ErrorMessage("Customer Already Activated", "This customer is already activated, please log in.");
				request.setAttribute("errorMessage", error);
				return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
			}
			
			else if (customer != null && customer.getActivationToken().equals(request.getParameter("activationToken"))) {
				customer = customerDAO.activate(customer);
				return new ActionResponse(ActionResponseType.REDIRECT, "activationSuccessful");
			}
			else {
				HttpSession session = request.getSession(true);
				session.setAttribute("debugActivation", customer);
				return new ActionResponse(ActionResponseType.REDIRECT, "activationError");
			}
		}

		return new ActionResponse(ActionResponseType.FORWARD, "activateCustomer");
	}
}