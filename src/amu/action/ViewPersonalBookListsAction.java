package amu.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookDAO;
import amu.database.PersonalBookListDAO;
import amu.database.ReviewDAO;
import amu.model.Book;
import amu.model.Customer;

public class ViewPersonalBookListsAction implements Action {
	
	

	@Override
	public ActionResponse execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            return actionResponse;
        }
		
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(request.getParameter("isbn"));
        if (book != null) {
        	request.setAttribute("book", book);
        }
        
        PersonalBookListDAO personalBookListDAO = new PersonalBookListDAO();
        request.setAttribute("listOfBookLists",personalBookListDAO.browsePersonalBooklists(customer));
        
        
		
		
        return new ActionResponse(ActionResponseType.FORWARD, "viewPersonalBookList");
	}

}
