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

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ControllerActivity extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String ip = super.getIntent().getExtras().getString("ch.aero.RemoteControl.Ip");
        
        try {
            echoSocket = new Socket(ip, 57891);
            bufIn = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection.");
            System.exit(1);
        }
        
        List<String> strList = new LinkedList<String>();
        
        while (true) {
        	String butName = "";
			try {
				butName = bufIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			if (butName.equals(""))
				break;
			strList.add(butName);
        }

        LinearLayout linLayout = new LinearLayout(this);        
        linLayout.setOrientation(LinearLayout.VERTICAL);
        
        Iterator<String> it = strList.iterator();
        byte i = 0;
        while (it.hasNext()) {
        	String label = it.next();
            
            Button button = new Button(this);
            button.setText(label);
            linLayout.addView(button);
            
            class ButtonClickListener implements View.OnClickListener {
            	ButtonClickListener(byte butNum) {
            		this.butNum = butNum;
            	}

    	        public void onClick(View view) {
    	        	try {
    					echoSocket.getOutputStream().write(butNum);
    					echoSocket.getOutputStream().flush();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    					System.exit(1);
    				}
    	        }
            	
            	private byte butNum;
            };
            
            button.setOnClickListener(new ButtonClickListener(i) {
    	    });
            i++;
        }
        setContentView(linLayout);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
		try {
	    	bufIn.close();
			echoSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
    }
    
    private Socket echoSocket;
    private BufferedReader bufIn;
}