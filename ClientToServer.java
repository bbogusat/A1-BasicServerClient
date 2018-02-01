import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientToServer implements Runnable{
	
	private ClientController controller;
	private String operation;
	private HashMap<String, String> attributes;
	private Socket clientSocket;
	private AtomicBoolean serverResponding;
	
	public ClientToServer(ClientController controller, String operation, HashMap<String, String> attributes, Socket clientSocket, AtomicBoolean serverResponding) {
		this.controller = controller;
		this.operation = operation;
		this.attributes = attributes;
		this.clientSocket = clientSocket;
		this.serverResponding = serverResponding;
	}

	@Override
	public void run() {
		if(serverResponding.get() && !operation.isEmpty()) {
			
			PrintWriter out = null;
			try {
				out = new PrintWriter(this.clientSocket.getOutputStream(), false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(operation.equals("GET\nALL")) {
				out.println(operation);
				out.flush();
			}else {
				out.println(operation);
				
				HashMap<String,String> cleanAttributes = cleanAttributes(attributes);
				if(!cleanAttributes.isEmpty() && validISBN(cleanAttributes)) {
					for(Entry<String, String> attribute : cleanAttributes.entrySet()) {
						out.println(attribute.getKey() + " " + attribute.getValue());
					}
					out.println("");
					out.flush();
				}
			} 
			
			//Send Operator to the server with an enter
			
			// ISBN is needed on the SUBMIT
			// Run through attributes to clean them (Remove any double spaces, and check if they exist)
			// Return a list of attributes that are there and cleaned
			// If operation is GET ALL - don't check attributes
		}
//		if(serverResponding.get() && !(text = textField.getText().trim()).isEmpty()) {
//			text = text.replaceAll("[\r\n]+", "\n");
//			PrintWriter out = null;
//			try {
//				out = new PrintWriter(this.clientSocket.getOutputStream(), false);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			out.println(text);
//			
//			out.flush();
//			String[] operators = {"SUBMIT\n","REMOVE\n","UPDATE\n","GET\n"};
//			boolean termination = false;
//			
//			for (String operator : operators) {
//				if(text.startsWith(operator)) {
//					termination = true;
//				}
//			}
//			
//			if(!text.equals("GET\nALL") && termination) {
//				out.println("");
//				out.flush();
//			}
//		}
	}
	
	private HashMap<String,String> cleanAttributes(HashMap<String,String> attributes){
		HashMap<String,String> cleanAttributes = new HashMap<String,String>();
		String newAttribute;
		for(Entry<String, String> attribute : attributes.entrySet()) {
			if(attribute.getValue() != null && !attribute.getValue().trim().isEmpty()) {
				newAttribute = attribute.getValue().trim().replaceAll("[\r\n]+", "\n");
				cleanAttributes.put(attribute.getKey(), newAttribute);
			}
		}
		if(cleanAttributes.size() == 0) {
			controller.writeToGui("No valid attributes found. No request sent.");
		}
		return cleanAttributes;
	}
	
	private boolean validISBN(HashMap<String,String> cleanAttributes) {
		if(cleanAttributes.containsKey("ISBN")) {
			String[] strIsbn = cleanAttributes.get("ISBN").split("");
			int[] arrIsbn = new int[strIsbn.length];
			if (arrIsbn.length != 13) {
				controller.writeToGui("Invalid ISBN.");
				return false;
			}
			for(int i=0; i<strIsbn.length; i++) {
				try {
				arrIsbn[i] = Integer.parseInt(strIsbn[i]);
				} catch (NumberFormatException e) {
					controller.writeToGui("Invalid ISBN.");
					return false;
				}
			}
			
			int sum = 0;
			int checkDigit;
			for(int i=0; i<arrIsbn.length-1;i=i+2) {
				sum += (arrIsbn[i]*1);
			}
			for(int i=1; i<arrIsbn.length-1;i=i+2) {
				sum += (arrIsbn[i]*3);
			}
			checkDigit = 10 - (sum%10);
			checkDigit = (checkDigit == 10) ? 0 : checkDigit;
			if(checkDigit != arrIsbn[arrIsbn.length-1]) {
				controller.writeToGui("Invalid ISBN.");
				return false;
			}
		}
		return true;
	}


}
