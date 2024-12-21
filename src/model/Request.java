package model;

import com.google.gson.Gson;

/**
 * Die Request-Klasse kapselt eine Anfrage, die den aktuellen Spielstatus enthält.
 * Sie ermöglicht die Erstellung einer Instanz aus einer JSON-Darstellung und
 * überprüft die Gültigkeit der Anfrage.
 * Die Klasse ist als `record` implementiert, was bedeutet, dass sie immutable ist
 * und automatisch Konstruktor, Getter, `equals`, `hashCode` und `toString` generiert.
 */
public record Request(GameStatus gameStatus) {

    /**
     * Erstellt eine Request-Instanz aus einem flachen JSON-String.
     *
     * @param flatJson JSON-Darstellung des GameStatus.
     * @return Eine neue Request-Instanz.
     * @throws IllegalArgumentException Wenn das JSON ungültig oder leer ist.
     */
    public static Request fromFlatJson(String flatJson) {
        // Überprüfung, ob der JSON-String null oder leer ist
        if (flatJson == null || flatJson.isBlank()) {
            throw new IllegalArgumentException("Eingabe-JSON darf nicht leer sein.");
        }
        try {
            // Gson-Instanz für die JSON-Dekodierung
            Gson gson = new Gson();
            // JSON-String in ein GameStatus-Objekt konvertieren
            GameStatus gameStatus = gson.fromJson(flatJson, GameStatus.class);
            // Rückgabe einer neuen Request-Instanz mit dem dekodierten GameStatus
            return new Request(gameStatus);
        } catch (Exception e) {
            // Fehlerbehandlung bei ungültigem JSON-Format
            throw new IllegalArgumentException("Ungültiges JSON-Format: " + flatJson, e);
        }
    }

    /**
     * Überprüft, ob die Anfrage gültig ist.
     * Die Gültigkeit hängt davon ab, ob der `gameStatus` nicht null ist
     * und seine Attribute sinnvolle Werte haben.
     *
     * @return true, wenn der gameStatus nicht null ist und valide Werte aufweist.
     */
    public boolean isValid() {
        return gameStatus != null
                && gameStatus.getWorkers() >= 0 // Arbeiteranzahl darf nicht negativ sein
                && gameStatus.getMinerals() >= 0 // Mineralienanzahl darf nicht negativ sein
                && gameStatus.getSupplyCap() >= gameStatus.getSupplyUsed(); // Versorgungslimit muss ausreichen
    }

    /**
     * Gibt eine String-Repräsentation der Request zurück.
     * Die Darstellung enthält den enthaltenen GameStatus.
     *
     * @return String-Darstellung der Request.
     */
    @Override
    public String toString() {
        return "Request{" +
                "gameStatus=" + gameStatus +
                '}';
    }
}
