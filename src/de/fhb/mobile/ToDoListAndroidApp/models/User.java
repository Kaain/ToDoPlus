package de.fhb.mobile.ToDoListAndroidApp.models;

public class User {
	private String username;
	private String password;
	private boolean oneConnectWithServer;
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public User() {
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isOneConnectWithServer() {
		return oneConnectWithServer;
	}
	public void setOneConnectWithServer(boolean oneConnectWithServer) {
		this.oneConnectWithServer = oneConnectWithServer;
	}
}
