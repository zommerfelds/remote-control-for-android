package ch.aero.RemoteControl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class Connection {

	private final int PORT = 57891;

	private boolean isConnected;
	private Socket socket;
	private BufferedReader bufIn;
	private BufferedWriter bufOut;

	private List<String> butLabelList;

	private static final String HANDSHAKE_CLIENT = "remote-control handshake client\n";
	private static final String HANDSHAKE_SERVER = "remote-control handshake server\n";

	public Connection() {
		isConnected = false;
		butLabelList = new LinkedList<String>();
	}

	public void open(String ipAddress) throws IOException {
		// check if IP address is of the form "###.###.###.###"
		if (!ipAddress
				.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
			throw new UnknownHostException(
					"Invalid IP address format! Must be: [0-255].[0-255].[0-255].[0-255]");
		}

		socket = new Socket();
		socket.connect(new InetSocketAddress(ipAddress, PORT), 2000); // open
																		// socket
		bufIn = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
		bufOut = new BufferedWriter(new OutputStreamWriter(socket
				.getOutputStream()));

		isConnected = true; // we are now connected

		try {
			write(HANDSHAKE_CLIENT);
			char[] handShakeResponse = new char[HANDSHAKE_SERVER.length()];
			bufIn.read(handShakeResponse);
			if (!String.valueOf(handShakeResponse).equals(HANDSHAKE_SERVER)) {
				throw new IOException("Wrong handshake");
			}

			// get button list
			while (true) {
				String butName = "";
				butName = bufIn.readLine();
				if (butName.equals("")) // stop if label is ""
					break;
				butLabelList.add(butName);
			}
		} catch (IOException e) {
			close();
			throw e;
		}
	}

	// close connection
	public void close() throws IOException {
		if (!isConnected)
			return; // only close if it's open

		bufIn.close();
		bufOut.close();
		socket.close();
	}

	// get the button labels we got from the server
	public List<String> getButLabels() {
		return butLabelList;
	}

	// send a click to the server
	public void sendButClick(byte butNum) throws IOException {
		if (!isConnected) {
			throw new IllegalStateException("Client is not connected!");
		}

		write(new byte[] { butNum });
	}

	private void write(byte[] buffer) throws IOException {
		socket.getOutputStream().write(buffer); // send the but number
		socket.getOutputStream().flush();
	}

	private void write(String str) throws IOException {
		bufOut.write(str);
		bufOut.flush();
	}
}
