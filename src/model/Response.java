package model;

import de.dfki.mycbr.util.Pair;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;

public class Response {

    private final PrintWriter out;

    public Response(PrintWriter out) {
        this.out = out;
    }

    /**
     * Formatiert die Antwort basierend auf den CBR-Ergebnissen.
     * Gibt nur die Top 5 ähnlichsten Fälle zurück.
     *
     * @param results Liste von CBR-Ergebnissen (Instance-Similarity-Paare).
     * @return Die formatierte Antwort als String.
     */
    public String formatResponse(List<Pair<Instance, Similarity>> results) {
        // Sortiere die Ergebnisse nach Ähnlichkeit absteigend
        results.sort(Comparator.comparingDouble((Pair<Instance, Similarity> pair) ->
                pair.getSecond().getValue()).reversed());

        // Baue die Antwort basierend auf den Top 5 Ergebnissen
        StringBuilder responseBuilder = new StringBuilder("Ähnlichste Fälle:\n");
        int limit = Math.min(results.size(), 5); // Maximal 5 Einträge oder weniger, wenn weniger vorhanden
        for (int i = 0; i < limit; i++) {
            Pair<Instance, Similarity> result = results.get(i);
            responseBuilder.append("Fall: ").append(result.getFirst().getName())
                    .append(", Ähnlichkeit: ").append(result.getSecond().getValue()).append("\n");
        }
        return responseBuilder.toString();
    }

    /**
     * Sendet die Antwort an den Python-Agenten.
     *
     * @param response Die formatierte Antwort als String.
     */
    public void sendResponse(String response) {
        out.println(response);
        System.out.println(response);
    }
}
