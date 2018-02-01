import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ButtonModel;

public class ClientController implements ActionListener{

	private ClientGUI clientGui;
	private Socket clientSocket;
	private Scanner clientScanner;
	private AtomicBoolean serverResponding ;
	
	
	public ClientController(ClientGUI clientGui) {
		this.clientGui = clientGui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() != null && e.getActionCommand() == "Connect") {
			if (clientSocket != null && !clientSocket.isClosed()) {
				writeToGui("Already connect to:"+ "\nHOST: " +clientSocket.getInetAddress() + 
						"\nPORT: " + clientSocket.getPort() + "\nPlease Disconnect First and try again.");
				return;
			}
			int portNum;
			try {
				portNum = Integer.parseInt(clientGui.getPortField().getText());
			} catch(NumberFormatException e1) {
				writeToGui("Invalid Port Number. Please make sure it is an Integer.");
				return;
			}
			InetAddress ipAdd = null;
			try {
				ipAdd = InetAddress.getByName(clientGui.getIpField().getText());
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				writeToGui("Invalid IP Address, Unknown Host.");
				return;
			}
			connect(ipAdd,portNum);
		} else if(e.getActionCommand() == "Disconnect") {
			disconnect();
		} else if(e.getActionCommand() == "Send") {
			if(clientSocket != null && serverResponding.get()) {
				send();
			} else {
				writeToGui("You aren't connected to any server.");
			}
			
			
		}
		
	}
	
	private void connect(InetAddress serverAddress, int serverPort) {
		try {
			clientSocket = new Socket(serverAddress, serverPort);
			clientScanner = new Scanner(System.in);
			serverResponding = new AtomicBoolean(true);
		} catch(Exception e) {
			writeToGui("Unable to make a connection with the server. Error: " + e.getMessage());
			return;
		}
        Scanner serverResponseScanner = null;
		try {
			serverResponseScanner = new Scanner(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Thread clientFromServer = new Thread(new ClientFromServer(this, serverResponseScanner, serverResponding));
        
        clientFromServer.start();
	}
	
	public void disconnect() {
		if(clientSocket != null && !clientSocket.isClosed()) {
			try {
				clientSocket.close();
				writeToGui("Connection closed.");
			} catch (IOException e1) {
				writeToGui("Unable to close the socket. Error : " + e1.getMessage());
			}
			
		}else {
			writeToGui("Connection already closed.");
		}
		return;
	}
	
	private void send() {
		String operation = clientGui.getOpButtons().getSelection().getActionCommand();
		Thread clientToServer = new Thread(new ClientToServer(this, operation, getAttributes(), clientSocket, serverResponding));
        clientToServer.start();
	}
	
	private HashMap<String,String> getAttributes(){
		HashMap<String,String> attributes = new HashMap<>();
		attributes.put("ISBN", clientGui.getIsbnField().getText());
		attributes.put("TITLE", clientGui.getTitleField().getText());
		attributes.put("AUTHOR", clientGui.getAuthorField().getText());
		attributes.put("PUBLISHER", clientGui.getPublisherField().getText());
		attributes.put("YEAR", clientGui.getYearField().getText());
		return attributes;
	}
	
	public void writeToGui(String text) {
		clientGui.getServerTextArea().setText(text);
	}
	
	
	

}
