package exercise2;

import exercise2.view.Login;
import exercise2.view.PostInterface;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/


//Client which sends users interactions to server and manages the returned messages

public class Client  extends Thread {
    public Registry registry;
    public InetAddress address;
    public ServiceUser userStub;
    private String userIP, userName;
    private int userPort;
    private PostInterface mainView;
    private ArrayList<String> posts;


    //connecting to registry
    public Client(String name, String serverIP, int serverPort, int userPort){
        super();
        try {
            registry = LocateRegistry.getRegistry(serverIP, 1888);
            userStub = (ServiceUser) registry.lookup("server.user");
            address = InetAddress.getLocalHost();
            userIP = address.getHostAddress();
            this.userPort = userPort;
            userName = name;
            posts= new ArrayList<>();
            start();
        } catch (Exception e) {
            System.err.println("Connection error! Wrong server IP!");
			e.printStackTrace();
        }

    }

    //receives data packets from the server, parses them and handles the content
    public void run() {
        DatagramSocket socket = null;
        try {
            // connection establishment
            socket = new DatagramSocket(userPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte buffer[] = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (true) {
            try {
                // waiting for server messages
                socket.receive(packet);

                String message= new String(packet.getData());
                System.out.println("Received message from server: "+message);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                String response;

                //check if nr of subscribers update or post
                if(message.contains("NOS")){
                    int nrOfSubscribers= Integer.valueOf(message.substring(3,4));
                    mainView.updateSubscribers(nrOfSubscribers);
                    response = "Subscriber update received! Sending response..";
                    System.out.println("Received subscriber update from: " + address);
                }else{
                    while(posts.size()>9){
                        posts.remove(0);
                    }
                    posts.add(message);
                    response = "Post received! Sending response..";
                    System.out.println("Received post from: " + address);
                    mainView.updateMessages(posts);

                }

                // Send response to Server
                buffer = null;
                buffer = response.getBytes();
                packet = new DatagramPacket(buffer, buffer.length, address,
                        port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //registers user sending a new account request with user data to the server
    public boolean register(){
        boolean success;
        try {
            success= userStub.createProfile(userName, userIP, userPort);
            if(success){
                System.out.println("Congratulations! You have created an new account!");
                int nrOfSubscribers= userStub.getNrOfSubscribers(userName, userIP, userPort);
                mainView= new PostInterface(userName, nrOfSubscribers , this);
                return true;
            }else{
                System.out.println("Failure! Looks like your name is already used!");
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return true;
        }
    }

    //deletes user's account by sending the corresponding request with user data to the server and quits the application afterwards
    public boolean deleteAccount(){
        boolean success;
        try {
            success= userStub.deleteProfile(userName, userIP, userPort);
            if(success){
                System.out.println("Congratulations! Your account has been successfully deleted!");
                mainView.quitApplication();
                Login login= new Login();
                return true;
            }else{
                System.out.println("Failure! Looks like your account doesn't exist!");
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return true;
        }
    }

    //subscribes to the content of a certain user by sending the user's data and the publishers name to the server. The server
    //than adds the user to the publisher's list of subscribers and sends both a notification about the updates.
    public boolean subscribe(String publisherName){
        boolean success;
        if(publisherName.length()!=0){
            try {
                success= userStub.subscribe(userName, userIP, userPort, publisherName);
                if(success){
                    System.out.println("You've subscribed to "+ publisherName+"'s account!");
                    mainView.showSubscribedNotification();
                }else{
                    System.out.println("Failure! Couldn't subscribe to "+ publisherName+"'s account!");
                    return false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //unfollows the content of a certain user by sending the user's data and the publishers name to the server. The server
    //than removes the user from the publisher's list of subscribers if possible and sends both a notification about the updates.
    public boolean unsubscribe(String publisherName){
        boolean success;
        if(publisherName.length()!=0){
            try {
                success= userStub.unsubscribe(userName, userIP, userPort, publisherName);
                if(success){
                    System.out.println("You've left "+ publisherName+"'s account!");
                }else{
                    System.out.println("Failure! Couldn't leave "+ publisherName+"'s account!");
                    return false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //Gets the inserted post from the GUI and forwards it to the server with the users data which adds the post to his posts and forwards it to all
    // of the registered subscribers
    public boolean post(String post){
        boolean success;
        if(post.length()!=0){
            try {
                success= userStub.post(userName, userIP, userPort, post);
                if(success){
                    System.out.println("Congratulations! You've posted your text!");
                    mainView.showPublishedNotification();
                    while (posts.size()>9){
                        posts.remove(0);
                    }
                    posts.add(post);
                    mainView.updateMessages(posts);
                    return true;
                }else{
                    System.out.println("Failure! Couldn't post the text!");
                    return false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //sends the inserted login data to the server which checks if such an account already exists and updates it's state if so.
    public boolean login(){
        boolean success;
        try{
            success= userStub.login(userName, userIP, userPort);
            if(success){
                System.out.println("Welcome back "+ userName+"!");
                int nrOfSubscribers= userStub.getNrOfSubscribers(userName, userIP, userPort);
                mainView= new PostInterface(userName, nrOfSubscribers , this);
                return true;
            }else{
                System.out.println("Error! Couldn't log in!");
                return false;
            }
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    //sends the user's data to the server which checks if such an account already exists and updates it's state if so.
    public boolean logout(){
        boolean success;
        try{
            success= userStub.logout(userName, userIP, userPort);
            if(success){
                System.out.println("Bye bye "+ userName+"!");
                if(mainView!= null) {
                    mainView.quitApplication();
                }
                return true;
            }else{
                System.out.println("Error! Couldn't log out!");
                return false;
            }
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
}
