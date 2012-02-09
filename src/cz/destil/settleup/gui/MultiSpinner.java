package cz.destil.settleup.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import de.fhb.mobile.ToDoListAndroidApp.R;
import de.fhb.mobile.ToDoListAndroidApp.models.Contact;

// from http://stackoverflow.com/questions/5015686/android-spinner-with-multiple-choice
// modified
public class MultiSpinner extends Spinner implements
		OnMultiChoiceClickListener, OnCancelListener {

	private List<Contact> items;
	private boolean[] selected;
	private String defaultText;
	private MultiSpinnerListener listener;

	public MultiSpinner(Context context) {
		super(context);
	}

	public MultiSpinner(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if (isChecked)
			selected[which] = true;
		else
			selected[which] = false;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// refresh text on spinner
		StringBuffer spinnerBuffer = new StringBuffer();
		boolean someUnselected = false;
		for (int i = 0; i < items.size(); i++) {
			if (selected[i] == true) {
				spinnerBuffer.append(items.get(i));
				spinnerBuffer.append(", ");
			} else {
				someUnselected = true;
			}
		}
		String spinnerText;
		if (someUnselected) {
			spinnerText = spinnerBuffer.toString();
			if (spinnerText.length() > 2)
				spinnerText = spinnerText
						.substring(0, spinnerText.length() - 2);
		} else {
			spinnerText = defaultText;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item,
				new String[] { spinnerText });
		Log.i(this.getClass().getName(), spinnerText);
		setAdapter(adapter);
		listener.onItemsSelected(selected);
	}

	@Override
	public boolean performClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		String[] contactArray = new String[items.size()];
		for (int i = 0; i < items.size(); i++)
			contactArray[i] = items.get(i).getDisplayName();
		builder.setMultiChoiceItems(contactArray, selected, this);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setOnCancelListener(this);
		builder.show();
		return true;
	}

	public void setItems(List<Contact> items, String allText,
			boolean allSelected, MultiSpinnerListener listener) {
		this.items = items;
		this.defaultText = allText;
		this.listener = listener;

		// all selected by default
		selected = new boolean[items.size()];
		for (int i = 0; i < selected.length; i++)
			selected[i] = allSelected;

		// all text on the spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, new String[] { allText });
		setAdapter(adapter);
	}

	/* New methods */

	public void setSelectedContacts(List<Long> contacts) {
		StringBuffer spinnerBuffer = new StringBuffer();
		boolean nothingSelected = true;
		for (int i = 0; i < items.size(); i++) {
			for (int j = 0; j < contacts.size(); j++) {
				if (items.get(i).getId() == contacts.get(j)) {
					selected[i] = true;
					nothingSelected = false;
					spinnerBuffer.append(items.get(i).getDisplayName());
					spinnerBuffer.append(", ");
				}
			}
		}
		if (!nothingSelected)
			spinnerBuffer.delete(spinnerBuffer.length() - 2,
					spinnerBuffer.length());
		else
			spinnerBuffer.append(defaultText);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item,
				new String[] { spinnerBuffer.toString() });
		Log.i(this.getClass().getName(),
				"SpinnerText = " + spinnerBuffer.toString());
		setAdapter(adapter);
	}

	public List<Long> getSelectedContacts() {
		List<Long> list = new ArrayList<Long>(0);
		for (int i = 0; i < items.size(); i++) {
			if (selected[i])
				list.add(items.get(i).getId());
		}
		return list;
	}

	public interface MultiSpinnerListener {
		public void onItemsSelected(boolean[] selected);
	}

}
