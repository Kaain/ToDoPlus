package de.fhb.mobile.ToDoListAndroidApp.commons;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {
	public static String listToString(List<Long> list) {
		if (!list.isEmpty()) {
			String string = String.valueOf(list.get(0));
			for (int i = 1; i < list.size(); i++)
				string = string + "|" + list.get(i);
			return string;
		} else
			return "";

	}
	public static List<Long> stringToList(String string){
		if(string.isEmpty())
			return new ArrayList<Long>(0);
		
		String[] stringA = string.split("\\|");
		List<Long> list = new ArrayList<Long>();
		for(int i = 0; i < stringA.length; i++)
			list.add(Long.valueOf(stringA[i]));
		return list;
	}
}
