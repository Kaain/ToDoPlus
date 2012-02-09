package de.fhb.mobile.ToDoListAndroidApp.models;

import java.util.List;

/**
 * The Class Contact.
 */
public class Contact {

	/** The id. */
	private long id;

	/** The display name. */
	private String displayName;

	/** The todos. */
	private List<Todo> todos;

	/** The phone number. */
	private String phoneNumber;

	/** The mail. */
	private String mail;

	/**
	 * Instantiates a new contact.
	 */
	public Contact() {
	}

	/**
	 * Instantiates a new contact.
	 * 
	 * @param id
	 *            the id
	 */
	public Contact(long id) {
		super();
		this.id = id;
		todos = null;
	}

	/**
	 * Instantiates a new contact.
	 * 
	 * @param id
	 *            the id
	 * @param displayName
	 *            the display name
	 */
	public Contact(long id, String displayName) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.todos = todos;
	}

	/**
	 * Instantiates a new contact.
	 * 
	 * @param id
	 *            the id
	 * @param todos
	 *            the todos
	 */
	public Contact(long id, List<Todo> todos) {
		super();
		this.id = id;
		this.todos = todos;
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
	 * Gets the todos.
	 * 
	 * @return the todos
	 */
	public List<Todo> getTodos() {
		return todos;
	}

	/**
	 * Sets the todos.
	 * 
	 * @param todos
	 *            the new todos
	 */
	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * 
	 * @param displayName
	 *            the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the phone number.
	 * 
	 * @return the phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone number.
	 * 
	 * @param phoneNumber
	 *            the new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the mail.
	 * 
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Sets the mail.
	 * 
	 * @param eMail
	 *            the new mail
	 */
	public void setMail(String eMail) {
		this.mail = eMail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.displayName;
	}
}
