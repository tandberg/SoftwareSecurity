package amu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.CustomerDAO;
import amu.database.ReviewDAO;
import amu.model.Customer;
import amu.model.Review;

public class RateBookReviewAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        String isbn = request.getParameter("isbn");
        System.out.println(isbn);

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "rateBookView");
            return actionResponse;
        }
        String liked = request.getParameter("liked");
        if(liked == null){
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "rateBookView");
            return actionResponse;
        }
        int reviewid = Integer.parseInt(request.getParameter("reviewid"));
        ReviewDAO reviewDAO = new ReviewDAO();
        if(liked.equals("yes")){
        	reviewDAO.updateLikes(reviewid);
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
            actionResponse.addParameter("isbn", isbn);
            return actionResponse;
        	
        }
        else if(liked.equals("no")){
        	reviewDAO.updateDislikes(reviewid);
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
            actionResponse.addParameter("isbn", isbn);
            return actionResponse;
        }
        
        
        // (request.getMethod().equals("GET")) 
        return new ActionResponse(ActionResponseType.FORWARD, "changeEmail");
    }
}
