package exercise1;

public class Main {
    public static  void main(String[] args) {
        Server server = new Server();
        server.start();
        Client client = new Client(Server.ADDRESS);

        boolean empty = client.isEmpty();
        System.out.println("isEmpty()");
        System.out.println("Expected: true");
        System.out.println("Received: " + empty);
        System.out.println();
        System.out.println("put(\"key1\", \"1\")");
        String result = client.put("key1", "1");
        System.out.println("Expected: null");
        System.out.println("Received: " + result);
        System.out.println();
        System.out.println("put(\"key1\", \"2\")");
        result = client.put("key1", "2");
        System.out.println("Expected: 1");
        System.out.println("Received: " + result);
        System.out.println();
        System.out.println("get(\"key1\")");
        result = client.get("key1");
        System.out.println("Expected: 2");
        System.out.println("Received: " + result);
        System.out.println();
        System.out.println("isEmpty()");
        empty = client.isEmpty();
        System.out.println("Expected: false");
        System.out.println("Received: " + empty);
        System.out.println();

        server.interrupt();
    }
}
