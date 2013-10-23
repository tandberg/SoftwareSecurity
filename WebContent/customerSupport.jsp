<div class="container">
    <h1>Customer Support</h1>
    <div class="general-form">
        <form method="post" action="customerSupport.do">
            <table class="general-table">
                <tr>
                    <td>Choose department:</td>
                    <td>
                        <select name="department">
                            <option selected value="tdt4237.amu.darya@gmail.com">Sales</option>
                            <option value="tdt4237.amu.darya@gmail.com">Technical support</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Subject</td>
                    <td><input name="subject" type="text"></input></td>
                </tr>
                <tr>
                	<td>Description</td>
                	<td><textarea name="content" rows="10" cols="40"></textarea></td>
                	
                </tr>
                <tr>
                	<td>
                	ReCaptcha
                	</td>
                	<td>
                		<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
						<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%> <%
 						ReCaptcha c = ReCaptchaFactory.newReCaptcha(
 							"6Le0JOkSAAAAADjDO9CQncwf72xoSVUF_gvoryI4",
 							"6Le0JOkSAAAAAOz6HWJBdfLjj-0iuI0qrovO4DA5", false);
 						out.print(c.createRecaptchaHtml(null, null));
 					%>
 					</td>
                </tr>
            </table>
            <div> <input type="submit" value="Send" /></div>
        </form>
    </div>
</div>