import java.io.*;
import java.net.*;
import com.google.gson.*;

public class Main {
    public static void main(String[] args) {
        int portNumber = 65432;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server läuft auf Port " + portNumber);

            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                ) {
                    System.out.println("Verbunden mit " + clientSocket.getRemoteSocketAddress());

                    String jsonRequest;
                    while ((jsonRequest = in.readLine()) != null) {
                        System.out.println("Empfangen: " + jsonRequest);

                        Gson gson = new Gson();
                        Request request = gson.fromJson(jsonRequest, Request.class);
                        System.out.println("JSON: " + request);

                        //CBR AUFURF
                        // CBR ANTWORT

                        // Beispiel-Antwort erstellen
                        Response response = new Response("ok");
                        String jsonResponse = gson.toJson(response);

                        // Antwort senden
                        out.println(jsonResponse);
                        System.out.println("Antwort gesendet: " + jsonResponse);
                    }
                } catch (IOException e) {
                    System.out.println("I/O Fehler: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Fehler beim Starten des Servers: " + e.getMessage());
        }
    }
}

class Request {
    private String message;
    private long timestamp;

    public Request(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Request{" +
                "message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

class Response {
    private String status;

    public Response(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}