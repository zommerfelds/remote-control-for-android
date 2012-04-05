// Copyright 2010 Christian Zommerfelds
// 
// This file is part of RemoteControl for Android.
// 
// RemoteControl for Android is free software: you can redistribute it
// and/or modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the License,
// or (at your option) any later version.
// 
// RemoteControl for Android is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with RemoteControl for Android.
// If not, see <http://www.gnu.org/licenses/>.

package ch.aero.RemoteControl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import ch.aero.RemoteControl.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private EditText ipTextInput; // IP inupt text box
	private TextView clickIpText; // IP inupt text box
	private ArrayAdapter<String> ipArraydapter; // list adapter behind the IP
												// list
	private String connectIpAddress; // IP address to connect to

	private static final String ipListFileName = "ip_list"; // "file" name for
															// storage of the IP
															// list
	private static final int DIALOG_ABOUT_ID = 0;

	public static final String EXTRA_NAME_IP = MainActivity.class.getPackage()
			.getName() + ".Ip";

	/**
	 * Main: Called when the activity is first created. Here begins the life of
	 * our app.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); // load the layout resource

		// get text field objects
		ipTextInput = (EditText) findViewById(R.id.EditTextIp);
		clickIpText = (TextView) findViewById(R.id.clickText);

		List<String> ipList = loadIpList(); // load IP list from storage

		// This adapter holds the list behind the IP-list widget
		ipArraydapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ipList);
		updateHelpLabelVisibility();

		ListView listView = (ListView) findViewById(R.id.ipListView); // get the IP list widget
		listView.setAdapter(ipArraydapter); // add the adapter to it

		// create a button click handler for the list widget
		listView.setOnItemClickListener(new OnItemClickListener() {
			// When clicked, connect to the corresponding IP
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				connect(((TextView) view).getText().toString()); // connect to the clicked IP
			}
		});
		registerForContextMenu(listView); // register for long clicks

		Button confirmButton = (Button) findViewById(R.id.ButtonConnect); // get the "connect" button

		// setup button listener
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String ipAddress = ipTextInput.getText().toString(); // read IP from text field
				connect(ipAddress); // and try to connect
			}
		});
	}

	/*
	 * Create a new context menu
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 * android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	/*
	 * Called when a selection is made in a context menu
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.deleteIp:
			String ip = ipArraydapter.getItem((int) info.id);
			removeIp(ip);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/*
	 * This is called the first time the menu button is pressed.
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	/*
	 * The user selected a menu item.
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			showDialog(DIALOG_ABOUT_ID);
			return true;
		case R.id.clearIPs:
			clearIpList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Connect to a server. This will start a new Activity.
	 * 
	 * @param ipAddress
	 *            IP address of the server
	 */
	private void connect(String ipAddress) {
		// start activity to connect to server
		connectIpAddress = ipAddress;
		Intent myIntent = new Intent();
		String packageName = this.getClass().getPackage().getName();
		String activityName = ControllerActivity.class.getName();
		myIntent.setClassName(packageName, activityName);
		myIntent.putExtra(EXTRA_NAME_IP, ipAddress);
		startActivityForResult(myIntent, ControllerActivity.CONNECT_REQUEST);
	}

	/**
	 * Gets the list of IP addresses stored in the phone
	 * 
	 * @return List of IPs
	 */
	private List<String> loadIpList() {
		List<String> ipList = new LinkedList<String>();
		try {
			BufferedReader ipListReader = new BufferedReader(
					new InputStreamReader(openFileInput(ipListFileName)));
			try {
				while (ipListReader.ready()) {
					ipList.add(ipListReader.readLine());
				}
			} finally {
				ipListReader.close();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			Toast.makeText(this, "Error: IOException reading IP list",
					Toast.LENGTH_SHORT).show();
		}
		return ipList;
	}

	/**
	 * Add IP address to the list and write it to the file
	 * 
	 * @param ip
	 *            IP address
	 */
	private void addIp(String ip) {
		FileOutputStream fos;
		try {
			fos = openFileOutput(ipListFileName, MODE_APPEND);
		} catch (FileNotFoundException e) {
			try {
				fos = openFileOutput(ipListFileName, MODE_PRIVATE);
			} catch (FileNotFoundException e1) {
				Toast.makeText(this, "Error storing IP to list",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		try {
			try {
				fos.write((ip + "\n").getBytes());
			} finally {
				fos.close();
			}
		} catch (IOException e) {
			Toast
					.makeText(this, "Error storing IP to list",
							Toast.LENGTH_SHORT).show();
		}
		ipArraydapter.add(connectIpAddress);
		updateHelpLabelVisibility();
	}

	/**
	 * Remove IP address from the list and write new list to the file
	 * 
	 * @param ip
	 *            IP address
	 */
	private void removeIp(String ip) {
		ipArraydapter.remove(ip);
		FileOutputStream fos;
		try {
			fos = openFileOutput(ipListFileName, MODE_PRIVATE);
			try {
				for (int i = 0; i < ipArraydapter.getCount(); i++) {
					String curIp = ipArraydapter.getItem(i);
					if (curIp != ip)
						fos.write((ip + "\n").getBytes());
				}
			} finally {
				if (fos != null)
					fos.close();
			}
		} catch (IOException e) {
			Toast
					.makeText(this, "Error storing IP to list",
							Toast.LENGTH_SHORT).show();
		}
		updateHelpLabelVisibility();
	}

	/**
	 * Clear stored IP list
	 */
	private void clearIpList() {
		try {
			FileOutputStream fos = openFileOutput(ipListFileName, MODE_PRIVATE);
			fos.close();
		} catch (FileNotFoundException e1) {
			Toast.makeText(this, "Error: could not clear the IP list file",
					Toast.LENGTH_LONG).show();
			return;
		} catch (IOException e) {
			Toast.makeText(this, "Error: IOException in close()",
					Toast.LENGTH_SHORT).show();
		}
		ipArraydapter.clear();
		updateHelpLabelVisibility();
	}

	/**
	 * Shows or hides the help text for clicking on the IP addresses
	 */
	private void updateHelpLabelVisibility() {
		if (ipArraydapter.isEmpty()) {
			clickIpText.setVisibility(View.INVISIBLE);
		} else {
			clickIpText.setVisibility(View.VISIBLE);
		}
	}

	/*
	 * This will get called when we get a result from the
	 * ControllerActivity
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ControllerActivity.CONNECT_REQUEST) {
			if (resultCode != ControllerActivity.RESULT_CONNECTION_ERROR) {
				// if the result code is good, store the IP address in the list
				if (ipArraydapter.getPosition(connectIpAddress) < 0) {
					addIp(connectIpAddress);
				}
			}
		}
	}

	/*
	 * Setup dialogs
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_ABOUT_ID:
			String version;
			try {
				version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				version = "#error getting version#";
			}
			dialog = new AlertDialog.Builder(this)
							.setTitle("About")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage("RemoteControl for Android\n" + "Version: " + version + "\n"
							          + "Author: Christian Zommerfelds\n"
							          + "E-Mail: christian.zommerfelds@gmail.com\n"
							          + "This program is licensed under the GNU GPL3")
							.setNeutralButton("OK", null)
							.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
}
