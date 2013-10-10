<div class="container">
<h1>Review for book: ${name}</h1>
    <form action="submitReview.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${not empty values.from}">
            <input type="hidden" name="from" value="${values.from}">
        </c:if>
        <div>
            <input type="hidden" name="isbn" value="${isbn}">
            <div><label for="title">Title: </label></div>
            <textarea id="title" name="title" rows="1" cols="20"></textarea>
        </div>
        <div>
            <div><label for="text">Review:  </label></div>
            <textarea id="text" name="text" rows="5" cols="20"></textarea>
        </div>
        <div><input type="submit" value="Submit" /></div>
    </form>
</div>