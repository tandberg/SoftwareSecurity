package amu.action;

import amu.database.AddressDAO;
import amu.model.Address;
import amu.model.Customer;
import amu.model.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class EditAddressAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        
        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }

        AddressDAO addressDAO = new AddressDAO();
        int adressID = -1;
        try {
            adressID = Integer.parseInt(request.getParameter("id"));

		} catch (NumberFormatException e) {
        	ErrorMessage error = new ErrorMessage("403 Forbidden", "Something went wrong");
        	request.setAttribute("errorMessage", error);
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");		
        }
        if(adressID <= 0){
        	ErrorMessage error = new ErrorMessage("403 Forbidden", "Something went wrong");
        	request.setAttribute("errorMessage", error);
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        }
        Address address = addressDAO.read(adressID);
        
        if(address == null || !addressDAO.checkAdressAccess(customer.getId(), address.getId())){
        	ErrorMessage error = new ErrorMessage("403 Forbidden", "You are not authorized to access this adress");
        	request.setAttribute("errorMessage", error);
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        }

        if (request.getMethod().equals("POST")) {
            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);

            address.setAddress(request.getParameter("address"));
            
            if (addressDAO.edit(address)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            }

            messages.add("An error occured.");
        }

        // (request.getMethod().equals("GET")) 
        request.setAttribute("address", address);
        return new ActionResponse(ActionResponseType.FORWARD, "editAddress");
    }

}
