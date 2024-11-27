import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cbr_util.CBREngine;
import com.google.gson.*;
import de.dfki.mycbr.util.Pair;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;
import model.GameStatus;
import model.Response;

public class Main {

    public static void main(String[] args) {
        int portNumber = 65432;

        // Singleton-Instanz von CBREngine abrufen
        CBREngine cbrEngine = CBREngine.getInstance();
        cbrEngine.init(); // Projekt wird automatisch mit dem angegebenen Pfad und Konzept geladen

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server gestartet, wartet auf Verbindungen...");

            while (true) {
                // Blockiert, bis eine neue Verbindung akzeptiert wird
                Socket clientSocket = serverSocket.accept();
                System.out.println("Verbunden mit " + clientSocket.getRemoteSocketAddress());

                // Jeder Client wird in einem neuen Thread behandelt
                new Thread(() -> handleClient(clientSocket, cbrEngine)).start();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Starten des Servers: " + e.getMessage());
        }
    }

    /**
     * Verarbeitet die Kommunikation mit einem einzelnen Client.
     *
     * @param clientSocket Die Socket-Verbindung zum Client.
     * @param cbrEngine    Die CBREngine-Instanz zur Verarbeitung von Anfragen.
     */
    private static void handleClient(Socket clientSocket, CBREngine cbrEngine) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            Gson gson = new Gson(); // Gson-Instanz für die JSON-Verarbeitung
            String jsonRequest;

            while ((jsonRequest = in.readLine()) != null) {
                System.out.println("Empfangene Anfrage: " + jsonRequest);

                try {
                    // Versuche, die Anfrage in ein GameStatus-Objekt zu deserialisieren
                    GameStatus gameStatus = gson.fromJson(jsonRequest, GameStatus.class);
                    System.out.println("Empfangener Spielstatus: " + gameStatus);

                    // Abfrage der CBR-Engine mit den Attributen aus dem GameStatus
                    Map<String, String> queryAttributes = extractAttributesFromGameStatus(gameStatus);

                    List<Pair<Instance, Similarity>> results = cbrEngine.retrieveCases(queryAttributes);

                    // Response-Handler erstellen, um die Antwort zu formatieren und zurückzusenden
                    Response responseHandler = new Response(out);
                    String cbrResponse = responseHandler.formatResponse(results);
                    responseHandler.sendResponse(cbrResponse);

                } catch (JsonSyntaxException e) {
                    // Fehler, falls die JSON-Anfrage ungültig ist
                    System.err.println("Fehlerhafte JSON-Anfrage: " + jsonRequest);
                    out.println("Ungültige Anfrage: Überprüfen Sie die JSON-Daten.");
                }
            }
        } catch (IOException e) {
            System.out.println("I/O Fehler bei " + clientSocket.getRemoteSocketAddress() + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Verbindung mit " + clientSocket.getRemoteSocketAddress() + " geschlossen.");
            } catch (IOException e) {
                System.out.println("Fehler beim Schließen des Sockets: " + e.getMessage());
            }
        }
    }


    /**
     * Extrahiert die Attribut-Werte-Paare aus dem GameStatus-Objekt.
     *
     * @param gameStatus Der empfangene Spielstatus.
     * @return Map mit Attributnamen und -werten.
     */
    private static Map<String, String> extractAttributesFromGameStatus(GameStatus gameStatus) {
        Map<String, String> attributes = new HashMap<>();

        // Extrahiert die Attribute und validiert sie
        attributes.put("Arbeiter", validateAttribute(gameStatus.getWorkers()));
        attributes.put("Freie Arbeiter", validateAttribute(gameStatus.getIdleWorkers()));
        attributes.put("Mineralien", validateAttribute(gameStatus.getMinerals()));
        attributes.put("Gas", validateAttribute(gameStatus.getGas()));
        attributes.put("Kanonen", validateAttribute(gameStatus.getCannons()));
        attributes.put("Pylons", validateAttribute(gameStatus.getPylons()));
        attributes.put("Nexus", validateAttribute(gameStatus.getNexus()));
        attributes.put("Gateway", validateAttribute(gameStatus.getGateways()));
        attributes.put("CyberneticsCores", validateAttribute(gameStatus.getCyberneticsCores()));
        attributes.put("Stargates", validateAttribute(gameStatus.getStargates()));
        attributes.put("Voidrays", validateAttribute(gameStatus.getVoidrays()));
        attributes.put("SupplyUsed", validateAttribute(gameStatus.getSupplyUsed()));
        attributes.put("SupplyCap", validateAttribute(gameStatus.getSupplyCap()));

        // Weitere Attribute können hier hinzugefügt werden, je nach den Feldern im GameStatus

        // Ausgabe der extrahierten Attribute für Debugging-Zwecke
        System.out.println("Extrahierte Attribute: " + attributes);

        return attributes;
    }

    /**
     * Validiert das Attribut, indem null-Werte durch "0" ersetzt werden.
     *
     * @param value Das zu validierende Attribut.
     * @return Der validierte Attributwert als String.
     */
    private static String validateAttribute(Object value) {
        return value != null ? String.valueOf(value) : "0"; // Wenn der Wert null ist, setze ihn auf "0"
    }
}
