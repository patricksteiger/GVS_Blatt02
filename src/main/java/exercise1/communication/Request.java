package exercise1.communication;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 05.12.2020
 *
 *****************************************************************************/

public class Request {
    public final String FUNCTION_NAME;
    public final String[] PARAMETERS;

    public Request(String FUNCTION_NAME, String[] PARAMETERS) {
        this.FUNCTION_NAME = FUNCTION_NAME;
        this.PARAMETERS = PARAMETERS;
    }
}
