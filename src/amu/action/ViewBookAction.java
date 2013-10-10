package amu.action;

import amu.database.BookDAO;
import amu.database.ReviewDAO;
import amu.model.Book;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ViewBookAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(request.getParameter("isbn"));
        ReviewDAO reviewDAO = new ReviewDAO();
        if (book != null) {
        	request.setAttribute("reviews", reviewDAO.findReviewByBookID(book.getId()));
            request.setAttribute("book", book);
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewBook");
    }
}
