import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import cbr_util.CBREngine;
import com.google.gson.*;
import model.GameStatus;
import model.Response;

/**
 * Hauptklasse, die als Server für die Verarbeitung von CBR-Anfragen dient.
 * Die Klasse akzeptiert Verbindungen von Clients, verarbeitet JSON-Anfragen und liefert die passenden Ergebnisse.
 */
public class Main {

    public static void main(String[] args) {
        int portNumber = 65432;

        // Singleton-Instanz von CBREngine abrufen und initialisieren
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
            System.err.println("Fehler beim Starten des Servers: " + e.getMessage());
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
                    // JSON-Anfrage in GameStatus-Objekt umwandeln
                    GameStatus gameStatus = gson.fromJson(jsonRequest, GameStatus.class);

                    // Abfrage-Attribute extrahieren
                    Map<String, String> queryAttributes = extractAttributesFromGameStatus(gameStatus);

                    // Fälle abrufen und kategorisieren
                    Map<String, String> categorizedCases = cbrEngine.retrieveAndCategorizeCases(queryAttributes);

                    // Antwort formatieren und senden
                    Response responseHandler = new Response(out);
                    responseHandler.sendResponse(formatCategorizedCases(categorizedCases));

                } catch (JsonSyntaxException e) {
                    System.err.println("Fehlerhafte JSON-Anfrage: " + jsonRequest);
                    out.println("Ungültige Anfrage: Überprüfen Sie die JSON-Daten.");
                }
            }
        } catch (IOException e) {
            System.err.println("I/O Fehler bei " + clientSocket.getRemoteSocketAddress() + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Verbindung mit " + clientSocket.getRemoteSocketAddress() + " geschlossen.");
            } catch (IOException e) {
                System.err.println("Fehler beim Schließen des Sockets: " + e.getMessage());
            }
        }
    }

    /**
     * Formatiert die kategorisierten Fälle in eine benutzerfreundliche Zeichenkette.
     *
     * @param categorizedCases Map mit Fallnamen und ihren Kategorien.
     * @return Eine formatierte Zeichenkette mit den Fällen und ihren Kategorien.
     */
    private static String formatCategorizedCases(Map<String, String> categorizedCases) {
        StringBuilder responseBuilder = new StringBuilder("Ähnlichste Fälle mit Kategorien:\n");
        for (Map.Entry<String, String> entry : categorizedCases.entrySet()) {
            responseBuilder.append("Fall: ").append(entry.getKey())
                    .append(", Kategorie: ").append(entry.getValue())
                    .append("\n");
        }
        return responseBuilder.toString();
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
