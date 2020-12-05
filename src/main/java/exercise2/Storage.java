package exercise2;


import exercise2.model.User;

import java.util.ArrayList;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/


//Storage used by the server handling users and posts
public class Storage {
    private ArrayList<User> users;

    public Storage(){
        users= new ArrayList<>();
    }

    public boolean addUser(String name, String ip, int port){
        if(getUser(name, ip, port)!= null){
            return false;
        }else{
            User user= new User(ip, port, name);
            users.add(user);
            return true;
        }
    }

    public boolean deleteUser(String name, String ip, int port){
        User userToBeDeleted= getUser(name, ip, port);
        if(userToBeDeleted== null){
            return false;
        }else {
            users.remove(userToBeDeleted);
            return true;
        }
    }

    public User getUser(String name, String ip, int port){
        if(users.size()>0){
            for(User u: users){
                if(u.getUsername().equals(name) && u.getIpAddress().equals(ip)&& u.getPortNumber()==port){
                    return u;
                }
            }
        }
        return null;
    }

    public User getUserByName(String name) {
        if(users.size()>0) {
            for (User u : users) {
                if (name.equals(u.getUsername())) {
                    return u;
                }
            }
        }
        return null;
    }
}
