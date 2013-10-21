<%@page import="java.sql.*"%>
<%@page import="amu.database.Database"%>
<h1>The TEMPORARY (in lieu of categories and such) List Of All Books In The Store</h1>
<%

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    
    try {
        connection = Database.getConnection();
        
        String query = "SELECT "
                + "book.id, "
                + "title.name, "
                + "book.published, "
                + "publisher.name, "
                + "book.edition, "
                + "book.binding, "
                + "book.isbn10, "
                + "book.isbn13, "
                + "book.description, "
                + "book.price "
                + "FROM title, book, publisher "
                + "WHERE title.id = book.title_id AND book.publisher_id = publisher.id;";
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery(query);
    	out.println("<div class='container'>");
        while (resultSet.next()) {
        	out.println("<div class='span5 well'");
            out.println("<h2>");
            String subTitle = "";
            if(resultSet.getString("title.name").length() >= 50){
            	subTitle = resultSet.getString("title.name").substring(0, 50) + "...";
            }
            else{
            	subTitle = resultSet.getString("title.name");
            }
            out.println("<a href='../viewBook.do?isbn=" + resultSet.getString("book.isbn13") + "'>");
            out.println(subTitle);
            out.println("</a>");
            out.println("</h2>");
            out.println("</div>");
		}
        out.println("</div>");
    } catch (SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
    } finally {
        Database.close(connection, statement, resultSet);
    }
%>