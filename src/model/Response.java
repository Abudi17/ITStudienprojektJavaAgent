package model;

import de.dfki.mycbr.util.Pair;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;

import java.io.PrintWriter;
import java.util.List;

public class Response {

    private final PrintWriter out;

    public Response(PrintWriter out) {
        this.out = out;
    }

    /**
     * Formatiert die Antwort basierend auf den CBR-Ergebnissen.
     *
     * @param results Liste von CBR-Ergebnissen (Instance-Similarity-Paare).
     * @return Die formatierte Antwort als String.
     */
    public String formatResponse(List<Pair<Instance, Similarity>> results) {
        StringBuilder responseBuilder = new StringBuilder("Ähnlichste Fälle:\n");
        for (Pair<Instance, Similarity> result : results) {
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
