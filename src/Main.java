import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.stream.Collectors;

import cbr_util.CBREngine;
import com.google.gson.*;
import de.dfki.mycbr.util.Pair;
import model.GameStatus;
import model.Request;
import model.Response;
import util.GameStatusProcessor;

/**
 * Die Main-Klasse implementiert einen Server, der Client-Anfragen über Sockets entgegennimmt.
 * Der Server verarbeitet JSON-basierte Anfragen, extrahiert relevante Spielstatusinformationen,
 * und gibt ähnliche Fälle zurück, die auf einem CBR (Case-Based Reasoning)-System basieren.
 */
public class Main {

    /**
     * Einstiegspunkt der Anwendung. Startet den Server und akzeptiert Verbindungen von Clients.
     *
     * @param args Kommandozeilenargumente (werden hier nicht verwendet).
     */
    public static void main(String[] args) {
        int portNumber = 65432; // Port nummer, auf der der Server lauscht

        System.out.println("INFO: Server wird gestartet...");

        // Initialisierung des CBR-Systems
        CBREngine cbrEngine = CBREngine.getInstance();
        cbrEngine.init();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("INFO: Server gestartet, wartet auf Verbindungen...");

            // Endlosschleife, um eingehende Client-Verbindungen zu akzeptieren
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Verbindung akzeptieren
                System.out.println("INFO: Verbunden mit " + clientSocket.getRemoteSocketAddress());

                // Jede Client-Verbindung wird in einem neuen Thread verarbeitet
                new Thread(() -> handleClient(clientSocket, cbrEngine)).start();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Fehler beim Starten des Servers: " + e.getMessage());
        }
    }

    /**
     * Verarbeitung eines einzelnen Client-Sockets.
     * Diese Methode liest Client-Anfragen, verarbeitet sie und sendet Antworten zurück.
     *
     * @param clientSocket Der Socket, der die Verbindung zum Client repräsentiert.
     * @param cbrEngine    Die Instanz des CBR-Systems zur Fallabfrage und Verarbeitung.
     */
    private static void handleClient(Socket clientSocket, CBREngine cbrEngine) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Eingangsdaten lesen
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true) // Ausgangsdaten schreiben
        ) {
            System.out.println("INFO: Client-Verbindung wird verarbeitet: " + clientSocket.getRemoteSocketAddress());

            String jsonRequest;
            while ((jsonRequest = in.readLine()) != null) { // Anfrage lesen
                System.out.println("INFO: Empfangene Anfrage: " + jsonRequest);

                try {
                    // JSON-String in eine Request-Instanz umwandeln
                    Request request = Request.fromFlatJson(jsonRequest);

                    if (!request.isValid()) { // Gültigkeit der Anfrage überprüfen
                        System.out.println("WARNING: Ungültige Anfrage: " + jsonRequest);
                        out.println("Ungültige Anfrage: Überprüfen Sie die Daten.");
                        continue;
                    }

                    // Extrahieren des Spielstatus und der Attribut-Werte-Paare
                    GameStatus gameStatus = request.gameStatus();
                    Map<String, String> queryAttributes = GameStatusProcessor.extractAttributes(gameStatus);

                    // Fälle mit Ähnlichkeitswerten abrufen
                    Map<String, Pair<String, Double>> categorizedCasesWithSimilarity = cbrEngine.retrieveAndCategorizeCases(queryAttributes);

                    System.out.println("INFO: Abgerufene Fälle mit Ähnlichkeit: " + categorizedCasesWithSimilarity);

                    // Erstelle die Antwort mit Ähnlichkeitswerten
                    Map<String, Double> similarityResults = categorizedCasesWithSimilarity.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, // Fallname
                                    entry -> entry.getValue().getSecond() // Ähnlichkeitswert
                            ));

                    Map<String, String> categorizedCases = categorizedCasesWithSimilarity.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> entry.getValue().getFirst()
                            ));

                    // Formatieren und Senden der kombinierten Antwort
                    Response responseHandler = new Response(out);
                    String combinedResponse = responseHandler.formatCombinedResponse(similarityResults, categorizedCases);
                    responseHandler.sendResponse(combinedResponse);

                } catch (JsonSyntaxException e) {
                    // Fehlerhafte JSON-Anfragen behandeln
                    System.out.println("ERROR: Fehlerhafte JSON-Anfrage: " + jsonRequest);
                    out.println("Ungültige Anfrage: Überprüfen Sie die JSON-Daten.");
                }
            }
        } catch (IOException e) {
            // Fehler bei der Socket-Kommunikation behandeln
            System.out.println("ERROR: I/O Fehler bei " + clientSocket.getRemoteSocketAddress() + ": " + e.getMessage());
        } finally {
            // Verbindung schließen
            try {
                clientSocket.close();
                System.out.println("INFO: Verbindung mit " + clientSocket.getRemoteSocketAddress() + " geschlossen.");
            } catch (IOException e) {
                System.out.println("ERROR: Fehler beim Schließen des Sockets: " + e.getMessage());
            }
        }
    }
}
