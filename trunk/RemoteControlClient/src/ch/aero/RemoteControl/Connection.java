package ch.aero.RemoteControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class Connection {
	public Connection() {
		isConnected = false;
		butLabelList = new LinkedList<String>();
		errMsg = "";
	}
	
	public boolean open(String ipAddress) {
        try {
        	// check if IP address is of the form "###.###.###.###"
        	if ( !ipAddress.matches( "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$" ) ) {
        	    throw new UnknownHostException( "Invalid IP address format! Must be: [0-255].[0-255].[0-255].[0-255]" );
        	}
        	
            echoSocket = new Socket(ipAddress, PORT);         // open socket
            bufIn = new BufferedReader(new InputStreamReader( // for buffered input
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) { // couldn't reach server
        	errMsg = e.getMessage();
        	return false;
        } catch (IOException e) { // IO error
        	errMsg = e.getMessage();
        	return false;
        }
        
        isConnected = true; // we are now connected
        
        // get button list
        while (true) {
        	String butName = "";
			try {
				butName = bufIn.readLine();
			} catch (IOException e) {
				errMsg = e.getMessage();
				close();
				return false;
			}
			if (butName.equals("")) // stop if label is ""
				break;
			butLabelList.add(butName);
        }
        return true;
	}
	
	// close connection
	public void close() {
		if (!isConnected)
			return; // only close if it's open
		try {
	    	bufIn.close();
			echoSocket.close();
		} catch (IOException e) {
			errMsg = e.getMessage();
		}
	}
	
	// get the button labels we got from the server
	public List<String> getButLabels() {
		return butLabelList;
	}
	
	// send a click to the server
	public boolean sendButClick(byte butNum) {
		if (!isConnected) {
			errMsg = "Client is not connected!";
			return false;
		}
		
		try {
			echoSocket.getOutputStream().write(butNum); // send the but number
			echoSocket.getOutputStream().flush();
		} catch (IOException e) {
			errMsg = e.getMessage();
			return false;
		}
		return true;
	}

	public String getErrMsg() {
		return errMsg;
	}
    
	private final int PORT = 57891;
	
	private boolean isConnected;
    private Socket echoSocket;
    private BufferedReader bufIn;
    private String errMsg;
    
    private List<String> butLabelList;
}
