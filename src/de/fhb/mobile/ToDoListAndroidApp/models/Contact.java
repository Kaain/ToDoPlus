package de.fhb.mobile.ToDoListAndroidApp.models;

import java.util.List;

public class Contact {
	private long id;
	private String displayName;
	private List<Todo> todos;
	private String phoneNumber;
	private String mail;
	
	public Contact() {}
	
	public Contact(long id){
		super();
		this.id = id;
		todos = null;
	}
	
	public Contact(long id, String displayName) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.todos = todos;
	}

	public Contact(long id, List<Todo> todos){
		super();
		this.id = id;
		this.todos = todos;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String eMail) {
		this.mail = eMail;
	}

	@Override
	public String toString(){
		return this.displayName;
	}
}
