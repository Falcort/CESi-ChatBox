package Client.Views;

import Client.Controllers.ChatManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ConnectionWindow {
    private JTextField input;
    private JButton send;
    private JPanel MainPanel;
    private JPanel helper;
    private String pseudo;
    private JFrame frame;
    private ChatManager chat_manager;

    public ConnectionWindow(JFrame frame, ChatManager chat_manager) {
        this.pseudo="";
        this.frame = frame;
        this.frame.setContentPane(this.MainPanel);
        this.frame.setDefaultCloseOperation(this.frame.EXIT_ON_CLOSE);
        this.frame.pack();
        this.frame.setVisible(true);
        this.chat_manager = chat_manager;
        this.frame.getRootPane().setDefaultButton(send);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonCLicked();
            }
        });
    }

    public void buttonCLicked() {
        pseudo = input.getText();
        if (!pseudo.equals("")){
            chat_manager.SetPseudo(pseudo);
            try {
                chat_manager.Connect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            frame.setVisible(false);
            frame.dispose();}
    }

    public static void ConnectionFailed(){
        JOptionPane.showMessageDialog(null,"Impossible de se connecter au serveur","Connexion impossible",JOptionPane.ERROR_MESSAGE);
    }

}







