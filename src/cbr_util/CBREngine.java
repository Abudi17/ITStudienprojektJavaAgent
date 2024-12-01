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

public class CBREngine {
    private static CBREngine instance;
    private Project cbrProject;
    private Concept statusConcept;
    private DefaultCaseBase caseBase;

    /**
     * Absoluter Pfad zur CBR-Projektdatei (.prj)
     */
    private static final String PROJECT_PATH = "C:\\Jan\\Universität\\Master\\IT-Studienprojekt\\SpeichernvonMyCBRDaten\\StarCraft2.prj";

    /**
     * Name des Hauptkonzepts im Projekt.
     */
    private static final String CONCEPT_NAME = "Ressourcenentscheidungen";

    /**
     * Privater Konstruktor für das Singleton-Pattern.
     */
    private CBREngine() {
    }

    /**
     * Singleton-Instanz abrufen.
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
     * Initialisiert das CBR-Projekt. Nutzt den festgelegten Projektpfad und Konzeptnamen.
     */
    public void init() {
        try {
            System.out.println("Lade myCBR-Projekt von: " + PROJECT_PATH);
            cbrProject = new Project(PROJECT_PATH);

            // Warten, bis das Projekt vollständig geladen ist
            while (cbrProject.isImporting()) {
                Thread.sleep(200);
            }

            System.out.println("Projekt erfolgreich geladen.");

            // Hauptkonzept abrufen
            statusConcept = cbrProject.getConceptByID(CONCEPT_NAME);
            if (statusConcept == null) {
                throw new IllegalArgumentException("Das Konzept '" + CONCEPT_NAME + "' wurde nicht gefunden.");
            }

            // Fallbasis abrufen
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
     * Führt eine Abfrage basierend auf den angegebenen Attributen aus.
     *
     * @param queryAttributes Map mit Attributnamen und -werten.
     * @return Liste von Instanzen und ihren Ähnlichkeiten.
     */
    public List<Pair<Instance, Similarity>> retrieveCases(Map<String, String> queryAttributes) {
        if (statusConcept == null || caseBase == null) {
            throw new IllegalStateException("CBREngine wurde nicht initialisiert. Rufe init() auf.");
        }

        List<Pair<Instance, Similarity>> results = new ArrayList<>();
        try {
            // Retrieval-Instanz erstellen
            Retrieval retrieval = new Retrieval(statusConcept, caseBase);
            retrieval.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);

            // Abfrageinstanz mit Attributen füllen
            Instance queryInstance = retrieval.getQueryInstance();
            List<String> ignoredAttributes = new ArrayList<>();

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

            // Retrieval ausführen
            retrieval.start();
            results = retrieval.getResult();

            System.out.println("Retrieval erfolgreich abgeschlossen. Gefundene Fälle: " + results.size());

        } catch (Exception e) {
            System.err.println("Fehler beim Retrieval: " + e.getMessage());
        }

        return results;
    }

    /**
     * Gibt alle Instanzen aus der Fallbasis zurück.
     *
     * @return Liste der Instanzen.
     */
    public List<Instance> getAllInstances() {
        if (caseBase == null) {
            throw new IllegalStateException("CBREngine wurde nicht initialisiert. Rufe init() auf.");
        }
        return new ArrayList<>(caseBase.getCases());
    }

    /**
     * Gibt das Hauptkonzept zurück.
     *
     * @return Hauptkonzept.
     */
    public Concept getMainConcept() {
        return statusConcept;
    }

    /**
     * Setzt das Hauptkonzept der CBREngine.
     *
     * @param conceptName Name des Konzepts.
     */
    public void setStatusConcept(String conceptName) {
        Concept concept = cbrProject.getConceptByID(conceptName);
        if (concept != null) {
            this.statusConcept = concept;
            System.out.println("Konzept '" + conceptName + "' erfolgreich gesetzt.");
        } else {
            throw new IllegalArgumentException("Konzept '" + conceptName + "' wurde nicht gefunden.");
        }
    }
}
