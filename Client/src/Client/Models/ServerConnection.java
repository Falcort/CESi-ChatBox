package Client.Models;

import Client.Controllers.ChatManager;
import Client.Views.ConnectionWindow;

import javax.swing.*;
import java.beans.Encoder;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;

public class ServerConnection {
    private String pseudo;
    private Socket socket;
    private int port;
    private String server_address;
    private boolean connected;

    public ServerConnection(int port, String server_address, String pseudo) {
        this.pseudo = pseudo;
        this.server_address = server_address;
        this.port = port;
    }

    public void InitSocket() {
        try {
            this.socket = new Socket(server_address, port);
            this.connected = true;
            SendMessage(this.pseudo);
        } catch (Exception e) {
            ChatManager.ConnectionFailed();
            System.exit(0);
        }
    }

    public void SendMessage(String message) throws IOException {
        if (this.connected == true) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            byte[] encodedBytes = Base64.getEncoder().encode(message.getBytes());
            writer.println(new String(Base64.getEncoder().encode(message.getBytes())));
            writer.flush();
        }
    }

    public void Disconnect() throws IOException {
        this.SendMessage("/quit");
        this.connected = false;
    }

    public boolean GetConnected() {
        return this.connected;
    }

    public String GetMessages() throws IOException {
        while (true) {
            BufferedReader buff = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            if (buff.ready()) {
                return new String(Base64.getDecoder().decode(buff.readLine()));
            }
        }
    }
}
