package de.fhb.mobile.ToDoListAndroidApp.commons;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import de.fhb.mobile.ToDoListAndroidApp.models.Contact;

/**
 * The Class AndroidContactsHelper.<br>
 * The App need read permission on the phone
 */
public class AndroidContactsHelper {

	/**
	 * Gets all contacts.
	 *
	 * @param contentResolver the content resolver
	 * @return all contacts
	 */
	public static List<Contact> getAllContacts(ContentResolver contentResolver){
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor people = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while(people.moveToNext()) {
		   int idFieldColumnIndex = people.getColumnIndex(PhoneLookup._ID);
		   long id = people.getLong(idFieldColumnIndex);
		   int nameFieldColumnIndex = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
		   String name = people.getString(nameFieldColumnIndex);
		   Contact contact = new Contact(id, name);
		   contacts.add(contact);
		}
		people.close();
		return contacts;
	}
	
	/**
	 * Gets the contacts from a list with ids
	 *
	 * @param contentResolver the content resolver
	 * @param contactIds the contact ids
	 * @return the contacts
	 */
	public static List<Contact> getContacts(ContentResolver contentResolver, List<Long> contactIds){
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor people = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while(people.moveToNext()) {
		   int iId = people.getColumnIndex(PhoneLookup._ID);
		   long id = people.getLong(iId);
		   if(contactIds.contains(id)){
			   int iName = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			   String name = people.getString(iName);
			   Contact contact = new Contact(id, name);
			   contacts.add(contact);
		   }
		}
		people.close();
		return contacts;
	}
	
	/**
	 * Gets the contact by ID.
	 *
	 * @param contentResolver the content resolver
	 * @param contactId the contact id
	 * @return the contact with ID and Displayname,
	 * 		null if contact not found
	 */
	public static Contact getContact(ContentResolver contentResolver, long contactId){
		Contact returnContact = null;
		String[] selectionArgs = { String.valueOf(contactId) };
		Cursor people = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, PhoneLookup._ID +"=?", selectionArgs, null);
		
		if(people.moveToFirst()){
			int iName = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String name = people.getString(iName);
			returnContact = new Contact(contactId, name);
		}
		people.close();
		return returnContact;
	}
}
