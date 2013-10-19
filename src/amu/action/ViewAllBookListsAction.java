package amu.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.PersonalBookListDAO;
import amu.model.Cart;

public class ViewAllBookListsAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
        HttpSession session = request.getSession();
        
        PersonalBookListDAO personalBookListDAO = new PersonalBookListDAO();
        
        request.setAttribute("listOfBookLists", personalBookListDAO.getAllLists());
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewAllBooklists");
    }
}
