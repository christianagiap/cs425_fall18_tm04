import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread extends Thread {
	final static int R = 300;
	private Socket socket = null;
	private int clientNumber = 0;
	private String serverIP = null;
	private String clientIP = null;
	private int serverPORT = 0;

	public ClientThread(String[] args, int clientNumber) {
		this.serverIP = args[0];
		this.serverPORT = Integer.parseInt(args[1]);
		this.clientNumber = ++clientNumber;
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			// trim localhost/ from ip address
			this.clientIP = localHost.getHostAddress().trim();
			this.socket = new Socket(this.clientIP, this.serverPORT);
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
			ex.printStackTrace();
		}

		System.out.println("New connection with client# " + clientNumber + " at " + socket);
	}

	public void run() {
		try {
			int i = 0;

			// check if socket is closed
			while (socket.isClosed() == true) {
				socket = new Socket(this.serverIP, this.serverPORT);
			}

			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);

			// create request message with client ip, server port and user id
			String clientRequest = "HELLO " + this.clientIP + " " + this.serverPORT + " " + clientNumber;

			long sumOfLatency = 0;

			// send 300 requests
			while (i < R) {
				// start calculating latency
				long startTime = System.currentTimeMillis();

				// send request to server
				writer.println(clientRequest);

				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));

				// get response from server
				String serverResponse = reader.readLine();

				while (serverResponse != null) {
					// stop getting responses when you read Stop
					if (serverResponse.compareTo("Stop") == 0) {
						break;
					}
					serverResponse = reader.readLine();
				}
				i++;

				// stop calculating latency
				long finishTime = System.currentTimeMillis();
				long latency = finishTime - startTime;
				sumOfLatency += latency;
			}
			// calculate average latency of each user
			double averageLatency = sumOfLatency / R;

			System.out.println("Client #" + this.clientNumber + " closed its connection after " + i + " requests");
			socket.close();
		} catch (IOException ex) {
			System.out.println("Client exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
