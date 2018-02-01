import java.awt.event.ActionEvent;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


public class ClientFromServer implements Runnable{

	private Scanner fromServer;
	private AtomicBoolean serverResponding;
	private ClientController controller;
	
	public ClientFromServer(ClientController controller, Scanner fromServer, AtomicBoolean serverResponding) {
		this.fromServer = fromServer;
		this.serverResponding = serverResponding;
		this.controller = controller;
	}

	@Override
	public void run() {
		String input;
		String message ="";
		int messageSize = 0;
		while(true && serverResponding.get()) {
			try {
				input = fromServer.nextLine();
				if(input.startsWith("SEQUENCESIZE")) {
					messageSize = Integer.parseInt(input.substring(("SEQUENCESIZE").length()+ 1));
				}else {
					message = message.concat(input + "\n");
				}
				
				
				if(messageSize == 0) {
					controller.writeToGui(message);
					message = "";
				}else {
					messageSize--;
				}
				
			} catch(NoSuchElementException e) {
				serverResponding.set(false);
				controller.disconnect();
			}

		}
		
	}
}
