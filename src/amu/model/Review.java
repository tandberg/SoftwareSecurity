package amu.model;

import java.util.Calendar;

public class Review implements Comparable<Review> {
	private Calendar created;
	private String title;
	private String text;
	private int likes;
	private int dislikes;
	private Customer writer;
	private int book_id;
	private int id;



	public Review(Calendar created, String title, String text, int likes,
			int dislikes, Customer writer, int book_id, int id) {
		this.created = created;
		this.title = title;
		this.text = text;
		this.likes = likes;
		this.dislikes = dislikes;
		this.writer = writer;
		this.book_id = book_id;
		this.id = id;
	}
	public int getId(){
		return id;
	}

	public Calendar getCreated() {
		return created;
	}
	public void setCreated(Calendar created) {
		this.created = created;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public Customer getWriter() {
		return writer;
	}
	public void setWriter(Customer writer) {
		this.writer = writer;
	}

	@Override
	public int compareTo(Review review) {
		// TODO Auto-generated method stub
		return  (review.getLikes() - review.getDislikes()) - (this.likes - this.dislikes);
	}


}
