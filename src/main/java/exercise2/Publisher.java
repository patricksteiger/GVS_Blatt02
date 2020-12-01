package exercise2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Publisher extends Remote {
    void publish() throws RemoteException;
}
