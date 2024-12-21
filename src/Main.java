import java.io.*;
import java.net.*;
import java.util.Map;

import cbr_util.CBREngine;
import com.google.gson.*;
import model.GameStatus;
import model.Request;
import model.Response;
import util.GameStatusProcessor;

public class Main {

    public static void main(String[] args) {
        int portNumber = 65432;

        System.out.println("INFO: Server wird gestartet...");

        CBREngine cbrEngine = CBREngine.getInstance();
        cbrEngine.init();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("INFO: Server gestartet, wartet auf Verbindungen...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("INFO: Verbunden mit " + clientSocket.getRemoteSocketAddress());

                new Thread(() -> handleClient(clientSocket, cbrEngine)).start();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Fehler beim Starten des Servers: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket, CBREngine cbrEngine) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            System.out.println("INFO: Client-Verbindung wird verarbeitet: " + clientSocket.getRemoteSocketAddress());

            String jsonRequest;
            while ((jsonRequest = in.readLine()) != null) {
                System.out.println("INFO: Empfangene Anfrage: " + jsonRequest);

                try {
                    Request request = Request.fromFlatJson(jsonRequest);

                    if (!request.isValid()) {
                        System.out.println("WARNING: Ungültige Anfrage: " + jsonRequest);
                        out.println("Ungültige Anfrage: Überprüfen Sie die Daten.");
                        continue;
                    }

                    GameStatus gameStatus = request.gameStatus();
                    Map<String, String> queryAttributes = GameStatusProcessor.extractAttributes(gameStatus);

                    Map<String, String> categorizedCases = cbrEngine.retrieveAndCategorizeCases(queryAttributes);
                    Response responseHandler = new Response(out);
                   responseHandler.sendResponse(formatCategorizedCases(categorizedCases));

                } catch (JsonSyntaxException e) {
                    System.out.println("ERROR: Fehlerhafte JSON-Anfrage: " + jsonRequest);
                    out.println("Ungültige Anfrage: Überprüfen Sie die JSON-Daten.");
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: I/O Fehler bei " + clientSocket.getRemoteSocketAddress() + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("INFO: Verbindung mit " + clientSocket.getRemoteSocketAddress() + " geschlossen.");
            } catch (IOException e) {
                System.out.println("ERROR: Fehler beim Schließen des Sockets: " + e.getMessage());
            }
        }
    }

    private static String formatCategorizedCases(Map<String, String> categorizedCases) {
        StringBuilder responseBuilder = new StringBuilder("INFO: Ähnlichste Fälle mit Kategorien:\n");
        for (Map.Entry<String, String> entry : categorizedCases.entrySet()) {
            responseBuilder.append("Fall: ").append(entry.getKey())
                    .append(", Kategorie: ").append(entry.getValue())
                    .append("\n");
        }
        return responseBuilder.toString();
    }
}
