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
     * @param gameStatus Der empfangene Spielstatus, dessen Attribute verarbeitet werden sollen.
     * @return Eine Map mit Attributnamen (als Schlüssel) und ihren entsprechenden Werten (als Strings).
     */
    public static Map<String, String> extractAttributes(GameStatus gameStatus) {
        Map<String, String> attributes = new HashMap<>();

        // Attribute extrahieren und validieren
        attributes.put("Arbeiter", validateAttribute(gameStatus.getWorkers())); // Anzahl der Arbeiter
        attributes.put("FreieArbeiter", validateAttribute(gameStatus.getIdleWorkers())); // Anzahl der nicht beschäftigten Arbeiter
        attributes.put("Minerals", validateAttribute(gameStatus.getMinerals())); // Anzahl der verfügbaren Mineralien
        attributes.put("Gas", validateAttribute(gameStatus.getGas())); // Menge an verfügbarem Gas
        attributes.put("PhotonCannon", validateAttribute(gameStatus.getPhotonCannons())); // Anzahl der Photon Cannons
        attributes.put("Pylon", validateAttribute(gameStatus.getPylons())); // Anzahl der Pylonen
        attributes.put("Nexus", validateAttribute(gameStatus.getNexus())); // Anzahl der Nexus-Einheiten
        attributes.put("Gateways", validateAttribute(gameStatus.getGateways())); // Anzahl der Gateways
        attributes.put("CyberneticsCores", validateAttribute(gameStatus.getCyberneticsCores())); // Anzahl der Cybernetics Cores
        attributes.put("Stargates", validateAttribute(gameStatus.getStargates())); // Anzahl der Stargates
        attributes.put("Voidrays", validateAttribute(gameStatus.getVoidrays())); // Anzahl der Voidrays
        attributes.put("SupplyUsed", validateAttribute(gameStatus.getSupplyUsed())); // Genutzte Versorgungseinheiten
        attributes.put("SupplyCap", validateAttribute(gameStatus.getSupplyCap())); // Maximale Versorgungseinheiten
        attributes.put("Forge", validateAttribute(gameStatus.getForge())); // Anzahl der Forge
        attributes.put("Sentry", validateAttribute(gameStatus.getSentry()));
        attributes.put("Assimilator", validateAttribute(gameStatus.getAssimilator()));// Anzahl der Assimilator
        attributes.put("totalAssimilatorHarvesters", validateAttribute(gameStatus.getTotalAssimilatorHarvesters()));

        return attributes; // Rückgabe der Map mit Attributen und Werten
    }

    /**
     * Validiert ein Attribut, um sicherzustellen, dass es nicht null ist.
     * Falls das Attribut null ist, wird ein Standardwert von "0" zurückgegeben.
     *
     * @param value Der zu validierende Attributwert.
     * @return Der Attributwert als String oder "0", falls der Wert null ist.
     */
    private static String validateAttribute(Object value) {
        return value != null ? String.valueOf(value) : "0"; // Sicherstellen, dass keine null-Werte zurückgegeben werden
    }
}
