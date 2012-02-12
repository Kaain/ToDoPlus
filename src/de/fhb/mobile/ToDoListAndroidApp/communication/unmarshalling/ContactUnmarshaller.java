/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication.unmarshalling;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Patrick
 *
 */
public class ContactUnmarshaller {

	public static Long unmarshall(JSONObject contactJson) throws JSONException {
		return new Long(contactJson.getInt("contactId"));
	}

	public static List<Long> unmarshallList(JSONArray list)
			throws JSONException {
		List<Long> listContact = new ArrayList<Long>();
		for (int i = 0; i < list.length(); i++) {
			listContact.add(unmarshall(list.getJSONObject(i)));
		}
		return listContact;
	}
}
