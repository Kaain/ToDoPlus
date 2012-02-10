package de.fhb.mobile.ToDoListAndroidApp.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;

/**
 * The Class Todo.
 */
public class Todo {

	/** The id. */
	private long id;

	/** The name. */
	private String name;

	/** The description. */
	private String description;

	/** The finished. */
	private boolean finished;

	/** The favorite. */
	private boolean favorite;

	/** The expire date. */
	private Calendar expireDate;

	/** The contacts. */
	private List<Long> contacts;
	
	/** The last updated. */
	private long lastUpdated;

	/**
	 * Instantiates a new todo.
	 * 
	 * @param id
	 *            the id
	 */
	public Todo(long id) {
		this.id = id;
	}

	/**
	 * Instantiates a new todo.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @param finished
	 *            the finished
	 * @param favorite
	 *            the favorite
	 * @param expireDate
	 *            the expire date
	 * @param contacts
	 *            the contacts
	 */
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

	/**
	 * Instantiates a new todo.
	 */
	public Todo() {
		this.expireDate = null;
		this.contacts = new ArrayList<Long>(0);
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Checks if is finished.
	 * 
	 * @return true, if is finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Sets the finished.
	 * 
	 * @param finished
	 *            the new finished
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Checks if is favorite.
	 * 
	 * @return true, if is favorite
	 */
	public boolean isFavorite() {
		return favorite;
	}

	/**
	 * Sets the favorite.
	 * 
	 * @param favorite
	 *            the new favorite
	 */
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	/**
	 * Gets the expire date.
	 * 
	 * @return the expire date
	 */
	public Calendar getExpireDate() {
		return expireDate;
	}

	/**
	 * Gets the expire date as string.
	 * 
	 * @return the expire date as string
	 */
	public String getExpireDateAsString() {
		return DateHelper.getDateTimeAsString(this.expireDate);
	}

	/**
	 * Sets the expire date.
	 * 
	 * @param expireDate
	 *            the new expire date
	 */
	public void setExpireDate(Calendar expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * Gets the contacts.
	 * 
	 * @return the contacts
	 */
	public List<Long> getContacts() {
		return contacts;
	}

	/**
	 * Sets the contacts.
	 * 
	 * @param contacts
	 *            the new contacts
	 */
	public void setContacts(List<Long> contacts) {
		this.contacts = contacts;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getName();
	}

}
