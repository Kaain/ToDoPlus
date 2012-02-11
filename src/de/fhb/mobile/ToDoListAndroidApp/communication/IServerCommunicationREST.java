/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication;

import java.io.IOException;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;

import de.fhb.mobile.ToDoListAndroidApp.models.Todo;


/**
 * @author Patrick
 *
 */
public interface IServerCommunicationREST {
	
	/**
	 * authentificate the user on the server.
	 * 
	 * @param username
	 * @param password
	 * @return true if you are authentifacted.
	 * @throws IOException 
	 */
	public boolean authentifactation(String username,String password) throws ConnectTimeoutException;
		
	/**
	 * synchronize local todolist with server-todolist
	 * 
	 * @param todoList
	 * @return 
	 */
	public boolean synchronize(List<Todo> todoList);
	
	/**
	 * get all todos from Server.
	 * 
	 * @return all your own todos.
	 */
	public List<Todo> getAllTodo();

	/**
	 * add a new todo to your list.
	 * 
	 * @param todo
	 */
	public void addTodo(Todo todo);
	
	/**
	 * delete a todo from your list.
	 * 
	 * @param todo
	 */
	public void deleteTodo(Todo todo);
	
}
