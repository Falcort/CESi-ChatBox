package Client.Controllers;

import Client.Models.ServerConnection;
import Client.Views.ChatWindow;
import Client.Views.ConnectionWindow;

import javax.swing.*;
import java.io.IOException;

public class ChatManager {
    private ServerConnection server_connection;
    private ChatWindow chat_window;
    private String pseudo;
    private String message;
    private Thread thread_client_socket;

    //public void SendMessageToView(String message){ChatWindow.DisplayMessage(message);}

    public void Connect() throws IOException {
        this.server_connection = new ServerConnection(42, "192.168.9.20", this.pseudo);
        this.server_connection.InitSocket();

        JFrame chat_window_frame = new JFrame("Chat");

        this.chat_window = new ChatWindow(chat_window_frame, this);
        this.thread_client_socket = new Thread(new Runnable() {
            @Override
            public void run() {
                while (server_connection.GetConnected()) {
                    String msg = null;
                    try {
                        msg = server_connection.GetMessages();
                        ;
                        if (!msg.equals(null)) {
                            chat_window.DisplayMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread_client_socket.start();
    }

    public void SendMessageToServer(String message) {
        try {
            this.server_connection.SendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            this.server_connection.Disconnect();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Startup() throws InterruptedException, IOException {
        JFrame connection_window_frame = new JFrame("Connexion");
        ConnectionWindow connection_window = new ConnectionWindow(connection_window_frame, this);
    }

    public void SetPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String GetPseudo() {
        return this.pseudo;
    }

    public static void ConnectionFailed() {
        ConnectionWindow.ConnectionFailed();
    }


}
