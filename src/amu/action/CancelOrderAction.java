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

public class CancelOrderAction implements Action {
	
	
	
	public CancelOrderAction(){
	}

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "cancelOrder");
            return actionResponse;
        }
        try {
        	Integer.parseInt(request.getParameter("orderId"));
		} catch (Exception e) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            ErrorMessage error = new ErrorMessage("403 Forbidden", "Ups, is this a number?");
            request.setAttribute("errorMessage", error);
            return actionResponse;		
        }
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        OrderDAO orderDao = new OrderDAO();
        Order order = orderDao.getSingleOrderByID(orderId, customer);
        
        if(!orderDao.checkOrderAccess(customer.getId(), orderId)){
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            ErrorMessage error = new ErrorMessage("403 Forbidden", "You are not allowed to cancel this order");
            request.setAttribute("errorMessage", error);
            return actionResponse;	
        }
        
        if(order == null){
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            ErrorMessage error = new ErrorMessage("404 Not found", "Did not find any order");
            request.setAttribute("errorMessage", error);
            return actionResponse;
        }

        order.setStatus(-1);
        orderDao.updateStatusOrder(order.getStatus(), order.getId());
        Cart cart = orderDao.getOrderCartItems(orderId);        
        if(cart == null){
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            ErrorMessage error = new ErrorMessage("404 Not found", "Did not find any cart");
            
            request.setAttribute("errorMessage", error);
            return actionResponse;
        }
        //Create new order;
        float returnprice = Float.parseFloat(order.getValue()) *(-1);
        Order newOrder = new Order(customer, order.getAddress(), returnprice + "");
        newOrder.setCart(cart);
        newOrder.setStatus(-1);
        orderDao.add(newOrder);
        
        
        //Need to show the new orders on the view customer site.
        List<Order> orders = orderDao.browse(customer);
        request.setAttribute("orders", orders);
        return new ActionResponse(ActionResponseType.FORWARD, "viewCustomer");
    }
}
