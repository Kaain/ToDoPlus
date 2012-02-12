/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication.unmarshalling;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.fhb.mobile.ToDoListAndroidApp.models.Todo;

/**
 * @author Patrick
 * 
 */
public class TodoUnmarshaller {

	public static Todo unmarshall(JSONObject todoJson) throws JSONException,
			ParseException {
		Todo todo = new Todo();

		GregorianCalendar expire = new GregorianCalendar();
		expire.setTimeInMillis(todoJson.getLong("expire"));
		todo.setDescription(todoJson.getString("description"));
		todo.setFavorite(todoJson.getBoolean("favourite"));
		todo.setFinished(todoJson.getBoolean("finished"));
		todo.setExpireDate(expire);
		todo.setLastUpdated(todoJson.getLong("lastChange"));
		todo.setName(todoJson.getString("name"));
		// todo.setUser(UserUnmarshaller.unmarshall(todoJson.getJSONObject("user")));System.out.println(i++);
		todo.setContacts(ContactUnmarshaller.unmarshallList(todoJson
				.getJSONArray("contacts")));

		return todo;
	}

	public static List<Todo> unmarshallList(JSONArray list)
			throws JSONException, ParseException {
		List<Todo> listTodo = new ArrayList<Todo>();
		for (int i = 0; i < list.length(); i++) {
			listTodo.add(unmarshall(list.getJSONObject(i)));
		}
		return listTodo;
	}
}
