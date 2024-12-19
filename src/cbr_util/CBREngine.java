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
 * Diese Klasse initialisiert das myCBR-Projekt, führt Retrievals durch und stellt die Ergebnisse bereit.
 */
public class CBREngine {

    private static volatile CBREngine instance; // Singleton-Instanz
    private Concept statusConcept; // Konzept, das die Abfragen definiert
    private DefaultCaseBase caseBase; // Fallbasis des Projekts

    // Konstante für den Projektpfad und das zu verwendende Konzept
    private static final String PROJECT_PATH = "C:\\Jan\\Universität\\Master\\IT-Studienprojekt\\SpeichernvonMyCBRDaten\\StarCraft2.prj";
    private static final String CONCEPT_NAME = "Ressourcenentscheidungen";

    // Privater Konstruktor für das Singleton-Pattern
    private CBREngine() {}

    /**
     * Gibt die Singleton-Instanz der CBREngine zurück.
     *
     * @return die Singleton-Instanz.
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
     * Initialisiert das myCBR-Projekt, das Konzept und die Fallbasis.
     */
    public void init() {
        try {
            System.out.println("Lade myCBR-Projekt von: " + PROJECT_PATH);
            Project cbrProject = new Project(PROJECT_PATH);

            // Warte, bis der Import abgeschlossen ist
            while (cbrProject.isImporting()) {
                Thread.sleep(200);
            }

            // Lade das Konzept basierend auf seinem Namen
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
     * Führt ein Retrieval basierend auf den übergebenen Attributen durch.
     *
     * @param queryAttributes Map der Abfrage-Attribute (Attributname -> Attributwert).
     * @return Eine Liste von Fällen mit ihren Ähnlichkeitswerten.
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
            List<String> ignoredAttributes = new ArrayList<>();

            // Füge die Abfrageattribute hinzu
            for (Map.Entry<String, String> entry : queryAttributes.entrySet()) {
                AttributeDesc attrDesc = statusConcept.getAllAttributeDescs().get(entry.getKey());
                if (attrDesc != null) {
                    queryInstance.addAttribute(attrDesc, entry.getValue());
                } else {
                    ignoredAttributes.add(entry.getKey());
                }
            }

            if (!ignoredAttributes.isEmpty()) {
                System.out.println("Warnung: Folgende Attribute wurden ignoriert, da sie nicht im Konzept existieren: " + ignoredAttributes);
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
     * Führt das Retrieval durch und gibt die Top 5 Fälle kategorisiert zurück.
     *
     * @param queryAttributes Map der Abfrage-Attribute (Attributname -> Attributwert).
     * @return Map der 5 ähnlichsten Fälle mit ihren Kategorien.
     */
    public Map<String, String> retrieveAndCategorizeCases(Map<String, String> queryAttributes) {
        // Rufe die Ergebnisse des Retrievals ab
        List<Pair<Instance, Similarity>> results = retrieveCases(queryAttributes);

        // Übergib die Ergebnisse an den RetrievalHelper
        return RetrievalHelper.getCategorizedTopCases(results);
    }
}
