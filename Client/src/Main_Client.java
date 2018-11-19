import Client.Controllers.ChatManager;
import Client.Models.ServerConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class Main_Client {
    public static void main(String[] args) throws IOException, InterruptedException {

        ChatManager chat_manager=new ChatManager();
        chat_manager.Startup();

    }
}

