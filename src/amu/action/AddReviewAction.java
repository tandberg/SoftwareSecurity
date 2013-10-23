package amu.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookDAO;
import amu.model.Book;
import amu.model.Cart;
import amu.model.CartItem;
import amu.model.Customer;
import amu.model.ErrorMessage;

public class AddReviewAction implements Action {
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null)
        {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
			return actionResponse;
		}
        
        
        if (request.getParameter("isbn") == null ){
    		ErrorMessage error = new ErrorMessage("Denied", "Do not change the isbn please.");
    		request.setAttribute("errorMessage", error);
            return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            
        }
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(request.getParameter("isbn"));
        if(book == null){
    		ErrorMessage error = new ErrorMessage("Denied", "This book do not exist...");
    		request.setAttribute("errorMessage", error);
            return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        }
        ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "addReview");
        request.setAttribute("isbn", book.getIsbn13());
        request.setAttribute("name", book.getTitle().getName());
        return actionResponse;
    }
}
