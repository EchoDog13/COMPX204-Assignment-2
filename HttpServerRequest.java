/**
 * HttpServerRequest to handle the request and process the request details
 */
class HttpServerRequest {
    // private string to store request type
    private String requestType = null;
    // private string to store file requested
    private String file = null;
    // private string to store host
    private String host = null;
    // private boolean to check if request is done
    private boolean done = false;
    private int line = 0;

    /**
     * Check if the request is done
     * 
     * @return boolean true if request is done, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Get the request type
     * 
     * @return String request type
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Get the file requested
     * 
     * @return String file requested
     */
    public String getFile() {
        return file;
    }

    /**
     * Get the host
     * 
     * @return String host
     */
    public String getHost() {
        return host;
    }

    /**
     * Process the request
     * 
     * @param in String input to process
     */
    public void process(String in) {

        if (in == null || in.isEmpty()) {
            done = true;
            return;
        }

        if (line == 0 && in.startsWith("GET") || in.startsWith("POST")) {
            // Extract request type and file (requested resource)
            String[] parts = in.split(" ");
            requestType = parts[0];
            // Defaults to index.html if no file is specified
            file = parts[1].equals("/") ? "index.html" : parts[1].substring(1);
        }

        // Extract host by removing the "Host: " prefix
        if (in.startsWith("Host: ")) {
            // Extract host
            host = in.substring(6).trim();
        }
        line++;
    }
}