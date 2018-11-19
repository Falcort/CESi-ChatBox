import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Date;

public class Chat implements Runnable {
    private Socket socket;
    private Integer id;
    private BufferedReader inputStream;

    /**
     * Constructeur du tchat
     *
     * @param id   : id de l'utilisateur
     * @param sock : socket de l'utilisateur
     * @throws IOException : exception si le socket est null
     */
    public Chat(Integer id, Socket sock) throws IOException {
        this.socket = sock;
        this.id = id;
        this.inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    /**
     * Méthode permettant d'envoyer un message à tous les clients du tchat
     *
     * @param message : message à envoyer
     * @throws IOException : exception si le socket est null
     */
    public void sendMessageToClients(String message) throws IOException {
        PrintWriter clientStream;
        System.out.println("[Message] " + message);

        for (Socket clientSocket : Server.socketsMap.values()) {
            clientStream = new PrintWriter(clientSocket.getOutputStream());
            clientStream.println(new String(Base64.getEncoder().encode(message.getBytes())));
            clientStream.flush();
        }
    }

    /**
     * Méthode permettant au serveur de lire un message envoyer par un client
     *
     * @param in : BufferedReader endroit on on lit un message (flux d'entr�e)
     * @return Sring : message du client
     * @throws IOException : exception si le socket est null
     */
    public String receiveMessageFromClient(BufferedReader in) throws IOException {
        String msg = new String(Base64.getDecoder().decode(in.readLine().getBytes()));
        String pseudo = Server.clientsMap.get(this.id);

        switch (msg) {
            case "/quit":
                // System.out.println(pseudo + " tente de se deconnecter");

                Server.socketsMap.get(this.id).close();

                String logMessage = "[Deconnection] Pseudo : " + pseudo
                        + " \n\tSocket : " + this.socket
                        + " \n\tTime : " + new Date(System.currentTimeMillis());

                System.out.println(logMessage);

                BufferedWriter logWriter = new BufferedWriter(new FileWriter(Server.logFile));
                logWriter.append(logMessage);
                logWriter.newLine();
                logWriter.close();

                Server.socketsMap.remove(this.id);
                Server.clientsMap.remove(this.id);
                msg = pseudo + " s'est déconnecté";
                break;
            case "/ding":
                msg = "Dong";
                break;
            default:
                msg = pseudo + " : " + msg;
                break;
        }
        return msg;
    }

    /**
     * Méthode invoquée lorsque l'on instance un Chat, elle gère un utilisateur du tchat
     */
    @Override
    public void run() {
        try {
            while (Server.clientsMap.get(this.id) != null) {
                if (inputStream.ready()) {
                    String message = this.receiveMessageFromClient(inputStream);
                    this.sendMessageToClients(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
