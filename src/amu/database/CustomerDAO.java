package amu.database;

import amu.Config;
import amu.model.Customer;
import amu.security.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.logging.*;

import javax.sql.*;
import javax.xml.bind.DatatypeConverter;

public class CustomerDAO {

    public static String hashPassword(String plainTextPassword) {
        String hashedPassword = null;
        // Calculate SHA1(password+salt)
		hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
		//hashedPassword = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA1").digest((plainTextPassword + Config.SALT).getBytes()));
        
        return hashedPassword;
    }

    public static String generateActivationCode() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[8];
        random.nextBytes(bytes);
        return DatatypeConverter.printHexBinary(bytes);
    }

    public Customer findByEmail(String email) {
        Customer customer = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM customer WHERE email=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByEmail SQL Query: " + query);


            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPassword(resultSet.getString("password"));
                customer.setName(resultSet.getString("name"));
                customer.setActivationToken(resultSet.getString("activation_token"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }

    public boolean edit(Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "UPDATE customer SET email=?, password=?, name=? WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setInt(4, customer.getId());
          
            if (statement.executeUpdate() == 0) {
                return false; // No rows were affected
            } else {
                return true;
            }

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
            return false;
        } finally {
            Database.close(connection, statement, resultSet);
        }
    }
    
    public Customer register(Customer customer) {

        DataSource dataSource = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO customer (email, password, name, activation_token) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setString(4, customer.getActivationToken());
            
            
            
            statement.executeUpdate();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "register SQL Query: " + query);

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
        }

        return findByEmail(customer.getEmail());
    }

    public Customer activate(Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "UPDATE customer SET activation_token=NULL WHERE email=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.executeUpdate();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "activate SQL Query: " + query);

            customer.setActivationToken(null);

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }
    public Customer getCustomerByID(int customer_id){
    	Customer customer = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM customer WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, customer_id);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByID SQL Query: " + query);


            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPassword(resultSet.getString("password"));
                customer.setName(resultSet.getString("name"));
                customer.setActivationToken(resultSet.getString("activation_token"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }
    
}