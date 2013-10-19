package amu.action;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookDAO;
import amu.database.PersonalBookListDAO;
import amu.database.ReviewDAO;
import amu.model.Book;
import amu.model.Customer;
import amu.model.PersonalBookList;

public class AddNewPersonalBooklist implements Action {
	
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

        PersonalBookListDAO personalBookListDAO = new PersonalBookListDAO();
        String title = request.getParameter("title");
        String description = request.getParameter("text");
        if(title == null){
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            return actionResponse;
        }
        if(description == null){
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
            return actionResponse;
        }
        if(book != null){
        	PersonalBookList personalBookList = new PersonalBookList(customer, Calendar.getInstance(), title, description);
        	personalBookList.addBook(book);
        	personalBookListDAO.addNewListWithBooks(personalBookList, customer);
        }
        else{
        	PersonalBookList personalBookList = new PersonalBookList(customer, Calendar.getInstance(), title, description);
        	personalBookListDAO.addNewListWithNoBooks(personalBookList, customer);
        }
		
		return new ActionResponse(ActionResponseType.REDIRECT, "viewUserBooklists");
	}
}
