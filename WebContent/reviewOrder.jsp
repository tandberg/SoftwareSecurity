<div class="container">
    <h1>Review Order</h1>
    <h2>Shopping cart:</h2>
    <c:choose>
        <c:when test="${empty cart.items}">
            <div>No items in shopping cart.</div>
        </c:when>
        <c:otherwise>
            <c:forEach items="${cart.items}" var="item">
                <div>${item.value.book.title.name}</div>
                <div>Price: ${item.value.book.price}</div>
                <div> Quantity: ${item.value.quantity}</div>
            </c:forEach>
            <c:choose>
                <c:when test="${cart.numberOfItems == 1}">
                    <div>Subtotal: ${cart.subtotal}</div>
                </c:when>
                <c:otherwise>
                    <div>Subtotal (${cart.numberOfItems} items): ${cart.subtotal}</div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
    <h2>Shipping address:</h2>
    <div><c:out value="${cart.shippingAddress.address}"></c:out></div>
    <h2>Payment method:</h2>
    <div>Credit card number: <c:out value="${cart.creditCard.maskedCreditCardNumber}"></c:out></div>
    <div>Expiry date: <fmt:formatDate value="${cart.creditCard.expiryDate.time}" type="date" dateStyle="short" /></div>
    <div>Cardholder's name: <c:out value="${cart.creditCard.cardholderName}"></c:out></div>
    <div><a href="placeOrder.do">Place order</a></div>
</div>