/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication.unmarshalling;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.fhb.mobile.ToDoListAndroidApp.models.User;

/**
 * @author Patrick
 *
 */
public class UserUnmarshaller {

	public static User unmarshall(JSONObject userJson) throws JSONException{
		User user = new User();
		user.setUsername(userJson.getString("username"));
		user.setPassword(userJson.getString("password"));
		return user;
	}
	
	public static List<User> unmarshallList(JSONArray list) throws JSONException{
		List<User> listUser = new ArrayList<User>();
		for(int i=0;i<list.length();i++){
			listUser.add(unmarshall(list.getJSONObject(i)));
		}
		return listUser;
	}
}
