package amu;

import amu.action.Action;
import amu.action.ActionFactory;
import amu.action.ActionResponse;
import amu.action.ActionResponseType;
import amu.model.ErrorMessage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {				

			Action action = ActionFactory.getAction(request);

			//If a user types in a page that does not exist.


			ActionResponse actionResponse = action.execute(request, response);

			if (actionResponse.getType() == ActionResponseType.REDIRECT) {
				response.sendRedirect(actionResponse.getURL() + actionResponse.getParameterString());
			} else { // actionResponse.getType() == ActionResponse.Type.FORWARD
				request.getRequestDispatcher(actionResponse.getURL()).forward(request, response);
			}


		} catch (Exception e) {
            throw new ServletException("Executing action failed.", e);

		}
	}
}
