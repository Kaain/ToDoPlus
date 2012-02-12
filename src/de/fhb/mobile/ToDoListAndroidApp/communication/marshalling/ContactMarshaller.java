/**
 * 
 */
package de.fhb.mobile.ToDoListAndroidApp.communication.marshalling;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Christoph
 *
 */
public class ContactMarshaller {

	public static JSONObject marshall(Long contactid) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("contactId", contactid);
		return json;
	}

	public static JSONArray marshallList(List<Long> list)
			throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			jsonArray.put(marshall(list.get(i)));
		}
		return jsonArray;
	}
}
