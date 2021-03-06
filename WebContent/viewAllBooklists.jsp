<div class="container">
	<c:forEach var="list" items="${listOfBookLists}" varStatus="counter">
		<div class="personalBooklist">
			<div class="personalBooklistLeft">

				<div>
					<b>Created by:</b>
				</div>
				<div>
					<p><c:out value="${list.customer.name}"></c:out> </p>
				</div>
				<div>
					<b>Title:</b>
				</div>
				<div>
					<p class="titleBooklist">"<c:out value="${list.title}"></c:out>"</p>
				</div>
				<div class="created">
					Created
					<fmt:formatDate value="${list.createdDate.time}" type="date"
						dateStyle="short" />

				</div>
			</div>
			<div class="personalBooklistRight">
				<b>Description: </b> <c:out value="${list.description}"></c:out>
				<c:forEach var="book" items="${list.books}" end="2">
					<li><c:out value="${book.title.name}" /></li>
				</c:forEach>
				<form action="viewSingleList.do" method="post">
					<input type="hidden" name="list_id" value="${list.id}" /> <input
						type="submit" value="View more..." />
				</form>
			</div>
			<div class="clear"></div>
		</div>
		<br>
	</c:forEach>
</div>