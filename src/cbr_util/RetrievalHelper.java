package cbr_util;

import de.dfki.mycbr.util.Pair;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.similarity.Similarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hilfsklasse für die Verarbeitung und Kategorisierung von Retrieval-Ergebnissen
 * aus einem Case-Based Reasoning (CBR)-System.
 */
public class RetrievalHelper {

    /**
     * Kategorisiert die Top-5 ähnlichsten Fälle basierend auf ihren Fallnummern und deren Ähnlichkeitswerten.
     *
     * @param results Eine Liste von Ergebnissen aus dem Retrieval-Prozess, bestehend aus Fall-Instanzen
     *                und ihren zugehörigen Ähnlichkeitswerten.
     * @return Eine Map, in der die Fallnamen (als Schlüssel) ihren entsprechenden Kategorien (als Werte) zugeordnet sind.
     */
    public static Map<String, String> getCategorizedTopCases(List<Pair<Instance, Similarity>> results) {
        // Sortiere die Ergebnisse basierend auf Ähnlichkeitswerten in absteigender Reihenfolge
        results.sort((p1, p2) -> Double.compare(p2.getSecond().getValue(), p1.getSecond().getValue()));

        // Begrenze die Ergebnisse auf die Top Eintrag
        List<Pair<Instance, Similarity>> topResults = results.stream().limit(1).toList();

        // Map zur Speicherung der Zuordnung von Fallnamen und Kategorien
        Map<String, String> categorizedCases = new HashMap<>();

        // Iteriere über die Top-5 Ergebnisse und kategorisiere die Fälle
        for (Pair<Instance, Similarity> result : topResults) {
            String caseName = result.getFirst().getName(); // Name der Fallinstanz

            // Extrahiere die Fallnummer aus dem Fallnamen
            int caseNumber = extractCaseNumber(caseName);

            // Bestimme die Kategorie basierend auf der Fallnummer
            String category = getCategoryFromCaseNumber(caseNumber);

            // Speichere die Zuordnung von Fallname und Kategorie in der Map
            categorizedCases.put(caseName, category);
        }

        return categorizedCases;
    }

    /**
     * Extrahiert die Fallnummer aus dem Namen eines Falls.
     *
     * <p>
     * Beispiel:
     * - Eingabe: "Fall 14" zu Ausgabe: 14
     * - Eingabe: "Case XYZ" zu Ausgabe: -1 (falls keine Zahl gefunden wird)
     * </p>
     *
     * @param caseName Der Name des Falls, aus dem die Nummer extrahiert werden soll (z. B. "Fall 14").
     * @return Die extrahierte Fallnummer als Integer oder -1, falls keine gültige Zahl im Fallnamen gefunden wurde.
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
     * <p>
     * Kategorien basieren auf festen Bereichen:
     * - 1 bis 5: "Build"
     * - 6 bis 10: "Troops"
     * - 11 bis 15: "Attack"
     * - 16 bis 20: "Defense"
     * - Andere: "Unknown"
     * </p>
     *
     * @param caseNumber Die Fallnummer, die aus dem Fallnamen extrahiert wurde.
     * @return Die entsprechende Kategorie des Falls oder "Unknown" für unklassifizierte Fälle.
     */
    private static String getCategoryFromCaseNumber(int caseNumber) {
        if (caseNumber == 0) {
            return "build_Nexus";
        } else if (caseNumber == 1) {
            return "build_Pylon";
        } else if (caseNumber == 2) {
            return "build_Gateway";
        } else if (caseNumber == 3) {
            return "build_Assimilator";
        } else if (caseNumber == 4) {
            return "build_CyberneticsCore";
        } else if (caseNumber == 5) {
            return "build_Stargate";
        } else if (caseNumber == 6) {
            return "build_Forge";
        } else if (caseNumber == 7) {
            return "troup_Worker";
        } else if (caseNumber == 8) {
            return "troup_Voidray";
        } else if (caseNumber == 9) {
            return "troup_Sentry";
        } else if (caseNumber == 10) {
            return "deff_PhotonCannon";
        } else if (caseNumber == 11) {
            return "deff_Sentry";
        } else if (caseNumber == 12) {
            return "attack_Voidray";
        } else if (caseNumber == 13) {
            return "attack_Sentry";
        } else if (caseNumber == 14) {
            return "attack_Sentry_Voidray";
        } else {
            return "Unknown"; // Kategorie für Fälle außerhalb des definierten Bereichs
        }
    }
}
