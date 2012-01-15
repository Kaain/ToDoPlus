package de.fhb.mobile.ToDoListAndroidApp.models;

import java.util.Date;
import java.util.List;

import android.provider.ContactsContract;
import android.text.format.DateFormat;

public class Todo {
	private long id;
	private String name;
	private String description;
	private boolean finished;
	private boolean favorite;
	private Date datetime;
	private List<Long> contacts;
	
	public Todo(long id) {
		this.id = id;
	}
	public Todo() {

	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public List<Long> getContacts() {
		return contacts;
	}
	public void setContacts(List<Long> contacts) {
		this.contacts = contacts;
	}
	
}
