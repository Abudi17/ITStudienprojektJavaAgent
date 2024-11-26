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

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
                ) {
                    System.out.println("Verbunden mit " + clientSocket.getRemoteSocketAddress());

                    String jsonRequest;
                    while ((jsonRequest = in.readLine()) != null) {
                        Gson gson = new Gson();

                        // JSON in GameStatus-Objekt deserialisieren
                        GameStatus gameStatus = gson.fromJson(jsonRequest, GameStatus.class);
                        System.out.println("Empfangener Spielstatus: " + gameStatus);

                        // Abfrage erstellen
                        Map<String, String> queryAttributes = extractAttributesFromGameStatus(gameStatus);

                        // Anfrage an CBREngine senden
                        List<Pair<Instance, Similarity>> results = cbrEngine.retrieveCases(queryAttributes);

                        // Neue Response-Instanz erstellen
                        Response responseHandler = new Response(out);

                        // Antwort formatieren und senden
                        String cbrResponse = responseHandler.formatResponse(results);
                        responseHandler.sendResponse(cbrResponse);
                    }
                } catch (IOException e) {
                    System.out.println("I/O Fehler: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Fehler beim Starten des Servers: " + e.getMessage());
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

        // Beispiel: Extraktion von Attributen (Passe dies an deine tatsächlichen Felder an)
        // attributes.put("Mineralien", String.valueOf(gameStatus.getMinerals()));
        // attributes.put("Attribut2", gameStatus.toString());

        // Füge weitere Attribute hinzu, je nach GameStatus-Feldern
        System.out.println(attributes);
        return attributes;
    }
}
