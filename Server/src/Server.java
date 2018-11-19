import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private ServerSocket serverSocket;
    private int serverPort;

    public static HashMap<Integer, String> clientsMap = new HashMap<Integer, String>();
    public static HashMap<Integer, Socket> socketsMap = new HashMap<Integer, Socket>();
    public static File logFile = new File("./log.txt");

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public static void main(String[] args) {
        Server s = new Server();
        try {
            if (!Server.logFile.exists()) {
                Server.logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        s.connect(42);
    }

    public void connect(int port) {
        this.serverPort = port;

        try {
            this.serverSocket = new ServerSocket(this.serverPort, 500);
            Thread acceptorThread = new Thread(new ClientAcceptor(this));
            acceptorThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
