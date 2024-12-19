package cbr_util;

import de.dfki.mycbr.util.Pair;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hilfsklasse für die Verarbeitung von Retrieval-Ergebnissen aus einem CBR-System.
 */
public class RetrievalHelper {

    /**
     * Kategorisiert die 5 ähnlichsten Fälle basierend auf ihrer Fallnummer.
     *
     * @param results Liste der Ergebnisse aus dem Retrieval-Prozess, bestehend aus Fall-Instanzen und deren Ähnlichkeitswerten.
     * @return Eine Map mit Fallnamen als Schlüssel und deren Kategorien als Werte.
     */
    public static Map<String, String> getCategorizedTopCases(List<Pair<Instance, Similarity>> results) {
        // Sortiere die Ergebnisse nach Ähnlichkeit (absteigend)
        results.sort((p1, p2) -> Double.compare(p2.getSecond().getValue(), p1.getSecond().getValue()));

        // Begrenze die Ergebnisse auf die Top-5
        List<Pair<Instance, Similarity>> topResults = results.stream().limit(5).toList();

        // Map zur Speicherung von Fallnamen und ihren Kategorien
        Map<String, String> categorizedCases = new HashMap<>();

        // Kategorisiere jeden der Top-5 Fälle
        for (Pair<Instance, Similarity> result : topResults) {
            String caseName = result.getFirst().getName(); // Name der Fallinstanz

            // Extrahiere die Fallnummer aus dem Fallnamen
            int caseNumber = extractCaseNumber(caseName);

            // Bestimme die Kategorie basierend auf der Fallnummer
            String category = getCategoryFromCaseNumber(caseNumber);

            // Füge die Zuordnung von Fallname und Kategorie in die Map ein
            categorizedCases.put(caseName, category);
        }

        return categorizedCases;
    }

    /**
     * Extrahiert die Fallnummer aus dem Namen eines Falls.
     *
     * @param caseName Der Name des Falls (z. B. "Fall 14").
     * @return Die extrahierte Fallnummer als Integer oder -1, falls keine gültige Zahl gefunden wurde.
     */
    private static int extractCaseNumber(String caseName) {
        try {
            // Entferne alle nicht-numerischen Zeichen und parse die verbleibende Zahl
            return Integer.parseInt(caseName.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            // Rückgabe von -1, wenn keine gültige Fallnummer extrahiert werden konnte
            return -1;
        }
    }

    /**
     * Bestimmt die Kategorie eines Falls basierend auf dessen Fallnummer.
     *
     * @param caseNumber Die Fallnummer, die aus dem Fallnamen extrahiert wurde.
     * @return Die entsprechende Kategorie (z. B. "Build", "Troops", "Attack", "Defense") oder "Unknown" für unklassifizierte Fälle.
     */
    private static String getCategoryFromCaseNumber(int caseNumber) {
        if (caseNumber >= 1 && caseNumber <= 5) {
            return "Build";
        } else if (caseNumber >= 6 && caseNumber <= 10) {
            return "Troops";
        } else if (caseNumber >= 11 && caseNumber <= 15) {
            return "Attack";
        } else if (caseNumber >= 16 && caseNumber <= 20) {
            return "Defense";
        } else {
            return "Unknown"; // Kategorie für Fälle außerhalb des definierten Bereichs
        }
    }
}
