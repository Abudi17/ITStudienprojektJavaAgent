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
    private static volatile CBREngine instance;
    private Concept statusConcept;
    private DefaultCaseBase caseBase;

    private static final String PROJECT_PATH = "C:\\Jan\\Universität\\Master\\IT-Studienprojekt\\SpeichernvonMyCBRDaten\\StarCraft2.prj";
    private static final String CONCEPT_NAME = "Ressourcenentscheidungen";

    private CBREngine() {}

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

    public void init() {
        try {
            System.out.println("Lade myCBR-Projekt von: " + PROJECT_PATH);
            Project cbrProject = new Project(PROJECT_PATH);

            while (cbrProject.isImporting()) {
                Thread.sleep(200);
            }

            statusConcept = cbrProject.getConceptByID(CONCEPT_NAME);
            if (statusConcept == null) {
                throw new IllegalArgumentException("Das Konzept '" + CONCEPT_NAME + "' wurde nicht gefunden.");
            }

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

    public List<Pair<Instance, Similarity>> retrieveCases(Map<String, String> queryAttributes) {
        if (statusConcept == null || caseBase == null) {
            throw new IllegalStateException("CBREngine wurde nicht initialisiert. Rufe init() auf.");
        }

        List<Pair<Instance, Similarity>> results = new ArrayList<>();
        try {
            Retrieval retrieval = new Retrieval(statusConcept, caseBase);
            retrieval.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);

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

            retrieval.start();
            results = retrieval.getResult();

        } catch (Exception e) {
            System.err.println("Fehler beim Retrieval: " + e.getMessage());
        }

        return results;
    }


}
