package exercise2;

import exercise2.view.Login;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

//Hint: this controller will start the whole test situation. As the initial manual configuration of users and data isn't the
// normal way of use, this will open a lot of windows at once (for each user)

/**To test: 1. insert any name. For this test, server ip and port are already defined.
     2. Click on register which opens the main view
    3. You can now test the different functions by switching between the different user consoles.
            Possible interactions: post, un/subscribe (by entering another users name),
                        delete account and log out.

    COMMENT: We decided to merge publisher and subscriber as in most publishing services it is also possible, to be both at once.

    IMPORTANT HINT: sometimes, the server wasn't available fast enough and clients couldn't find the registry.
    In this case, delete the server creation below and start the server manually and wait a few seconds afterwards.
 */

public class TestController {
    public static void main(String [] args){
        InetAddress ip= null;
        try {
            Server server= new Server();

            ip = InetAddress.getLocalHost();
            String serverIP= ip.getHostAddress();
            Client testClient1= new Client("Günther", serverIP, 1888, 1881);
            Client testClient2= new Client("Gisela", serverIP, 1888, 1882);
            Client testClient3= new Client("Jutta", serverIP, 1888, 1883);
            Client testClient4= new Client("Brigitte", serverIP, 1888, 1884);


            testClient1.register();
            testClient2.register();
            testClient3.register();
            testClient4.register();

            testClient2.subscribe("Günther");
            testClient1.subscribe("Gisela");
            testClient3.subscribe("Günther");
            testClient4.subscribe("Günther");

            testClient1.post("Heute habe ich einen neuen Pflanzendünger ausprobiert! Große klasse!");
            testClient2.post("Ich wünsche euch einen tollen Tag!");
            Login login= new Login();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
