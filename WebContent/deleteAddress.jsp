<div class="container">
    <h1>Delete Address</h1>
    <div>Do you want to delete the following address?</div>
    <div><c:out value="${address.address}"></c:out></div>
    <form action="deleteAddress.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <input name="id" value="${address.id}" type="hidden" />
        <div><input type="submit" value="Confirm" /></div>
    </form>
</div>