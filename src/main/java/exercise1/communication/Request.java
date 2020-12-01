package exercise1.communication;

public class Request {
    public final String FUNCTION_NAME;
    public final String[] PARAMETERS;

    public Request(String FUNCTION_NAME, String[] PARAMETERS) {
        this.FUNCTION_NAME = FUNCTION_NAME;
        this.PARAMETERS = PARAMETERS;
    }
}
