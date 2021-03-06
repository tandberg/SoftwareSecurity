package amu.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.AddressDAO;
import amu.database.OrderDAO;
import amu.model.Address;
import amu.model.Cart;
import amu.model.Customer;
import amu.model.ErrorMessage;
import amu.model.Order;

public class EditOrderAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "editOrder");
            return actionResponse;
        }
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        OrderDAO orderDao = new OrderDAO();
        Order order = orderDao.getSingleOrderByID(orderId, customer);
        
        if(order == null || !orderDao.checkOrderAccess(customer.getId(), order.getId())){
        	System.out.println(customer.getId());
        	System.out.println(order.getId());
        	ErrorMessage error = new ErrorMessage("403 Forbidden", "You are not authorized to access this order");
        	request.setAttribute("errorMessage", error);
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        }
        
        Cart cart = orderDao.getOrderCartItems(orderId);        
        session.setAttribute("cart", cart);
        cart.setLastOrder(order);
        return new ActionResponse(ActionResponseType.FORWARD, "viewCart");
    }
}
