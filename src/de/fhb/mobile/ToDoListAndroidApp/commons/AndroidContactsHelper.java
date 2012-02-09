package de.fhb.mobile.ToDoListAndroidApp.commons;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import de.fhb.mobile.ToDoListAndroidApp.models.Contact;

/**
 * The Class AndroidContactsHelper.<br>
 * The App need read permission on the phone
 */
public class AndroidContactsHelper {

	/**
	 * Gets all contacts.
	 * 
	 * @param contentResolver
	 *            the content resolver
	 * @return all contacts
	 */
	public static List<Contact> getAllContacts(ContentResolver contentResolver) {
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor people = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (people.moveToNext()) {
			int idFieldColumnIndex = people.getColumnIndex(PhoneLookup._ID);
			long id = people.getLong(idFieldColumnIndex);
			int nameFieldColumnIndex = people
					.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String name = people.getString(nameFieldColumnIndex);
			int phoneNumberColumnIndex = people
					.getColumnIndex(PhoneLookup.NUMBER);
			String phoneNumber = "";
			if (phoneNumberColumnIndex > 0) {
				phoneNumber = people.getString(phoneNumberColumnIndex);
			}
			String mail = getMailAddress(contentResolver, id);
			Contact contact = new Contact(id, name);
			contact.setPhoneNumber(phoneNumber);
			contact.setMail(mail);
			contacts.add(contact);
		}
		people.close();
		return contacts;
	}

	/**
	 * Gets the contacts from a list with ids
	 * 
	 * @param contentResolver
	 *            the content resolver
	 * @param contactIds
	 *            the contact ids
	 * @return the contacts
	 */
	public static List<Contact> getContacts(ContentResolver contentResolver,
			List<Long> contactIds) {
		List<Contact> contacts = new ArrayList<Contact>();
		List<Contact> allContacts = getAllContacts(contentResolver);

		for (Contact contact : allContacts) {
			if (contactIds.contains(contact.getId()))
				contacts.add(contact);
		}
		return contacts;
	}

	/**
	 * Gets the contact by ID with phonenumber and mail if exists
	 * 
	 * @param contentResolver
	 *            the content resolver
	 * @param contactId
	 *            the contact id
	 * @return the contact with ID and Displayname, null if contact not found
	 */
	public static Contact getContact(ContentResolver contentResolver,
			long contactId) {
		Contact returnContact = null;
		String[] selectionArgs = { String.valueOf(contactId) };
		Cursor people = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, PhoneLookup._ID
						+ "=?", selectionArgs, null);

		if (people.moveToFirst()) {
			int iName = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String name = people.getString(iName);
			int phoneNumberColumnIndex = people
					.getColumnIndex(PhoneLookup.HAS_PHONE_NUMBER);
			String phoneNumber = "";
			if (phoneNumberColumnIndex > 0) {
				phoneNumber = getPhoneNumber(contentResolver, contactId);
			}
			Log.i("", phoneNumber + "index:" + phoneNumberColumnIndex);
			String mail = getMailAddress(contentResolver, contactId);
			returnContact = new Contact(contactId, name);
			returnContact.setMail(mail);
			returnContact.setPhoneNumber(phoneNumber);
		}
		people.close();
		return returnContact;
	}

	/**
	 * Gets one mail address from the contact.
	 * 
	 * @param contentResolver
	 *            the content resolver
	 * @param contactID
	 *            the contact id
	 * @return the mail address or "" if there is none
	 */
	// from
	// http://stackoverflow.com/questions/6152442/how-to-get-contact-email-id
	public static String getMailAddress(ContentResolver contentResolver,
			long contactID) {

		String mail = "";
		contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { String.valueOf(contactID) }, null);

		Cursor emails = contentResolver.query(Email.CONTENT_URI, null,
				Email.CONTACT_ID + " = " + contactID, null, null);
		if (emails.moveToNext())
			mail = emails.getString(emails.getColumnIndex(Email.DATA));
		emails.close();
		return mail;
	}

	/**
	 * Gets one phone number.
	 * 
	 * @param contentResolver
	 *            the content resolver
	 * @param contactID
	 *            the contact id
	 * @return the phone number or "" if there is none
	 */
	public static String getPhoneNumber(ContentResolver contentResolver,
			long contactID) {
		String number = "";
		Cursor pCur = contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { String.valueOf(contactID) }, null);
		if (pCur.moveToNext()) {
			int iNumber = pCur
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			number = pCur.getString(iNumber);
		}
		pCur.close();
		return number;
	}
}
