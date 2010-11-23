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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Iterator;

public class ControllerActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the IP address from the main activity's intent
        String ip = super.getIntent().getExtras().getString("ch.aero.RemoteControl.Ip");
        
        // setup a new connection
        connection = new Connection();
        boolean success = connection.open(ip);
        
        // if failed, show error and exit
        if (!success) {
        	Toast.makeText(this, connection.getErrMsg(), Toast.LENGTH_SHORT).show();
        	
        	finish();
        	return;
        }

        // create buttons layout with data from server
        LinearLayout linLayout = new LinearLayout(this);        
        linLayout.setOrientation(LinearLayout.VERTICAL);
        
        Iterator<String> it = connection.getButLabels().iterator();
        byte i = 0;
        while (it.hasNext()) {
        	String label = it.next();
            
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
    	        	boolean success = connection.sendButClick(butNum);
    	        	if (!success) {
    	        		finish();
    	        	}
    	        }
            	
            	private byte butNum;
            };
            
            button.setOnClickListener(new ButtonClickListener(i) {
    	    });
            
            if (i==255)
            	break;
            i++;
        }
        setContentView(linLayout); // set the layout
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	connection.close(); // close the connection
    }
    
    private Connection connection;
}