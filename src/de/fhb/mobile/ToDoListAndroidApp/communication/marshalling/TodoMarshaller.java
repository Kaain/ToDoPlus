/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication.marshalling;

import java.text.ParseException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.fhb.mobile.ToDoListAndroidApp.models.Todo;


/**
 * @author Christoph
 *
 */
public class TodoMarshaller {
	public static JSONObject marshall(Todo todo) throws JSONException, ParseException {
		JSONObject json = new JSONObject();
		json.put("description", todo.getDescription());
		json.put("favourite", todo.isFavorite());
		json.put("finished", todo.isFinished());
		json.put("expire", todo.getExpireDate().getTime().getTime());
		json.put("lastChange", todo.getLastUpdated());
		json.put("name",todo.getName());
		json.put("id", todo.getId());
		json.put("contacts", ContactMarshaller.marshallList(todo.getContacts()));
		return json;
	}

	public static JSONArray marshallList(List<Todo> list)
			throws JSONException, ParseException {
		JSONArray jsonArray = new JSONArray();
		for(Todo t:list)
			jsonArray.put(TodoMarshaller.marshall(t));
		return jsonArray;
	}
}
