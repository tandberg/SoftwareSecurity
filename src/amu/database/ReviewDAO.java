package amu.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import amu.model.Book;
import amu.model.Customer;
import amu.model.Order;
import amu.model.Publisher;
import amu.model.Review;
import amu.model.Title;

public class ReviewDAO {
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	public boolean addReview(String text, String title, int book_id, int customer_id){
		try {
			connection = Database.getConnection();
			String query = "INSERT INTO `review` (text, likes, dislikes, customer_id, created, title, book_id) "
					+ "VALUES (?, ?, ?, ?, CURDATE(), ?, ?)";
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, text);
			statement.setInt(2, 0);
			statement.setInt(3, 0);
			statement.setInt(4, customer_id);
			statement.setString(5, title);
			statement.setInt(6, book_id);
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// TODO: Add OrderItems 
				return true;
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return false;
	}
	public List<Review> findReviewByBookID(int book_id){
		List<Review> reviews = new ArrayList<Review>();

		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `review` WHERE book_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, book_id);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Customer customer = new CustomerDAO().getCustomerByID(resultSet.getInt("customer_id"));
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(resultSet.getDate("created"));
				reviews.add(new Review(createdDate, resultSet.getString("title"), resultSet.getString("text"),
						resultSet.getInt("likes"), resultSet.getInt("dislikes"), customer, resultSet.getInt("book_id"), resultSet.getInt("id")));
				
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}
		Collections.sort(reviews);

		return reviews;
	}
	public void updateLikes(int review_id){

		try {
			connection = Database.getConnection();
			String query = "SELECT likes FROM `review` WHERE id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, review_id);
			resultSet = statement.executeQuery();
			

			if (resultSet.next()) {
				int likes = resultSet.getInt("likes");
				likes = likes + 1;
				String updateQuery = "UPDATE `review` SET likes=? WHERE id=?";
				performUpdate(updateQuery, likes, review_id);
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}
	}
	public void updateDislikes(int review_id){

		try {
			connection = Database.getConnection();
			String query = "SELECT dislikes FROM `review` WHERE id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, review_id);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int dislikes = resultSet.getInt("dislikes");
				dislikes = dislikes + 1;
				String updateQuery = "UPDATE `review` SET dislikes=? WHERE id=?";
				performUpdate(updateQuery, dislikes, review_id);
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}
	}
	private void performUpdate(String query, int updateNumber, int reviewid){
		try {
			connection = Database.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, updateNumber);
			statement.setInt(2, reviewid);
			statement.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			Database.close(connection, statement, resultSet);
		}
	}
	public boolean customerHasLikedOrDisliked(int customerid, int reviewid){
        try {
        	
            connection = Database.getConnection();            
            String query = "SELECT * FROM `review_vote_by_customer` WHERE `customer_id`= ? AND `review_id`= ?";
        	System.out.println("R " + query);

            statement = connection.prepareStatement(query);
            statement.setInt(1, customerid);
            statement.setInt(2, reviewid);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
            	return true;
                // TODO: Reviews, Categories
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        
        return false;
		
	}
	public boolean addLikedToUser(int customerid, int reviewid){
		try {
			connection = Database.getConnection();
			String query = "INSERT INTO `review_vote_by_customer` (customer_id, review_id) "
					+ "VALUES (?, ?)";
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, customerid);
			statement.setInt(2, reviewid);
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return false;
	}
}
