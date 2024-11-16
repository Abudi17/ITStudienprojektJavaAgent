import java.io.*;
import java.net.*;
import com.google.gson.*;
import model.GameStatus;
import model.Request;
import model.Response;

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
                        Gson gson = new Gson();

                        // JSON direkt in GameStatus-Objekt deserialisieren
                        GameStatus gameStatus = gson.fromJson(jsonRequest, GameStatus.class);

                        System.out.println(gameStatus);
                        //jtf.setText(jtf.getText() + "\nExecuting retrieval...2");
                        //Thread.sleep(5000);
                        /*
                        handlePlayerCaseBase(player);
                        //jtf.setText(jtf.getText() + "\nExecuting retrieval...3");

                        Response response = engine.executeRetrieval(request);
                        // CBR ANTWORT

                         */

                        // Beispiel-Antwort erstellen
                        //Response response = new Response();
                       // String jsonResponse = gson.toJson(response);

                        // Antwort senden
                        //out.println(jsonResponse);
                        //System.out.println("Antwort gesendet: " + jsonResponse);
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

class RequestPythonAgent {
    private String message;
    private long timestamp;

    public RequestPythonAgent(String message, long timestamp) {
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

class ResponsePythonAgent {
    private String status;

    public ResponsePythonAgent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}