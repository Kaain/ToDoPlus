/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication;

import java.util.List;

import de.fhb.mobile.ToDoListAndroidApp.models.Todo;

/**
 * This class implements the REST commincation.
 * 
 * @author Patrick
 *
 */
public class ServerCommunicationREST implements IServerCommunicationREST{

	@Override
	public boolean authentifactation(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void synchronize(List<Todo> todoList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Todo> getAllTodo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTodo(Todo todo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTodo(Todo todo) {
		// TODO Auto-generated method stub
		
	}

}
