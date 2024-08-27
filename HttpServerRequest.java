class HttpServerRequest {
    private String requestType = null;
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    public boolean isDone() {
        return done;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getFile() {
        return file;
    }

    public String getHost() {
        return host;
    }

    public void process(String in) {
        // System.out.println("Processing: " + in);

        if (in == null || in.isEmpty()) {
            done = true;
            return;
        }

        if (line == 0 && in.startsWith("GET") || in.startsWith("POST")) {
            // Extract request type and file (requested resource)
            String[] parts = in.split(" ");
            requestType = parts[0];
            file = parts[1].equals("/") ? "index.html" : parts[1].substring(1);
        }

        if (in.startsWith("Host: ")) {
            // Extract host
            host = in.substring(6).trim();
        }

        line++;
    }
}