package exercise2;


import exercise2.model.Post;
import exercise2.model.User;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/

public class Server extends Thread implements ServiceUser {

    String ip;
    private static final int REGISTRYPORTNR=1888;
    Storage storage;

    //gets host's ip address and starts server
    protected Server() throws RemoteException {
        super();
        storage= new Storage();

        try{
            ip= InetAddress.getLocalHost().getHostAddress();
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
        start();
    }

    //creates registry with (in this case fixed port number and binds the server instance to it
    public static void main(String[] args){
        try {

            Registry registry = LocateRegistry.createRegistry(REGISTRYPORTNR);
            Server server = new Server();
            ServiceUser userInterface= (ServiceUser) UnicastRemoteObject.exportObject(server, 0);
            System.setProperty("java.rmi.server.hostname","192.168.101.174");
            registry.bind("server.user", userInterface);
            System.err.println("Server ready: " + server.ip);
        } catch (Exception e) {

            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();

        }

    }

    //saves incoming posts, forwards it to the given publisher's subscribers and waits for response (propagation method below).
    @Override
    public boolean post(String name, String ip, int port, String text) throws RemoteException {
        if(text== null|| text.length()==0){
            System.out.println("Can't post empty text!");
            return false;
        }

        User author= storage.getUser(name, ip, port);

        if(author== null){
            System.out.println("Invalid user data! Couldn't post.");
            return false;
        }else {
            Post post = new Post(author, text);
            author.addPost(post);
            propagate(post, author.getSubscribers());
            return true;
        }
    }

    private boolean propagate(Post post, ArrayList<User> subscribers){
        int viewerCount=0;
        for(User u: subscribers){
            if(u.isOnline()){
                try {
                    // Preparation for propagation
                    byte message[] = new byte[1024];
                    message = post.getContent().getBytes();
                    String username = u.getIpAddress();
                    InetAddress address = InetAddress.getByName(u.getIpAddress());

                    // Sending post to subscriber
                    DatagramSocket socket = new DatagramSocket();
                    DatagramPacket packet = new DatagramPacket(message, message.length,
                            address, u.getPortNumber());
                    socket.send(packet);

                    // Waiting for response from client
                    message = new byte[1024];
                    packet = new DatagramPacket(message, message.length);
                    socket.receive(packet);
                    String response = new String(packet.getData());

                    System.out.println("Server: Received propagation response from subscriber: " + username + ":"
                            + u.getPortNumber() + " confirm " + response);
                    viewerCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        System.out.println("Post sent to" + String.valueOf(viewerCount) + " subscribers.");
        return true;
    }

    //gets users data and adds him to the requested publishers subscriber list if possible
    @Override
    public boolean subscribe(String name, String ip, int port, String publisherName) throws RemoteException {
        return updateSubscribers(name, ip, port, publisherName, false);
    }

    //gets users data and removes him from the requested publishers subscriber list if possible
    @Override
    public boolean unsubscribe(String name, String ip, int port, String publisherName) throws RemoteException {
        return updateSubscribers(name, ip, port, publisherName, true);
    }

    //furthermore: updates both, subscriber and publisher (via nr of subscribers) about the changes.
    private boolean updateSubscribers(String name, String ip, int port, String publisherName, boolean delete){
        User publisher= storage.getUserByName(publisherName);
        User subscriber= storage.getUser(name, ip, port);
        if(publisher== null){
            System.out.println("Couldn't not find publisher!");
            return false;
        }else if(subscriber== null){
            System.out.println("Could not find subscribers account!");
            return false;
        }else{
            if(!delete) {
                publisher.addSubscriber(subscriber);
            }else{
                publisher.deleteSubscriber(subscriber);
            }

            if(publisher.isOnline()) {
                try {
                    // Preparation for propagation
                    byte message[] = new byte[1024];
                    String subscriberupdate = "NOS" + String.valueOf(publisher.getSubscribers().size());
                    message = subscriberupdate.getBytes();
                    String username = publisher.getUsername();
                    System.out.println("Server: Notifying publisher about subscriber changes: " + username);
                    InetAddress address = InetAddress.getByName(publisher.getIpAddress());

                    // Sending post to subscriber
                    DatagramSocket socket = new DatagramSocket();
                    DatagramPacket packet = new DatagramPacket(message, message.length,
                            address, publisher.getPortNumber());
                    socket.send(packet);

                    // Waiting for response from client
                    message = new byte[1024];
                    packet = new DatagramPacket(message, message.length);
                    socket.receive(packet);
                    String response = new String(packet.getData());

                    System.out.println("Server: Received subscriber update response from publisher: " + username + ": " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
    }

    //checks the list of accounts for given data and updates state if found.
    @Override
    public boolean login(String name, String ip, int port) throws RemoteException {
        User user= storage.getUser(name, ip, port);
        if(user== null){
            System.out.println("Could not find related account!");
            return false;
        }else{
            user.setOnline(true);
            return true;
        }
    }

    //checks list of accounts for given data and updates users state if found
    @Override
    public boolean logout(String name, String ip, int port) throws RemoteException {
        User user= storage.getUser(name, ip, port);
        if(user== null){
            System.out.println("Could not find related account!");
            return false;
        }else{
            user.setOnline(false);
            return true;
        }
    }

    //checks if there is already an account with such data and creates one otherwise
    @Override
    public boolean createProfile(String name, String ip, int port) throws RemoteException{
        System.out.println("Server: Creating account...");
        return storage.addUser(name, ip, port);
    }

    //checks if account with given data exists and deletes it if so
    @Override
    public boolean deleteProfile(String name, String ip, int port) throws RemoteException {
        System.out.println("Server: Deleting account...");
        return storage.deleteUser(name, ip, port);
    }

    //returns the number of subscribers of the given users if found
    @Override
    public int getNrOfSubscribers(String name, String ip, int port){
        User user= storage.getUser(name, ip, port);
        if(user!=null) {
            int nrOfSubscribers= user.getSubscribers().size();
            System.out.println("Nr of subscribers of "+ user.getUsername()+ ": "+ String.valueOf(nrOfSubscribers));
            return nrOfSubscribers;
        }else{
            System.out.println("No such user found!");
            return 0;
        }
    }

}