package exercise1;

import com.google.gson.Gson;
import exercise1.communication.Request;
import exercise1.communication.Response;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Client implements KVStore {
    private final ZContext context;
    private final ZMQ.Socket socket;
    private final Gson gson = new Gson();

    public Client(String address) {
        context = new ZContext();
        this.socket = context.createSocket(SocketType.REQ);
        this.socket.setIPv6(true);
        this.socket.connect(address);
    }

    @Override
    public String put(String key, String value) {
        return exchangeMessages("put", key, value);
    }

    @Override
    public String get(String key) {
        return exchangeMessages("get", key);
    }

    @Override
    public boolean isEmpty() {
        String response = exchangeMessages("isEmpty");
        return Boolean.parseBoolean(response);
    }

    private String exchangeMessages(String functionName, String... parameters) {
        // Send Request object containing functionName and parameters
        Request req = new Request(functionName, parameters);
        String message = gson.toJson(req);
        socket.send(message.getBytes(ZMQ.CHARSET));
        // Receive reply and return response
        byte[] reply = socket.recv();
        String answer = new String(reply, ZMQ.CHARSET);
        Response res = gson.fromJson(answer, Response.class);
        return res.RESPONSE;
    }

}
