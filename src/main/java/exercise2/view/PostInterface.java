package exercise2.view;

import exercise2.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/


//GUI and Actionhandlers of main view

public class PostInterface extends JFrame {
    DefaultListModel<String> dlm;

    JButton subscribeNewBttn, unscribeBttn;
    JButton logoutBttn, deleteAccBttn, postBttn;

    JPanel postsPanel;
    JPanel subscribeNewPanel, postNewPanel;
    JPanel headerPanel;
    JTextField subscribeNewTextField, postTextField;

    JLabel nameLabel, subscribersLabel, subscribeFieldLabel, postFieldLabel;

    JList postsList;

    Client client;

    public PostInterface(String name, int nrOfSubscribers, final Client client){
        super("Home");

        this.client=client;
        nameLabel = new JLabel(name);
        nameLabel.setSize( 100,40);
        subscribersLabel = new JLabel("Subscribers: "+ String.valueOf(nrOfSubscribers));
        postFieldLabel = new JLabel("Insert post:");
        postFieldLabel.setSize(100,40);
        subscribeFieldLabel = new JLabel("Publishers name:");
        subscribeFieldLabel.setSize(100,40);

        postTextField = new JTextField(15);
        subscribeNewTextField = new JTextField(15);

        subscribeNewBttn = new JButton("Subscribe");
        postBttn = new JButton("Post");
        logoutBttn = new JButton("Logout");
        deleteAccBttn = new JButton("Delete");
        unscribeBttn = new JButton("Unfollow");

        subscribeNewBttn.setSize(80,50);
        postBttn.setSize(80,40);
        logoutBttn.setSize(40,40);
        deleteAccBttn.setSize(60,40);
        unscribeBttn.setSize(60,40);

        postsPanel = new JPanel();
        postNewPanel= new JPanel();
        subscribeNewPanel = new JPanel();
        headerPanel = new JPanel();

        postsList= new JList();


        headerPanel.setBounds(0,0, 600,25);
        headerPanel.setLayout(new GridLayout(1,3));
        headerPanel.add(nameLabel);
        headerPanel.add(subscribersLabel);
        headerPanel.add(logoutBttn);
        headerPanel.add(deleteAccBttn);


        subscribeNewPanel.setBounds(0, 275,600,45);
        subscribeNewPanel.add(subscribeFieldLabel);
        subscribeNewPanel.add(subscribeNewTextField);
        subscribeNewPanel.add(subscribeNewBttn);
        subscribeNewPanel.add(unscribeBttn);


        postNewPanel.setBounds(100, 320,400,55);
        postNewPanel.add(postFieldLabel);
        postNewPanel.add(postTextField);
        postNewPanel.add(postBttn);


        setSize(600,400);
        postsPanel.setBounds(0,25, 600, 250);
        postsPanel.add(new JScrollPane(postsList));

        getContentPane().setLayout(null);
        getContentPane().add(headerPanel);
        getContentPane().add(postsPanel);
        getContentPane().add(subscribeNewPanel);
        getContentPane().add(postNewPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        subscribeNewBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(subscribeNewTextField.getText().length()>0){
                    client.subscribe(subscribeNewTextField.getText());
                    subscribeNewTextField.setText("");
                    updateFrame();
                }else{
                    JOptionPane.showMessageDialog(null,"Please insert the name of the publisher!");
                }
            }
        });

        postBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(postTextField.getText().length()>0){
                    client.post(postTextField.getText());
                    postTextField.setText("");
                    updateFrame();
                }else{
                    JOptionPane.showMessageDialog(null,"Please insert the text you want to post!");
                }
            }
        });

        logoutBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.logout();
            }
        });

        deleteAccBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.deleteAccount();
            }
        });

        unscribeBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(subscribeNewTextField.getText().length()>0){
                    client.unsubscribe(subscribeNewTextField.getText());
                }
            }
        });
    }

    public void quitApplication(){
        dispose();
        System.exit(0);
    }

    public void updateSubscribers(int nrOfSubscribers){
        subscribersLabel.setText("Subscribers: "+ String.valueOf(nrOfSubscribers));
        updateFrame();
    }

    private void updateFrame(){
        repaint();
    }

    public void updateMessages(ArrayList<String> posts){
        dlm = new DefaultListModel();

        for(int i=0; i<posts.size()-1; i++){
            dlm.add(i, posts.get(i));
        }
        postsList.setModel(dlm);
        updateFrame();
    }

    public void showPublishedNotification(){
        JOptionPane.showMessageDialog(null,"Congratulations! You published your post!");
    }

    public void showSubscribedNotification(){
        JOptionPane.showMessageDialog(null,"Congratulations! You've successfully subscribed!");
    }
}
