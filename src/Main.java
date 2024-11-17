import java.io.*;
import java.net.*;

import cbr_util.CBREngine;
import com.google.gson.*;
import model.GameStatus;
import model.Request;
import model.Response;

public class Main {

    public static void main(String[] args) {
        int portNumber = 65432;

        // Instanz von CBREngine erstellen (URL des myCBR-Servers anpassen)
        CBREngine cbrEngine = new CBREngine("http://<mycbr-server-url>");


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
                        System.out.println("Empfangener Spielstatus: " + gameStatus);

// Anfrage an den CBR-Server senden
                        String requestData = gameStatus.toString(); // Wenn nötig, nutze eine Methode im RetrievalHelper zur Formatierung
                        String cbrResponse = cbrEngine.retrieveCase(requestData);

// Verarbeitung der Antwort (optional, falls eine spezifische Logik nötig ist)
// RetrievalHelper.processResponse(cbrResponse);

// Beispielantwort an den Client zurücksenden
                        ResponsePythonAgent response = new ResponsePythonAgent("CBR-Antwort verarbeitet");
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