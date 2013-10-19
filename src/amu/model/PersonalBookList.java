package amu.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PersonalBookList {

	private List<Book> books;
	private Customer customer;
    private Calendar createdDate;
    private String title;
    private int id;
    private String description;



	public PersonalBookList(Customer customer, Calendar createdDate, String title, String description){
		books = new ArrayList<Book>();
		this.customer = customer;
		this.createdDate = createdDate;
		this.title = title;
		this.description = description;
	}
	public PersonalBookList(List<Book> books, Customer customer, Calendar createdDate, String title, String description){
		this.books = books;
		this.customer = customer;
		this.createdDate = createdDate;
		this.title = title;
		this.description = description;
	}
	public void setBooks(List<Book> books){
		this.books = books;
	}

	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public boolean addBook(Book book){
		if(book != null){
			return this.books.add(book);
		}
		return false;
	}
	public boolean removeBook(Book book){
		if(book != null){
			return this.books.remove(book);
		}
		return false;

	}
	public List<Book> getBooks(){
		return this.books;
	}
	public String getCustomerName(){
		return this.customer.getName();
	}
	public Customer getCustomer(){
		return this.customer;
	}
    public Calendar getCreatedDate() {
        return this.createdDate;
    }
    public String getTitle(){
    	return this.title;
    }
    public String getDescription(){
    	return this.description;
    }



}
