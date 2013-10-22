<div class="container">
	<h1>Login</h1>
    <c:choose>
        <c:when test="${empty customer}">
            <div class="general-form">
                <form action="loginCustomer.do" method="post">
                    <c:if test="${not empty values.from}">
                        <input type="hidden" name="from" value="${values.from}">
                    </c:if>
                    <table class="general-table">
                        <tr>
                            <td>
                                <label for="email">Email</label>
                            </td>
                            <td>
                                <input id="email" name="email" type="text" value="${values.email}" />
                            </td>
                            <c:if test="${not empty messages.emailOrpassword}">
                                <td><span class="error">${messages.emailOrpassword}</span></td>
                            </c:if>
                        </tr>
                        <tr>
                            <td><label for="password">Password</label></td>
                            <td><input id="password" name="password" type="text" autocomplete="off" /></td>
                        </tr>
						<tr>
						<td>
							<label>ReCaptcha</label>
						</td>
						<td>
							<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
							<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
							<%
								ReCaptcha c = ReCaptchaFactory.newReCaptcha(
												"6Le0JOkSAAAAADjDO9CQncwf72xoSVUF_gvoryI4", "6Le0JOkSAAAAAOz6HWJBdfLjj-0iuI0qrovO4DA5", false);
										out.print(c.createRecaptchaHtml(null, null));
							%>
							</td>
						</tr>
					</table>
                    <div>
                        <input type="submit" value="Submit">
                    </div>

                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                Login successful!
            </div>
        </c:otherwise>
    </c:choose>
</div>