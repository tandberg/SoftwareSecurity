package amu.database;

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

import amu.model.Book;
import amu.model.Customer;
import amu.model.PersonalBookList;

public class PersonalBookListDAO {
	
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	
	public boolean addNewListWithNoBooks(PersonalBookList personalBookList, Customer customer) {

		try {
			connection = Database.getConnection();
//INSERT INTO `user_booklist`(`id`, `Title`, `customer_id`) VALUES ([value-1],[value-2],[value-3])
			String query = "INSERT INTO `user_booklist` (title, customer_id, created, description) VALUES (?, ?, CURDATE(), ?)";
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, personalBookList.getTitle());
			statement.setInt(2, customer.getId());
			statement.setString(3, personalBookList.getDescription());

			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// TODO: Add OrderItems 
				return  true;
			}

		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return false;
	}
	public boolean addNewListWithBooks(PersonalBookList personalBookList, Customer customer) {

		try {
			connection = Database.getConnection();
//INSERT INTO `user_booklist`(`id`, `Title`, `customer_id`) VALUES ([value-1],[value-2],[value-3])
			String query = "INSERT INTO `user_booklist` (title, customer_id, created, description) VALUES (?, ?, CURDATE(), ?)";
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, personalBookList.getTitle());
			statement.setInt(2, customer.getId());
			statement.setString(3, personalBookList.getDescription());
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// TODO: Add OrderItems 
				for (Book book : personalBookList.getBooks()) {
					if(!addBookToList(resultSet.getInt(1), book.getId())){
						return false;
					}
				}
				return true;
			}

		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return false;
	}
	public boolean addBookToList(int listID, int book_id){

		try {
			connection = Database.getConnection();
			String query = "INSERT INTO `user_booklist_items` (user_booklist_id, book_id) VALUES (?, ?)";
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, listID);
			statement.setInt(2, book_id);
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return  true;
			}

		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return false;
	}
	public PersonalBookList findListByID(int listid){
		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `user_booklist` WHERE id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, listid);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(resultSet.getDate("created"));
				Customer customer = new CustomerDAO().getCustomerByID(resultSet.getInt("customer_id"));
				PersonalBookList personalBookList = new PersonalBookList(customer, createdDate, resultSet.getString("title"), resultSet.getString("description"));
				personalBookList.setId(resultSet.getInt("id"));

				personalBookList.setBooks(getBooks(resultSet.getInt("id")));
				return personalBookList;
				
			}
			
			} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return null;
	}
	public List<PersonalBookList> browsePersonalBooklists(Customer customer){
		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `user_booklist` WHERE customer_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, customer.getId());
			resultSet = statement.executeQuery();
			List<PersonalBookList> personalBookLists = new ArrayList<PersonalBookList>();
			while (resultSet.next()) {
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(resultSet.getDate("created"));
				
				PersonalBookList personalBookList = new PersonalBookList(customer, createdDate, resultSet.getString("title"), resultSet.getString("description"));
				personalBookList.setId(resultSet.getInt("id"));

				personalBookList.setBooks(getBooks(resultSet.getInt("id")));
				personalBookLists.add(personalBookList);
				
				
			}
			return personalBookLists;
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return new ArrayList<PersonalBookList>();
	}
	public List<Book> getBooks(int listid){
		ResultSet rs = null;
		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `user_booklist_items` WHERE user_booklist_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, listid);
			rs = statement.executeQuery();
			List<Book> listOfBooks = new ArrayList<Book>();
			BookDAO bookDAO = new BookDAO();
			while (rs.next()) {
				listOfBooks.add(bookDAO.findByID(rs.getInt("book_id")));
			}
			return listOfBooks;
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, rs);
		}

		return new ArrayList<Book>();
	}
	public List<PersonalBookList> getAllLists(){
		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `user_booklist`";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			List<PersonalBookList> personalBookLists = new ArrayList<PersonalBookList>();
			while (resultSet.next()) {
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(resultSet.getDate("created"));
				Customer customer = new CustomerDAO().getCustomerByID(resultSet.getInt("customer_id"));
				
				PersonalBookList personalBookList = new PersonalBookList(customer, createdDate, resultSet.getString("title"), resultSet.getString("description"));
				personalBookList.setId(resultSet.getInt("id"));

				personalBookList.setBooks(getBooks(resultSet.getInt("id")));
				personalBookLists.add(personalBookList);
				
				
			}
			return personalBookLists;
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return new ArrayList<PersonalBookList>();
	}
	

}
