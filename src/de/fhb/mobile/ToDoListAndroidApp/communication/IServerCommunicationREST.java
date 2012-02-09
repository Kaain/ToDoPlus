/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication;

import java.util.List;

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
	 */
	public boolean authentifactation(String username,String password);
		
	/**
	 * synchronize local todolist with server-todolist
	 * 
	 * @param todoList
	 */
	public void synchronize(List<Todo> todoList);
	
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
