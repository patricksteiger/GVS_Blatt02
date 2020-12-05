package exercise2.model;

import java.util.ArrayList;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/


//model of user being able to be publisher and subscriber at once
public class User {
    private boolean isOnline=true;
    private String username;
    private String ipAddress;
    private int portNumber;
    private ArrayList<Post> posts;
    private ArrayList<User> subscribers;


    public User(String ip, int port, String username){
        this.ipAddress=ip;
        this.portNumber=port;
        this.username=username;
        posts= new ArrayList<>();
        subscribers= new ArrayList<>();
    }


    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public ArrayList<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(ArrayList<User> subscribers) {
        this.subscribers = subscribers;
    }

    public void addSubscriber(User subscriber){if(!subscribers.contains(subscriber))subscribers.add(subscriber);}

    public boolean deleteSubscriber(User subsriber){
        if(subscribers.contains(subsriber)){
            subscribers.remove(subsriber);
            return true;
        }else{
            return false;
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post){posts.add(post);}
}
