package exercise1;

import com.google.gson.Gson;
import exercise1.communication.Request;
import exercise1.communication.Response;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 05.12.2020
 *
 *****************************************************************************/

public class Server extends Thread {

    public static final String ADDRESS = "tcp://localhost:5555";

    public void run() {
        try (ZContext context = new ZContext()) {
            // Setup server
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.setIPv6(true);
            socket.bind(ADDRESS);
            Gson gson = new Gson();
            RemoteKVStore store = new RemoteKVStore();
            // Handle requests
            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = socket.recv();
                String msg = new String(reply, ZMQ.CHARSET);
                Request req = gson.fromJson(msg, Request.class);
                executeRequest(req, store, socket, gson);
            }
        } catch (ZMQException e) {
            System.out.println("Closing server...");
        }
    }

    private void executeRequest(Request req, RemoteKVStore store, ZMQ.Socket socket, Gson gson) {
        if ("put".equals(req.FUNCTION_NAME)) {
            String result = store.put(req.PARAMETERS[0], req.PARAMETERS[1]);
            sendMessage(result, socket, gson);
        } else if ("get".equals(req.FUNCTION_NAME)) {
            String result = store.get(req.PARAMETERS[0]);
            sendMessage(result, socket, gson);
        } else if ("isEmpty".equals(req.FUNCTION_NAME)) {
            String result = Boolean.toString(store.isEmpty());
            sendMessage(result, socket, gson);
        } else {
            String error = "Function '" + req.FUNCTION_NAME + "' is not supported!";
            sendMessage(error, socket, gson);
        }
    }

    private void sendMessage(String message, ZMQ.Socket socket, Gson gson) {
        Response res = new Response(message);
        String resJson = gson.toJson(res);
        socket.send(resJson.getBytes(ZMQ.CHARSET));
    }

}
