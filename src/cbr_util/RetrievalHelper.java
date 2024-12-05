package cbr_util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dfki.mycbr.util.Pair;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;

import java.util.List;

/**
 * Utility-Klasse zur Unterstützung von JSON-Verarbeitung für Retrieval-Prozesse.
 */
public class RetrievalHelper {

    /**
     * Konvertiert die Ergebnisse einer myCBR-Abfrage in einen JSON-String.
     * Diese Methode wurde so erweitert, dass sie auch die Attribute der Fälle einbezieht.
     *
     * @param results Die Liste der gefundenen Fälle mit Ähnlichkeiten.
     * @return Ein JSON-String, der die Fälle und ihre Ähnlichkeiten darstellt.
     */
    public static String processResponseAsString(List<Pair<Instance, Similarity>> results) {
        JsonObject responseObject = new JsonObject();
        JsonArray casesArray = new JsonArray();

        // Durchlaufen der Ergebnisse und Umwandeln in ein JSON-Format
        for (Pair<Instance, Similarity> pair : results) {
            JsonObject caseObject = new JsonObject();
            caseObject.addProperty("caseID", pair.getFirst().getName()); // Fall-ID
            caseObject.addProperty("similarity", String.format("%.2f", pair.getSecond().getValue() * 100) + "%"); // Ähnlichkeit in %

            // Durch alle Attribute der Instanz gehen und sie in den JSON-Objekt einfügen
            JsonObject attributesObject = new JsonObject();
            pair.getFirst().getAttributes().forEach((attributeDesc, value) -> {
                attributesObject.addProperty(attributeDesc.getName(), value.toString());
            });

            caseObject.add("attributes", attributesObject); // Hinzufügen der Attribute
            casesArray.add(caseObject);
        }

        responseObject.add("cases", casesArray);
        return responseObject.toString(); // Rückgabe des JSON-Strings mit allen Fällen und Ähnlichkeiten
    }
}
