import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Date;

public class ClientAcceptor implements Runnable {

    private Server server;
    private Socket socket;

    public Thread thread;

    /**
     * Constructeur avec 1 argument
     *
     * @param server : serveur ou on veut accecpter un client
     */
    public ClientAcceptor(Server server) {
        this.server = server;
    }

    /**
     * Méthode invoquée lorsque l'on instancie un ClientAcceptor permettant de gérer la connexion au serveur
     */
    @Override
    public void run() {

        try {
            while (true) {
                this.socket = this.server.getServerSocket().accept();

                BufferedReader inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                PrintWriter outputStream = new PrintWriter(this.socket.getOutputStream());

                String userName = new String(Base64.getDecoder().decode(inputStream.readLine()));
                Integer userID = Server.clientsMap.size() + 1;

                Server.clientsMap.put(userID, userName);
                Server.socketsMap.put(userID, this.socket);

                // On écrit dans le fichier log
                String logMessage = "[Connection] Pseudo : " + userName
                        + " \n\tSocket : " + this.socket
                        + " \n\tTime : " + new Date(System.currentTimeMillis());

                System.out.println(logMessage);

                BufferedWriter logWriter = new BufferedWriter(new FileWriter(Server.logFile));
                logWriter.append(logMessage);
                logWriter.newLine();
                logWriter.close();

                String message = userName + " s'est connecté.";
                outputStream.println(new String(Base64.getEncoder().encode(message.getBytes())));
                outputStream.flush();

                // Envoie message de connection aux autres
                for (Socket clientSocket : Server.socketsMap.values()) {
                    if (clientSocket != this.socket) {
                        PrintWriter clientStream = new PrintWriter(clientSocket.getOutputStream());
                        clientStream.println(new String(Base64.getEncoder().encode(message.getBytes())));
                        clientStream.flush();
                    }
                }
                this.thread = new Thread(new Chat(userID, this.socket));
                this.thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
