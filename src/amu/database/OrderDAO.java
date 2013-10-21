package amu.database;

import amu.model.Book;
import amu.model.Cart;
import amu.model.CartItem;
import amu.model.Customer;
import amu.model.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	public Order getSingleOrderByID(int id, Customer customer){
		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `order` WHERE id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				AddressDAO addressDAO = new AddressDAO();
				Calendar createdDate = Calendar.getInstance();

				Order order = new Order(resultSet.getInt("id"),
						customer, 
						addressDAO.read(resultSet.getInt("address_id")), 
						createdDate, 
						resultSet.getString("value"), 
						resultSet.getInt("status"));
				return order;
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return null;

	}

	public List<Order> browse(Customer customer) {
		List<Order> orders = new ArrayList<Order>();

		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `order` WHERE customer_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, customer.getId());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				AddressDAO addressDAO = new AddressDAO();
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(resultSet.getDate("created"));
				orders.add(new Order(resultSet.getInt("id"),
						customer, 
						addressDAO.read(resultSet.getInt("address_id")), 
						createdDate, 
						resultSet.getString("value"), 
						resultSet.getInt("status")));
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return orders;
	}

	public boolean add(Order order) {

		try {
			connection = Database.getConnection();

			String query = "INSERT INTO `order` (customer_id, address_id, created, value, status) VALUES (?, ?, CURDATE(), ?, ?)";
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, order.getCustomer().getId());
			statement.setInt(2, order.getAddress().getId());
		
			statement.setBigDecimal(3, new BigDecimal(order.getValue()));
			statement.setInt(4, order.getStatus());
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// TODO: Add OrderItems 
				return addOrderItems(order, resultSet.getInt(1));
			}

		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return false;
	}
	private boolean addOrderItems(Order order, int orderID){
		try {
			System.out.println(order.getValue());
			connection = Database.getConnection();
			String query = "INSERT INTO `order_items` (order_id, book_id, quantity, price, status) VALUES (?, ?, ?, ?, ?)";

			for (CartItem item : order.getCart().getItems().values()) {
				statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

				statement.setInt(1, orderID);
				statement.setInt(2, item.getBook().getId());
				System.out.println("S: " + item.getQuantity());
				statement.setInt(3, item.getQuantity());
				statement.setFloat(4, item.getBook().getPrice());
				statement.setInt(5, order.getStatus());
				statement.executeUpdate();
				resultSet = statement.getGeneratedKeys();
			}
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
	public Cart getOrderCartItems(int orderID){
		Cart cart = new Cart();

		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `order_items` WHERE order_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, orderID);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Book book = new BookDAO().findByID(resultSet.getInt("book_id"));
				CartItem item = new CartItem(book, resultSet.getInt("quantity"));
				cart.addItem(item);
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return cart;
	}
	public void updateStatusOrder(int status, int orderId){
		try {
			System.out.println("STAT2: " + status);

			connection = Database.getConnection();
			String query = "UPDATE `order` SET status=? WHERE id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, status);
			statement.setInt(2, orderId);
			System.out.println(statement.execute());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			Database.close(connection, statement, resultSet);
		}
		updateStatusOrderItems(orderId, status);
	}
	private void updateStatusOrderItems(int orderId, int status){
		try {
			System.out.println("STAT: " + status);
			connection = Database.getConnection();
			String query = "UPDATE `order_items` SET status=? WHERE order_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, status);
			statement.setInt(2, orderId);
			statement.execute();	

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			Database.close(connection, statement, resultSet);
		}
	}
	/**
	 * Metode som passer på at man ikke kan endre på andres ordre.
	 * */
    public boolean checkOrderAccess(int customerid, int orderid){
        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM `order` WHERE id=? AND customer_id=? AND status=0";
            statement = connection.prepareStatement(query);
            statement.setInt(1, orderid);
            statement.setInt(2, customerid);
            	

            resultSet = statement.executeQuery();

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
