package amu.action;

import amu.database.BookDAO;
import amu.model.Book;
import amu.model.Cart;
import amu.model.CartItem;
import amu.model.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class AddToCartAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null)
        {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        
        if (request.getParameter("isbn") != null && request.getParameter("quantity") != null)
        {
            BookDAO bookDAO = new BookDAO();
            Book book = bookDAO.findByISBN(request.getParameter("isbn"));
            try {
            	int quantity = Integer.parseInt(request.getParameter("quantity"));
            	if(quantity>=0)
            		cart.addItem(new CartItem(book, quantity));
            	else{
    	        	throw new Exception();
            	}
			}  catch (Exception e) {
	        	ErrorMessage error = new ErrorMessage("Illegal Input!!", "Only positive integers allowed!");
	        	request.setAttribute("errorMessage", error);
	        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
			}
            
        }

        return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
    }
    
}
