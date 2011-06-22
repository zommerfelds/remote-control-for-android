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

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ControllerActivity extends Activity {

	private Connection connection;

	public final static int CONNECT_REQUEST = 0;
	public final static int RESULT_CONNECTION_ERROR = 100;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.controller); // set the layout from the XML file

		// get the IP address from the main activity's intent
		String ip = super.getIntent().getExtras().getString(
				MainActivity.EXTRA_NAME_IP);

		// setup a new connection
		connection = new Connection();
		try {
			connection.open(ip);
		} catch (IOException e) {
			// if failed, show error and exit
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

			setResult(RESULT_CONNECTION_ERROR);

			finish();
			return;
		}

		setResult(RESULT_OK);

		// create buttons layout with data from server
		LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

		byte i = 0;
		for (String label : connection.getButLabels()) {

			Button button = new Button(this);
			button.setText(label);
			linLayout.addView(button);

			// class to listen for button clicks
			class ButtonClickListener implements View.OnClickListener {
				ButtonClickListener(byte butNum) {
					this.butNum = butNum;
				}

				public void onClick(View view) {
					// send event to server
					try {
						connection.sendButClick(butNum);
					} catch (IOException e) {
						Toast.makeText(getApplicationContext(), e.getMessage(),
								Toast.LENGTH_SHORT).show();
						finish();
					}
				}

				private byte butNum;
			}
			;

			button.setOnClickListener(new ButtonClickListener(i));

			if (i == 255)
				break;
			i++;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			connection.close(); // close the connection
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}
}