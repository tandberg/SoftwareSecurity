<div class="container">
	<h2>Single user created booklist</h2>
	<br>
	<div>
		<div><b>Title: </b> <c:out value="${list.title}"></c:out> </div>
		<div><b>Description: </b> <c:out value="${list.description}"></c:out> </div>
		<div><b>Creator: </b> <c:out value="${list.customer.name}"></c:out> </div>
		<div><b>Created: </b> <fmt:formatDate value="${list.createdDate.time}" type="date" dateStyle="short"/></div>
		<br>
		<br>
		<div><h3>Books:</h3></div>
		<div>
			<c:forEach var="book" items="${list.books}">
				<li><c:out value="${book.title.name}" /></li>
			</c:forEach>
		</div>
	</div>

</div>