
<div class="container">
    <h1>Book</h1>
    <c:choose>
        <c:when test="${empty book}">
            <h2>Book not found!</h2>
            <div class = "index-item"><a href="debug/list_books.jsp">List books</a></div>
        </c:when>
        <c:otherwise>
            <h2><c:out value="${book.title.name}"></c:out></h2>
            <div>
                <ul class="booklist">
                    <li>
                        <b>Authors:</b> 
                        <c:forEach items="${book.author}" var="author" varStatus="it">
                            ${author.name}<c:if test="${!it.last}">, </c:if>
                        </c:forEach>
                    </li>
                    <li><b>Publisher:</b> <c:out value="${book.publisher.name}"></c:out></li>
                    <li><b>Published:</b> <c:out value="${book.published}"></c:out></li>
                    <li><b>Edition:</b> <c:out value="${book.edition} (${book.binding})"></c:out> </li>
                    <li><b>ISBN:</b> <c:out value="${book.isbn13}"></c:out></li>
                    <li><b>Price:</b> <c:out value="${book.price}"></c:out></li>
                </ul>
            </div>
            <br>
            <div>
                <b>Description: </b> <c:out value="${book.description}"></c:out>
            </div>
            <br>
            <div>
                <form class="addtocart" action="addBookToCart.do" method="post">
                    <input type="hidden" name="isbn" value="${book.isbn13}" />
                    <input type="text" name="quantity" value="1" />
                    <input type="submit" value="Add to cart" />
                </form>
                <form class="addToWishList" action="viewUserBooklists.do" method="post">
                    <input type="hidden" name="isbn" value="${book.isbn13}" />                
                    <input type="submit" value="Add to personal booklist" />
                </form>
                <form class="addreview" action="addReviewToBook.do" method="post">
                    <input type="hidden" name="isbn" value="${book.isbn13}" />                
                    <input type="submit" value="Review" />
                </form>
            </div>
            <br>
            <div class="reviewContainer">
            	<c:forEach var="review" items="${reviews}" varStatus="counter">
	           		<div class = "singleReview">
	            		<div class = "singleReviewLeft">
	           				<div><p class="nameOfReviewer"> Name: <c:out value="${review.writer.name}"></c:out>  </p></div>
	            		</div>
	            		<div class = "singleReviewRight">
	            			
	            			<div><p class="titleReview">"<c:out value="${review.title}"></c:out>"</p></div>
			                <div class="reviewedDate">Reviewed <fmt:formatDate value="${review.created.time}" type="date" dateStyle="short"/> </div>
			               	<br>
			                <div class="reviewText"><c:out value="${review.text}"></c:out></div>
			                <br>
			                <div class="likesDivReview">${review.likes} likes and ${review.dislikes} dislikes </div>
			                <div>
			                <br>
			                <p>Was this review helpful?</p> 
			                <form class="likedForm" action="rateBookReview.do" method="post">
                    			<input type="hidden" name="isbn" value="${book.isbn13}" />
                    			<input type="hidden" name="reviewid" value="${review.id}" />
                    			<input type="hidden" name="liked" value="yes" />
                    			<input class="liked" type="submit" value="Yes">
                    				                			</form>
                			<form class="dislikedForm" action="rateBookReview.do" method="post">
                    			<input type="hidden" name="isbn" value="${book.isbn13}" />
                    			<input type="hidden" name="reviewid" value="${review.id}" />
                    			<input type="hidden" name="liked" value="no" />
                    			<input class="disliked" type="submit" value="No" />
                			</form>
			                </div>
	           			 </div>
           			</div>
            		<br>
        		</c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>