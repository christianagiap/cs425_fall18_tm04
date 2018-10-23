import java.io.*;
import java.net.*;

/**
 * This server is multi-threaded. It responds with a welcome message to each
 * client's request.
 */
public class Server {

	public static void main(String[] args) {
		if (args.length < 1)
			return;

		// get server port
		int port = Integer.parseInt(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("Server is listening on port " + port);

			int clientNumber = 1;
			while (true) {
				// accept connection to socket
				Socket socket = serverSocket.accept();

				System.out.println("Client " + clientNumber + " is connected to server.");

				// start server thread
				new ServerThread(socket).start();
				clientNumber++;
			}
		} catch (IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}