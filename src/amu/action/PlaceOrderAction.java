package amu.action;

import amu.database.OrderDAO;
import amu.model.Cart;
import amu.model.Customer;
import amu.model.Order;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class PlaceOrderAction implements Action {

	public PlaceOrderAction() {
	}

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
			return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
		}

		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
			actionResponse.addParameter("from", "placeOrder");
			return actionResponse;
		}
		OrderDAO orderDAO = new OrderDAO();
		Order order = null;
		if(cart.getLastOrder() != null){
			Order lastOrder = cart.getLastOrder();
			lastOrder.setStatus(-1);
			orderDAO.updateStatusOrder(-1, lastOrder.getId());;
			float subTotal = Float.parseFloat(cart.getSubtotal().toString()) - Float.parseFloat(lastOrder.getValue());
			order = new Order(customer, cart.getShippingAddress(), subTotal + "");
			order.setCart(cart);

		}
		else{
			order = new Order(customer, cart.getShippingAddress(), cart.getSubtotal().toString());
			order.setCart(cart);
		}


		if (orderDAO.add(order))
		{
			cart = new Cart();
			session.setAttribute("cart", cart);
			return new ActionResponse(ActionResponseType.REDIRECT, "placeOrderSuccessful");
		} else {
			return new ActionResponse(ActionResponseType.REDIRECT, "placeOrderError");
		}
	}

}
