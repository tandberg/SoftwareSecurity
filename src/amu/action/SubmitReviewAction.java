package amu.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookDAO;
import amu.database.ReviewDAO;
import amu.model.Book;
import amu.model.Cart;
import amu.model.Customer;

public class SubmitReviewAction implements Action {
	
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
      
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
			actionResponse.addParameter("from", "addReviewToBook");
			return actionResponse;
		}
        
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String text = request.getParameter("text");
        if (isbn != null && title != null && text != null){
            
            BookDAO bookDAO = new BookDAO();
            Book book = bookDAO.findByISBN(request.getParameter("isbn"));
            ReviewDAO reviewDAO = new ReviewDAO();
            
            reviewDAO.addReview(text, title, book.getId(), customer.getId());
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
            actionResponse.addParameter("isbn", isbn);
            request.setAttribute("isbn", isbn);
            return actionResponse;
        }

        

        return new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
    }

}
