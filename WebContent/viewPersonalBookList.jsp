<div class="container">
	<div class="addNewList">
		<c:choose>
			<c:when test="${empty book}">
				<h2>Create a new book list or look at them</h2>
				<form action="createNewList.do" method="post">
					<div>
						<b>Title: </b>
					</div>
					<div>
  						<textarea id="title" name="title" rows="1" cols="20"></textarea>
					</div>
					<div>
						<b>Description: </b>
					</div>
					<div>
						<textarea id="text" name="text" rows="3" cols="20"></textarea>
					</div>
					<div>
						<input type="submit" value="Create new booklist" />
					</div>
				</form>
			</c:when>
			<c:otherwise>
				<h2>Add or create a new list with <c:out value="${book.title.name}"></c:out> </h2>

				<form action="createNewList.do" method="post">
					<input type="hidden" name="isbn" value="${book.isbn13}" />
					<div>
						<b>Title: </b>
					</div>
					<div>
  						<textarea id="title" name="title" rows="1" cols="20"></textarea>
					</div>
					<div>
						<b>Description: </b>
					</div>
					<div>
						<textarea id="text" name="text" rows="3" cols="20"></textarea>
					</div>
					<div>
						<input type="submit" value="Create new booklist and add book" />
					</div>
				</form>
			</c:otherwise>

		</c:choose>
		<br></br>
		<br></br>
		

	</div>
	<div>
	<br></br>
	<br></br>
		<c:choose>
			<c:when test="${empty book}">
				<c:forEach var="list" items="${listOfBookLists}" varStatus="counter">
					<div class="personalBooklist">
					<div class="personalBooklistLeft">
					
							<div>
								<p class="titleBooklist"><c:out value="${list.title}"></c:out></p>
							</div>
							<div class="created">Created <fmt:formatDate value="${list.createdDate.time}" type="date"
									dateStyle="short" />
		
							</div>
						</div>
						<div class="personalBooklistRight">
							<b>Description: </b><c:out value="${list.description}"></c:out> 
							<c:forEach var="book" items="${list.books}" end="2">
								<li><c:out value="${book.title.name}"/></li>
							</c:forEach>
							<form action="viewSingleList.do" method="post">
									<input type="hidden" name="list_id" value="${list.id}" />
									<input type="submit" value="View more..." />
							</form>
						</div>
						<div class="clear"></div>
					</div>
					<br>
				</c:forEach>
			</c:when>
			<c:otherwise>

				<c:forEach var="list" items="${listOfBookLists}" varStatus="counter">
					<div class="personalBooklist">
						<div class="personalBooklistLeft">
					
							<div>
								<p class="titleBooklist"><c:out value="${list.title}"></c:out></p>
							</div>
							<div class="created">Created <fmt:formatDate value="${list.createdDate.time}" type="date"
									dateStyle="short" />
							</div>
						</div>
						<div class="personalBooklistRight">
							<b>Description: </b><c:out value="${list.description}"></c:out> 
						
							<c:forEach var="book" items="${list.books}" end="2">
								<li><c:out value="${book.title.name}"/></li>
							</c:forEach>
							<form class="addBookToListForm" action="addBookToPersonalList.do" method="post">
									<input type="hidden" name="isbn" value="${book.isbn13}" />
									<input type="hidden" name="list_id" value="${list.id}" />
									<input type="submit" value="Add to this list" />
							</form>
							<form class="viewMoreForm" action="viewSingleList.do" method="post">
									<input type="hidden" name="list_id" value="${list.id}" />
									<input type="submit" value="View more..." />
							</form>
						</div>
						<div class="clear"></div>
					
					</div>
					<br>
				</c:forEach>
			</c:otherwise>

		</c:choose>

	</div>

</div>