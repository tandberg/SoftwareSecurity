package amu.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookDAO;
import amu.database.PersonalBookListDAO;
import amu.model.Book;
import amu.model.Customer;

public class ViewSingleBookList implements Action {
	@Override
	public ActionResponse execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
        if (request.getParameter("list_id") == null) {
        	return new ActionResponse(ActionResponseType.FORWARD, "generalErrorMessage");
        }
        int listid = Integer.parseInt(request.getParameter("list_id"));

        
        PersonalBookListDAO personalBookListDAO = new PersonalBookListDAO();
        request.setAttribute("list", personalBookListDAO.findListByID(listid));	
		
        return new ActionResponse(ActionResponseType.FORWARD, "viewBooksFromUserList");
	}

}
