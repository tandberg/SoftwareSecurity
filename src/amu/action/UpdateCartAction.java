package amu.action;

import java.util.Arrays;

import amu.database.BookDAO;
import amu.model.Book;
import amu.model.Cart;
import amu.model.CartItem;
import amu.model.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class UpdateCartAction implements Action {

	private int quantityInt;

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		Cart cart = (Cart) session.getAttribute("cart");

		if (cart == null) {
			cart = new Cart();
			session.setAttribute("cart", cart);
		}

		String[] isbn = request.getParameterValues("isbn");
		String[] quantity = request.getParameterValues("quantity");
		System.out.println(Arrays.toString(quantity));
		if (isbn != null && quantity != null && isbn.length == quantity.length) {

			for (int i = 0; i < isbn.length; i++) {
				CartItem item = cart.getItemByISBN(isbn[i]);
				if (item == null) {
					BookDAO bookDAO = new BookDAO();
					Book book = bookDAO.findByISBN(isbn[i]);
					try {
						quantityInt = Integer.parseInt(request.getParameter("quantity"));
						if(quantityInt>=0)
							cart.addItem(new CartItem(book, quantityInt));
						else
							throw new Exception();
					} catch (Exception e) {
						ErrorMessage error = new ErrorMessage("Illegal Input!!", "Only positive integers allowed!");
						request.setAttribute("errorMessage", error);
						return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
					}
				} else {
					try {
						quantityInt = Integer.parseInt(quantity[i]);
						if(quantityInt >= 0){
							item.setQuantity(quantityInt);
							cart.updateItem(item);
						}
						else
							throw new Exception();

					} catch (Exception e) {
						ErrorMessage error = new ErrorMessage("Illegal Input!!", "Only positive integers allowed!");
						request.setAttribute("errorMessage", error);
						return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
					}
				}
			}
		}

		return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
	}
}
