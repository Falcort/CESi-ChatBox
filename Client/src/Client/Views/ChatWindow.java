package Client.Views;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Client.Controllers.ChatManager;

public class ChatWindow {
    private JPanel helper;
    private JPanel panel;
    private JComboBox channels;
    private JButton send;
    private JTextField MessageInput;
    private String message;
    private JLabel pseudo;
    private JPanel avatarHelper;
    private JTextArea chat;
    private JLabel avatar;
    private JButton deconnexionButton;
    private ChatManager chat_manager;
    private JFrame frame;

    public ChatWindow(JFrame frame, ChatManager chat_manager) {
        this.frame = frame;
        this.frame.setContentPane(this.panel);
        this.frame.setDefaultCloseOperation(this.frame.EXIT_ON_CLOSE);
        this.frame.pack();
        this.frame.setVisible(true);
        this.chat_manager=chat_manager;
        this.pseudo.setText(chat_manager.GetPseudo());
        this.frame.getRootPane().setDefaultButton(send);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = MessageInput.getText(); /* cette ligne recup le message tapé */
                if(!message.equals("")){
                    chat_manager.SendMessageToServer(message);
                    MessageInput.setText("");}
            }});
        deconnexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chat_manager.Disconnect();
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
    public void DisplayMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //System.out.println("ChatBox = " + message);
                chat.append(message + "\n");
            }
        });
    }
}
