package model;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
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
     * Die Antwort enthält eine Liste der Top 5 Fälle mit den höchsten Ähnlichkeitswerten sowie
     * die Kategorien aller Fälle.
     *
     * @param similarityResults Map, die Fallnamen mit ihren Ähnlichkeitswerten verbindet.
     * @param categorizedCases  Map, die Fallnamen mit ihren zugehörigen Kategorien verbindet.
     * @return Eine formatierte Antwort als String, die sowohl die Ähnlichkeitsliste
     * als auch die Kategorien enthält.
     */
    public String formatCombinedResponse(Map<String, Double> similarityResults, Map<String, String> categorizedCases) {
        StringBuilder responseBuilder = new StringBuilder();

        // Ähnlichkeitsliste erstellen (Top 5 Fälle)
        responseBuilder.append("INFO: Ähnlichste Fälle:\n");
        Map<String, Double> top5Similarities = new LinkedHashMap<>();
        similarityResults.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Absteigend sortieren
                .limit(5) // Maximal 5 Fälle
                .forEach(entry -> top5Similarities.put(entry.getKey(), entry.getValue())); // Manuell in LinkedHashMap speichern

        top5Similarities.forEach((caseName, similarity) ->
                responseBuilder.append("Fall: ").append(caseName)
                        .append(", Ähnlichkeit: ").append(similarity).append("\n"));

        // Kategorisierte Fälle nur für die Top 5 hinzufügen
        responseBuilder.append("\nINFO: Ähnlichste Fälle mit Kategorien:\n");
        for (String caseName : top5Similarities.keySet()) {
            String category = categorizedCases.getOrDefault(caseName, "Unbekannt");
            responseBuilder.append("Fall: ").append(caseName)
                    .append(", Kategorie: ").append(category).append("\n");
        }

        return responseBuilder.toString();
    }


    /**
     * Sendet die formatierte Antwort an den Client.
     *
     * @param response Die zu sendende Antwort als String.
     */
    public void sendResponse(String response) {
        out.println(response); // Antwort an den Client senden
        System.out.println(response); // Antwort auch auf der Konsole ausgeben (Debugging)
    }
}
