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

import ch.aero.RemoteControl.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	    ipText = (EditText) findViewById(R.id.EditTextIp);
	    
	    Button confirmButton = (Button) findViewById(R.id.ButtonConnect);
	    confirmButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	        	Intent myIntent = new Intent();
	        	myIntent.setClassName("ch.aero.RemoteControl", "ch.aero.RemoteControl.ControllerActivity");
	        	myIntent.putExtra("ch.aero.RemoteControl.Ip", ipText.getText().toString()); // key/value pair, where key needs current package prefix.
	        	startActivity(myIntent);
	        }
	    });
    
    }
    
    private EditText ipText;
}