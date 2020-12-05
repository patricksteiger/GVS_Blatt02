package exercise2.view;


import exercise2.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/


// GUI for the login. The combo of username, ip and port must be unique for a successful registration.

public class Login extends JFrame {
    JButton loginBttn, registerBttn;
    JPanel loginpanel;
    JTextField txuser;
    JLabel username;


    public Login(){
        super("Login");

        loginBttn = new JButton("Login");
        registerBttn = new JButton("Register");
        loginpanel = new JPanel();
        txuser = new JTextField(15);
        getRootPane().setDefaultButton(loginBttn);
        username = new JLabel("Username: ");

        setSize(300,200);
        setLocation(500,280);
        loginpanel.setLayout (null);


        txuser.setBounds(100,30,150,20);
        loginBttn.setBounds(150,100,80,20);
        registerBttn.setBounds(30,100,100,20);
        username.setBounds(20,28,100,20);

        loginpanel.add(loginBttn);
        loginpanel.add(registerBttn);
        loginpanel.add(txuser);
        loginpanel.add(username);

        getContentPane().add(loginpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        loginBttn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    String name = txuser.getText();
                try {
                    InetAddress ip=InetAddress.getLocalHost();
                    String serverIP= ip.getHostAddress();

                    if(name.equals("")){
                        JOptionPane.showMessageDialog(null,"Please insert Username!");
                    }

                    Client client= new Client(name, serverIP, 1888, 1889);
                    boolean success=client.login();
                    if(success){
                        dispose();
                    }
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }

            }
        });

        registerBttn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = txuser.getText();
                try {
                    InetAddress ip=InetAddress.getLocalHost();
                    String serverIP= ip.getHostAddress().toString();

                    if(name.equals("")){
                        JOptionPane.showMessageDialog(null,"Please insert Username!");
                    }

                    Client client= new Client(name, serverIP, 1888, 1889);
                    boolean success=client.register();
                    if(success){
                        dispose();
                    }
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }

            }
        });
    }
}
