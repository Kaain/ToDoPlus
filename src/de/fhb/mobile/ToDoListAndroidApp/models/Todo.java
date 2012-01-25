package de.fhb.mobile.ToDoListAndroidApp.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;

public class Todo{

	private long id;
	private String name;
	private String description;
	private boolean finished;
	private boolean favorite;
	private Calendar expireDate;
	private List<Long> contacts;
	
	public Todo(long id) {
		this.id = id;
	}
	
	public Todo(long id, String name, String description, boolean finished,
			boolean favorite, Calendar expireDate, List<Long> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.finished = finished;
		this.favorite = favorite;
		this.expireDate = expireDate;
		this.contacts = contacts;
	}

	public Todo() {
		this.expireDate = null;
		this.contacts = new ArrayList<Long>(0);
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
	public Calendar getExpireDate() {
		return expireDate;
	}
	public String getExpireDateAsString(){
		return DateHelper.getDateTimeAsString(this.expireDate);
	}
	public void setExpireDate(Calendar expireDate) {
		this.expireDate = expireDate;
	}
	public List<Long> getContacts() {
		return contacts;
	}
	public void setContacts(List<Long> contacts) {
		this.contacts = contacts;
	}
	@Override
	public String toString(){
		return this.getName();
	}
	
}
