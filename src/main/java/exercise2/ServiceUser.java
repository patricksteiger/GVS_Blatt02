package exercise2;

import java.rmi.Remote;
import java.rmi.RemoteException;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/


// Remote interface for user interaction between client and server

public interface ServiceUser extends Remote {
    boolean subscribe(String name, String ip, int port, String publisherName) throws RemoteException;
    boolean unsubscribe(String name, String ip, int port, String publisherName) throws RemoteException;
    boolean login(String name, String ip, int port) throws RemoteException;
    boolean logout(String name, String ip, int port) throws RemoteException;
    boolean createProfile(String name, String ip, int port) throws RemoteException;
    boolean deleteProfile(String name, String ip, int port) throws RemoteException;
    boolean post(String name, String ip, int port, String text) throws RemoteException;
    int getNrOfSubscribers(String name, String ip, int port) throws RemoteException;
}
