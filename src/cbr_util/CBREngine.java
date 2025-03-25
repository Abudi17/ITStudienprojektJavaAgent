package cbr_util;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.ICaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;

import java.util.*;

/**
 * Singleton-Klasse zur Verwaltung des CBR-Systems.
 * Diese Klasse initialisiert das myCBR-Projekt, ermöglicht die Durchführung von Retrievals
 * und bietet Methoden zur Verarbeitung der Ergebnisse.
 */
public class CBREngine {

    /**
     * Singleton-Instanz der CBREngine
     */
    private static volatile CBREngine instance;

    /**
     * Das Hauptkonzept des myCBR-Projekts, auf dem die Abfragen basieren
     */
    private Concept statusConcept;

    /**
     * Die Fallbasis (Case Base), die Fälle speichert und abfragt
     */
    private DefaultCaseBase caseBase;

    /**
     * Pfad zur myCBR-Projektdatei
     */
    private static final String PROJECT_PATH = "C:\\Users\\abdul\\Desktop\\IT-Studienprojekt\\StarCraft2.prj";

    /**
     * Name des Hauptkonzepts im Projekt
     */
    private static final String CONCEPT_NAME = "Ressourcenentscheidungen";

    /**
     * Privater Konstruktor, um die Erstellung mehrerer Instanzen zu verhindern
     * (Singleton-Pattern).
     */
    private CBREngine() {
    }

    /**
     * Gibt die Singleton-Instanz der CBREngine zurück.
     * Falls die Instanz noch nicht existiert, wird sie initialisiert.
     *
     * @return die Singleton-Instanz der CBREngine
     */
    public static CBREngine getInstance() {
        if (instance == null) {
            synchronized (CBREngine.class) {
                if (instance == null) {
                    instance = new CBREngine();
                }
            }
        }
        return instance;
    }

    /**
     * Initialisiert das myCBR-Projekt, lädt das Hauptkonzept und die Standard-Fallbasis.
     */
    public void init() {
        try {
            System.out.println("Lade myCBR-Projekt von: " + PROJECT_PATH);
            Project cbrProject = new Project(PROJECT_PATH);

            // Warte, bis der Projekt-Import abgeschlossen ist
            while (cbrProject.isImporting()) {
                Thread.sleep(200);
            }

            // Lade das Hauptkonzept aus dem Projekt
            statusConcept = cbrProject.getConceptByID(CONCEPT_NAME);
            if (statusConcept == null) {
                throw new IllegalArgumentException("Das Konzept '" + CONCEPT_NAME + "' wurde nicht gefunden.");
            }

            // Initialisiere die Standard-Fallbasis
            ICaseBase base = cbrProject.getCaseBases().values().stream().findFirst().orElse(null);
            if (base instanceof DefaultCaseBase) {
                caseBase = (DefaultCaseBase) base;
            } else {
                throw new IllegalArgumentException("Keine gültige Standard-Fallbasis gefunden.");
            }

            System.out.println("CBREngine erfolgreich initialisiert.");
        } catch (Exception e) {
            System.err.println("Fehler beim Initialisieren der CBREngine: " + e.getMessage());
        }
    }

    /**
     * Führt ein Retrieval basierend auf den angegebenen Attributen durch.
     *
     * @param queryAttributes Eine Map mit Attributnamen und zugehörigen Werten für die Abfrage
     * @return Eine Liste von Fällen und deren Ähnlichkeitswerten
     */
    public List<Pair<Instance, Similarity>> retrieveCases(Map<String, String> queryAttributes) {
        if (statusConcept == null || caseBase == null) {
            throw new IllegalStateException("CBREngine wurde nicht initialisiert. Rufe init() auf.");
        }

        List<Pair<Instance, Similarity>> results = new ArrayList<>();
        try {
            // Erstelle ein Retrieval-Objekt
            Retrieval retrieval = new Retrieval(statusConcept, caseBase);
            retrieval.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);

            // Initialisiere die Abfrageinstanz
            Instance queryInstance = retrieval.getQueryInstance();

            // Füge die Abfrageattribute hinzu, wobei null-Attribute ausgeschlossen werden
            List<String> ignoredAttributes = new ArrayList<>();

            for (Map.Entry<String, String> entry : queryAttributes.entrySet()) {
                AttributeDesc attrDesc = statusConcept.getAllAttributeDescs().get(entry.getKey());
                if (attrDesc != null && entry.getValue() != null) {
                    queryInstance.addAttribute(attrDesc, entry.getValue());
                } else {
                    ignoredAttributes.add(entry.getKey());
                }
            }

            // Warnung für ignorierte Attribute
            if (!ignoredAttributes.isEmpty()) {
                System.out.println("Warnung: Folgende Attribute wurden ignoriert, da sie nicht im Konzept existieren oder null sind: " + ignoredAttributes);
            }

            // Starte das Retrieval und speichere die Ergebnisse
            retrieval.start();
            results = retrieval.getResult();

        } catch (Exception e) {
            System.err.println("Fehler beim Retrieval: " + e.getMessage());
        }

        return results;
    }

    /**
     * Führt ein Retrieval durch und gibt die Top 5 Ergebnisse kategorisiert zurück.
     *
     * @param queryAttributes Eine Map mit Attributnamen und zugehörigen Werten für die Abfrage
     * @return Eine Map der Top 5 Fälle (Fallname -> Kategorie)
     */
    public Map<String, Pair<String, Double>> retrieveAndCategorizeCases(Map<String, String> queryAttributes) {
        // Rufe die Ergebnisse des Retrievals ab
        List<Pair<Instance, Similarity>> results = retrieveCases(queryAttributes);

        for (Pair<Instance, Similarity> result : results) {
            System.out.println("Instance: " + result.getFirst() + ", Similarity: " + result.getSecond().getValue());
        }

        // Kategorisierte Fälle abrufen
        Map<String, String> categorizedCases = RetrievalHelper.getCategorizedTopCases(results);

        // Map mit Ähnlichkeitswerten erstellen
        Map<String, Pair<String, Double>> casesWithSimilarity = new HashMap<>();
        for (Pair<Instance, Similarity> result : results) {
            String caseName = result.getFirst().getName();
            double similarityValue = result.getSecond().getValue();
            String category = categorizedCases.getOrDefault(caseName, "Unbekannt");

            casesWithSimilarity.put(caseName, new Pair<>(category, similarityValue));
        }

        return casesWithSimilarity;
    }
}
