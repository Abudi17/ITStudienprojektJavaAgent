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

        // Begrenze die Ergebnisse auf den Top-Eintrag
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
     * Kategorien basieren auf festen Bereichszuweisungen. Jede Kategorie entspricht
     * einer spezifischen Art von Aktion, wie z.B. Bauen, Angreifen oder Verteidigen,
     * die durch die Fallnummer definiert wird:
     * - 1 bis 5: "build_Nexus" (Errichtung eines Nexus)
     * - 6 bis 10: "build_Pylon" (Errichtung eines Pylons)
     * - 11 bis 20: "troup_Worker" (Erzeugung von Arbeitern)
     * - 21 bis 40: "build_Gateway" (Errichtung eines Gateways)
     * - 41 bis 60: "build_CyberneticCore" (Errichtung eines Kybernetik-Kerns)
     * - 61 bis 80: "build_Assimilator" (Errichtung eines Assimilators)
     * - 81 bis 100: "build_Stargate" (Errichtung eines Sternentors)
     * - 101 bis 120: "build_Forge" (Errichtung einer Schmiede)
     * - 121 bis 130: "deff_PhotonCannon" (Verteidigung mit Photonengeschütz)
     * - 131 bis 140: "troup_Voidray" (Erzeugung eines Voidrays)
     * - 141 bis 150: "troup_Sentry" (Erzeugung eines Sentrys)
     * - 151 bis 160: "deff_Sentry" (Verteidigung mit Sentrys)
     * - 161 bis 170: "attack_Voidray" (Angriff mit Voidrays)
     * - 171 bis 180: "attack_Sentry" (Angriff mit Sentrys)
     * - 181 bis 190: "attack_Sentry_Voidray" (Kombinierter Angriff mit Sentrys und Voidrays)
     * - 191 bis 200: platzhalter
     * - Andere: "Unknown" (Unklassifizierte Fälle)
     * </p>
     *
     * @param caseNumber Die Fallnummer, die aus dem Fallnamen extrahiert wurde.
     * @return Die entsprechende Kategorie des Falls oder "Unknown" für unklassifizierte Fälle.
     */
    private static String getCategoryFromCaseNumber(int caseNumber) {
        // Fallnummer im Bereich 1 bis 5: Nexus wird gebaut
        if (caseNumber == 0) {
            return "build_Nexus";
        }
        // Fallnummer im Bereich 6 bis 10: Pylon wird gebaut
        else if (caseNumber ==1 ) {
            return "build_Pylon";
        }
        // Fallnummer im Bereich 11 bis 20: Arbeiter werden erzeugt
        else if (caseNumber == 7 || caseNumber == 21 || caseNumber ==22 || caseNumber == 23) {
            return "troup_Worker";
        }
        // Fallnummer im Bereich 21 bis 40: Gateway wird gebaut
        else if (caseNumber == 2) {
            return "build_Gateway";
        }
        // Fallnummer im Bereich 41 bis 60: Kybernetik-Kern wird gebaut
        else if (caseNumber == 4) {
            return "build_CyberneticsCore";
        }
        // Fallnummer im Bereich 61 bis 80: Assimilator wird gebaut
        else if (caseNumber == 3) {
            return "build_Assimilator";
        }
        // Fallnummer im Bereich 81 bis 100: Sternentor wird gebaut
        else if (caseNumber == 5) {
            return "build_Stargate";
        }
        // Fallnummer im Bereich 101 bis 120: Schmiede wird gebaut
        else if (caseNumber == 15 || caseNumber == 12 || caseNumber == 13) {
            return "troup_Worker_Assimilator";
        }
        else if (caseNumber == 16) {
            return "troup_Zealot";
        }
        else if (caseNumber == 17) {
            return "troup_Stalker";
        }
        else if (caseNumber == 18) {
            return "attack_Zealot";
        }
        else if (caseNumber == 19) {
            return "attack_Stalker";
        }
        else if (caseNumber == 20) {
            return "attack_Zealot_Stalker";
        }
        // Für alle anderen Fallnummern: Unbekannte Kategorie
        else {
            return "Unknown";
        }
    }

}



