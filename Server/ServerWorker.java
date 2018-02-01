import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ServerWorker implements Runnable{
	private Scanner serverInput;
	private PrintWriter serverOut;
	private Socket clientSocket;
	private ConcurrentHashMap<Long, HashMap<String,String>> booklist;


	public ServerWorker(Socket clientSocket, ConcurrentHashMap<Long, HashMap<String,String>> booklist) {
		this.clientSocket = clientSocket;
		this.booklist = booklist;
	}

	@Override
	public void run() {
		try {
			serverOut = new PrintWriter(clientSocket.getOutputStream(), false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			serverInput = new Scanner(clientSocket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ServerOperations serverOperation = new ServerOperations(serverInput, serverOut);

		System.out.println("Connection from " + clientSocket.getInetAddress().getHostAddress());
		serverOperation.sendToClient("Connection made. HOST: " + clientSocket.getLocalAddress().getHostName() + " PORT: " + clientSocket.getLocalPort());

		String message;
		StringBuilder response = new StringBuilder();
		int confirmation = 0;

		while (serverInput.hasNextLine() && !(message = serverInput.nextLine()).isEmpty() ) {
        		if(message.equals("SUBMIT")) {
        			System.out.println("Handling SUBMIT from: " + clientSocket.getInetAddress().getHostAddress());
        			response = serverOperation.submit(booklist);
							confirmation = 0;
        		}
        		else if(message.equals("GET")) {
        			System.out.println("Handling GET from: " + clientSocket.getInetAddress().getHostAddress());
        			response = serverOperation.get(booklist);
							confirmation = 0;
        		}
        		else if(message.equals("REMOVE")) {
        			System.out.println("Handling REMOVE from: " + clientSocket.getInetAddress().getHostAddress());
        			response = serverOperation.remove(booklist, confirmation);
							confirmation = 1;
							if (response.toString().startsWith("Removed: ")){
								confirmation = 0;
							}

        		}
        		else if(message.equals("UPDATE")) {
        			System.out.println("Handling UPDATE from: " + clientSocket.getInetAddress().getHostAddress());
        			response = serverOperation.update(booklist);

							confirmation = 0;
        		}else{
							confirmation = 0;
						}

        		if(!response.toString().isEmpty()) {
        			serverOperation.sendToClient(response.toString());
        			response.setLength(0);
        		}

        }

		System.out.println("Ending Connection to: " + clientSocket.getInetAddress().getHostAddress());
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
