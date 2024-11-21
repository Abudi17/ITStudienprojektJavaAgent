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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CBREngine {
    private static final String APPLICATION_NAME = "myCBRApplication";
    private static final String PROJECT_NAME = "Starkraft2.prj"; // Name der .prj-Datei
    private static final String CONCEPT_NAME = "Ressourcenentscheidungen"; // Name des Hauptkonzepts in der .prj-Datei

    private Project cbrProject;
    private Concept statusConcept;
    private DefaultCaseBase caseBase;

    /**
     * Singleton-Instanz der CBREngine.
     */
    private static CBREngine instance;

    /**
     * Privater Konstruktor zur Initialisierung der CBREngine.
     */
    private CBREngine() {
        try {
            // Pfad zur .prj-Datei dynamisch erstellen
            // Absoluten Pfad zur .prj-Datei angeben
            String projectPath = "C:\\Jan\\Universität\\Master\\IT-Studienprojekt\\SpeichernvonMyCBRDaten\\StarKraft2.prj";

            // Projekt laden
            cbrProject = new Project(projectPath).getProject();


            // Projekt laden
            cbrProject = new Project(projectPath);

            // Warten, bis das Projekt vollständig geladen ist
            while (cbrProject.isImporting()) {
                Thread.sleep(200);
            }

            // Hauptkonzept aus der .prj-Datei abrufen
            statusConcept = cbrProject.getConceptByID(CONCEPT_NAME);

            if (statusConcept == null) {
                throw new IllegalArgumentException("Das Hauptkonzept '" + CONCEPT_NAME + "' wurde nicht gefunden.");
            }

            // Standard-Fallbasis laden
            ICaseBase base = cbrProject.getCaseBases().get(cbrProject.getCaseBases().keySet().iterator().next());
            if (base instanceof DefaultCaseBase) {
                caseBase = (DefaultCaseBase) base;
            } else {
                throw new IllegalArgumentException("Keine gültige Standard-Fallbasis gefunden.");
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Laden des CBR-Systems: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gibt die Singleton-Instanz der CBREngine zurück.
     *
     * @return Instanz der CBREngine.
     */
    public static CBREngine getInstance() {
        if (instance == null) {
            instance = new CBREngine();
        }
        return instance;
    }

    /**
     * Führt den Retrieve-Prozess mit einer gegebenen Anfrage aus.
     *
     * @param queryAttributes Die Attributwerte für die Abfrage (Attributname und -wert).
     * @return Liste der ähnlichsten Fälle.
     */
    public List<Pair<Instance, Similarity>> retrieveCases(Map<String, String> queryAttributes) {
        List<Pair<Instance, Similarity>> results = new ArrayList<>();
        try {
            // Neue Retrieval-Instanz erstellen
            Retrieval retrieval = new Retrieval(statusConcept, caseBase);
            retrieval.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);

            // Query-Attribute setzen
            Instance queryInstance = retrieval.getQueryInstance();
            for (Map.Entry<String, String> entry : queryAttributes.entrySet()) {
                AttributeDesc attrDesc = statusConcept.getAllAttributeDescs().get(entry.getKey());
                if (attrDesc != null) {
                    queryInstance.addAttribute(attrDesc, entry.getValue());
                } else {
                    System.out.println("Attribut '" + entry.getKey() + "' existiert nicht im Konzept.");
                }
            }

            // Retrieval ausführen
            retrieval.start();
            results = retrieval.getResult();

        } catch (Exception e) {
            System.out.println("Fehler beim Retrieval-Prozess: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Gibt die Standard-Fallbasis zurück.
     *
     * @return Fallbasis.
     */
    public DefaultCaseBase getCaseBase() {
        return caseBase;
    }

    /**
     * Setzt das Hauptkonzept für die CBREngine.
     *
     * @param concept Hauptkonzept des Projekts.
     */
    public void setStatusConcept(Concept concept) {
        this.statusConcept = concept;
    }

    /**
     * Gibt das Hauptkonzept zurück.
     *
     * @return Hauptkonzept.
     */
    public Concept getMainConcept() {
        return this.statusConcept;
    }

    /**
     * Gibt die verfügbaren Instanzen in der Fallbasis zurück.
     *
     * @return Liste der Instanzen.
     */
    public List<Instance> getAllInstances() {
        try {
            return new ArrayList<>(caseBase.getCases().stream().toList());
        } catch (Exception e) {
            System.out.println("Fehler beim Abrufen der Instanzen: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
