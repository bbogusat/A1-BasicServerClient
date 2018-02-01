import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public final class Server {
	
	private ServerSocket serverSocket;
	private ConcurrentHashMap<Long, HashMap<String,String>> booklist;
	private boolean running;
	
	public Server(int portNum) throws Exception {
		this.booklist = new ConcurrentHashMap<>();
		try {
			this.serverSocket = new ServerSocket(portNum);
		} catch(BindException e) {
			throw new BindException("Unable to start server: " + e.getMessage());
		}
		this.running = true;
		
		int port = this.serverSocket.getLocalPort();
		String ipAdd = this.serverSocket.getInetAddress().getHostAddress();
		System.out.println("Server Started. Host: " + ipAdd + " Port: " + port);
	}
	
	private void listen() throws Exception {
		Socket clientSocket = this.serverSocket.accept();
		Thread thread = new Thread(new ServerWorker(clientSocket, booklist));
		thread.start();
	}
	
	public static void main(String[] args) throws Exception{
		//Get port num from the command line
		if(args.length != 1) {
			System.out.println("Invalid Number of args");
			return;
		}
		int port = Integer.parseInt(args[0]);
		try {
			Server myServer = new Server(port);
			while(myServer.running) {
				myServer.listen();
			}
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
		
	}
}
