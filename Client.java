import java.awt.EventQueue;
import java.awt.event.ActionListener;

public class Client {
	public static void main(String args[]) throws Exception {	
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ClientGUI clientGui = new ClientGUI("Bibliography Handler");
				clientGui.initialize();
				clientGui.setVisible(true);
				ActionListener clientHandler = new ClientController(clientGui);
				clientGui.getBtnConnect().addActionListener(clientHandler);
				clientGui.getBtnSend().addActionListener(clientHandler);
				clientGui.getBtnDisconnect().addActionListener(clientHandler);
			}
		});	


	
		
	}
	
}
