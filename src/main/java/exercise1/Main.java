package exercise1;

import java.util.Scanner;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 05.12.2020
 *
 *****************************************************************************/

public class Main {
    public static  void main(String[] args) {
        Server server = new Server();
        server.start();
        Client client = new Client(Server.ADDRESS);

        // Loop for manual testing
        System.out.println("Client and Server are running!");
        System.out.println("Use format: 'isEmpty', 'get key' and 'put key value'");
        try {
            Scanner scan = new Scanner(System.in);
            while (scan.hasNextLine()) {
                String[] input = scan.nextLine().split(" ");
                String result = "";
                if ("isEmpty".equalsIgnoreCase(input[0])) {
                    boolean empty = client.isEmpty();
                    result = Boolean.toString(empty);
                } else if ("get".equalsIgnoreCase(input[0])) {
                    result = client.get(input[1]);
                } else if ("put".equalsIgnoreCase(input[0])) {
                    result = client.put(input[1], input[2]);
                } else {
                    break;
                }
                System.out.println("Result: " + result);
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
        } finally {
            System.out.println("Ending application...");
        }

        server.interrupt();
    }
}
