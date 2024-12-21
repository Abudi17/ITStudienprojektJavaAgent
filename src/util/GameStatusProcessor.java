package util;

import model.GameStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility-Klasse zur Verarbeitung von GameStatus-Objekten.
 * Diese Klasse bietet Hilfsmethoden, um die Attribute eines GameStatus-Objekts zu extrahieren
 * und sie in einer benutzerfreundlichen Form darzustellen.
 */
public class GameStatusProcessor {

    /**
     * Extrahiert die Attribut-Werte-Paare aus einem GameStatus-Objekt.
     * Die Attribute und ihre Werte werden in einer Map gespeichert, wobei die Attributnamen als Schlüssel
     * und die Werte als Strings vorliegen.
     *
     * @param gameStatus Der empfangene Spielstatus.
     * @return Eine Map mit Attributnamen und ihren entsprechenden Werten als Strings.
     */
    public static Map<String, String> extractAttributes(GameStatus gameStatus) {
        Map<String, String> attributes = new HashMap<>();

        // Attribute extrahieren und validieren
        attributes.put("Arbeiter", validateAttribute(gameStatus.getWorkers()));
        attributes.put("Freie Arbeiter", validateAttribute(gameStatus.getIdleWorkers()));
        attributes.put("Mineralien", validateAttribute(gameStatus.getMinerals()));
        attributes.put("Gas", validateAttribute(gameStatus.getGas()));
        attributes.put("Photonenkanonen", validateAttribute(gameStatus.getPhotonCannons()));
        attributes.put("Pylonen", validateAttribute(gameStatus.getPylons()));
        attributes.put("Nexus", validateAttribute(gameStatus.getNexus()));
        attributes.put("Gateways", validateAttribute(gameStatus.getGateways()));
        attributes.put("Cybernetics Cores", validateAttribute(gameStatus.getCyberneticsCores()));
        attributes.put("Sternentore", validateAttribute(gameStatus.getStargates()));
        attributes.put("Voidrays", validateAttribute(gameStatus.getVoidrays()));
        attributes.put("Genutzte Versorgung", validateAttribute(gameStatus.getSupplyUsed()));
        attributes.put("Versorgungsobergrenze", validateAttribute(gameStatus.getSupplyCap()));

        return attributes;
    }

    /**
     * Validiert ein Attribut, indem überprüft wird, ob es null ist, und null-Werte durch "0" ersetzt.
     * Diese Methode stellt sicher, dass die Map keine null-Werte enthält.
     *
     * @param value Der zu validierende Wert.
     * @return Der validierte Wert als String.
     */
    private static String validateAttribute(Object value) {
        return value != null ? String.valueOf(value) : "0";
    }
}
