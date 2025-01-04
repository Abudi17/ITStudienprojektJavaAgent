package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.PrintWriter;
import java.util.Map;

/**
 * Die Klasse Response ist für das Formatieren und Senden von Antworten verantwortlich.
 * Sie kombiniert Ähnlichkeitswerte und Kategorien, um eine informative Rückmeldung
 * an den Client bereitzustellen.
 */
public class Response {

    /**
     * Writer zum Senden der Antworten an den Client.
     */
    private final PrintWriter out;

    /**
     * Konstruktor für die Response-Klasse.
     *
     * @param out Der PrintWriter, der für das Senden von Antworten an den Client verwendet wird.
     */
    public Response(PrintWriter out) {
        this.out = out;
    }

    /**
     * Formatiert die kombinierte Antwort basierend auf Ähnlichkeitswerten und Kategorien.
     * Gibt die Antwort im JSON-Format zurück.
     *
     * @param similarityResults Map, die Fallnamen mit ihren Ähnlichkeitswerten verbindet.
     * @param categorizedCases  Map, die Fallnamen mit ihren zugehörigen Kategorien verbindet.
     * @return Eine formatierte Antwort als JSON-String.
     */
    public String formatCombinedResponse(Map<String, Double> similarityResults, Map<String, String> categorizedCases) {
        // Erstelle eine JSON-Struktur
        JsonObject jsonResponse = new JsonObject();

        // Top 5 ähnliche Fälle
        JsonObject similarCases = new JsonObject();
        similarityResults.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Absteigend sortieren
                .limit(1) // Maximal 5 Fälle
                .forEach(entry -> {
                    String caseName = entry.getKey();
                    Double similarity = entry.getValue();
                    String category = categorizedCases.getOrDefault(caseName, "Unbekannt");

                    JsonObject caseDetails = new JsonObject();
                    caseDetails.addProperty("name", caseName);
                    caseDetails.addProperty("similarity", similarity);
                    caseDetails.addProperty("category", category);

                    similarCases.add(caseName, caseDetails);
                });

        jsonResponse.add("similar_cases", similarCases);

        // Konvertiere das JSON-Objekt in einen String
        return new Gson().toJson(jsonResponse);
    }

    /**
     * Sendet die formatierte JSON-Antwort an den Client.
     *
     * @param response Die zu sendende JSON-Antwort als String.
     */
    public void sendResponse(String response) {
        out.println(response); // JSON-Antwort an den Client senden
        System.out.println("DEBUG: Gesendete JSON-Antwort: " + response); // Antwort für Debugging auf der Konsole ausgeben
    }
}
