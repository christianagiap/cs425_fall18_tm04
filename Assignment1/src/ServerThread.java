import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * This thread is responsible to handle client connection.
 */
public class ServerThread extends Thread {
	private Socket socket;
	private int clientNumber;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	// based on current time get a random integer between 300-2000
	public int calculatePayload() {
		Random rand = new Random(System.currentTimeMillis());
		int payload = (rand.nextInt((2000 - 300) + 1) + 300) * 1024;
		return payload;
	}

	// get the last word of request message (user id)
	public void getUserID(String reqMessage) {
		String c = reqMessage.substring(reqMessage.lastIndexOf(" ") + 1);
		this.clientNumber = Integer.parseInt(c);
	}

	public void run() {
		try {
			// get hello message from client
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// send welcome message to client
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);

			String reqMessage = null;

			// read hello message
			reqMessage = reader.readLine();

			getUserID(reqMessage);

			while (reqMessage != null) {
				getUserID(reqMessage);
				int payload = calculatePayload();

				// construct server response with user id and calculated payload
				String serverResponse = "WELCOME Client " + clientNumber + " " + payload;
				writer.println(serverResponse);
				writer.println("Stop");
				reqMessage = reader.readLine();
			}

			System.out.println("Client #" + this.clientNumber + " closed connection.");
			socket.close();

		} catch (IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}