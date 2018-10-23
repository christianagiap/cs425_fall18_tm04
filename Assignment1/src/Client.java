import java.util.ArrayList;

/**
 * The client program creates and starts 10 users (threads) in each repetition.
 */
public class Client {

	final static int N = 10;

	public static void main(String[] args) {
		if (args.length < 3)
			return;

		int reps = Integer.parseInt(args[2]);
		int l = 0;

		try {
			// counter of repetitions
			while (l < reps) {
				// create an arraylist to add user threads
				ArrayList<ClientThread> users = new ArrayList<ClientThread>();
				for (int i = 0; i < N; i++) {
					// create new client thread
					users.add(new ClientThread(args, i));
					users.get(i).start();
				}
				// wait for thread to die
				for (int i = 0; i < users.size(); i++) {
					users.get(i).join();
				}
				l++;
			}

		} catch (InterruptedException e) {
			System.out.println("InterruptedException: " + e.getMessage());
			e.printStackTrace();
		}

	}
}