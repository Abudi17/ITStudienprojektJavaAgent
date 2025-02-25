package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Die Klasse GameStatus repräsentiert den aktuellen Status eines Spiels
 * und speichert verschiedene relevante Metriken.
 * Sie implementiert das Serializable-Interface, um die Instanzen der Klasse
 * serialisieren und speichern zu können.
 */
public class GameStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Versionierung für Serialisierung

    // Attribute, die den Status des Spiels repräsentieren
    private int iteration;              // Iterationsnummer (Schritt im Spiel)
    private int workers;                // Anzahl der Arbeiter
    private int idleWorkers;            // Anzahl der untätigen Arbeiter
    private int minerals;               // Menge der verfügbaren Mineralien
    private int gas;                    // Menge des verfügbaren Gases
    private int photonCannons;          // Anzahl der Photon-Kanonen
    private int pylons;                 // Anzahl der Pylonen
    private int nexus;                  // Anzahl der Nexus-Strukturen
    private int gateways;               // Anzahl der Gateways
    private int cyberneticsCores;       // Anzahl der Cybernetics Cores
    private int stargates;              // Anzahl der Stargates
    private int voidrays;               // Anzahl der Void Rays
    private int supplyUsed;             // Aktuell verwendetes Versorgungslimit
    private int supplyCap;              // Maximales Versorgungslimit
    private int forge;                  // Anzahl der Schmiede
    private int sentry;                 // Anzahl der Protektor
    private int assimilator;

    /**
     * Standardkonstruktor, der alle Werte auf ihre Standardwerte (0) setzt.
     */
    public GameStatus() {
        // Standardwerte sind automatisch 0
    }

    /**
     * Konstruktor, der alle Werte initialisiert.
     * Negative Werte werden durch 0 ersetzt.
     *
     * @param iteration        Iterationsnummer
     * @param workers          Anzahl der Arbeiter
     * @param idleWorkers      Anzahl der untätigen Arbeiter
     * @param minerals         Verfügbare Mineralien
     * @param gas              Verfügbares Gas
     * @param photonCannons    Anzahl der Photon-Kanonen
     * @param pylons           Anzahl der Pylonen
     * @param nexus            Anzahl der Nexus-Strukturen
     * @param gateways         Anzahl der Gateways
     * @param cyberneticsCores Anzahl der Cybernetics Cores
     * @param stargates        Anzahl der Stargates
     * @param voidrays         Anzahl der Void Rays
     * @param supplyUsed       Verwendetes Versorgungslimit
     * @param supplyCap        Maximales Versorgungslimit
     * @param forge            Anzahl der Schmiede
     * @param sentry           Anzahl der Protektor
     * @param assimilator
     */
    public GameStatus(int iteration, int workers, int idleWorkers, int minerals, int gas, int photonCannons, int pylons,
                      int nexus, int gateways, int cyberneticsCores, int stargates, int voidrays, int supplyUsed, int supplyCap, int forge, int sentry, int assimilator) {
        this.iteration = iteration;
        this.workers = Math.max(0, workers); // Validierung: Keine negativen Werte
        this.idleWorkers = Math.max(0, idleWorkers);
        this.minerals = Math.max(0, minerals);
        this.gas = Math.max(0, gas);
        this.photonCannons = Math.max(0, photonCannons);
        this.pylons = Math.max(0, pylons);
        this.nexus = Math.max(0, nexus);
        this.gateways = Math.max(0, gateways);
        this.cyberneticsCores = Math.max(0, cyberneticsCores);
        this.stargates = Math.max(0, stargates);
        this.voidrays = Math.max(0, voidrays);
        this.supplyUsed = Math.max(0, supplyUsed);
        this.supplyCap = Math.max(0, supplyCap);
        this.forge = Math.max(0, forge);
        this.sentry = Math.max(0, sentry);
        this.assimilator = Math.max(0, assimilator);
    }

    // Getter-Methoden

    public int getWorkers() {
        return workers;
    }

    public int getIteration() {
        return iteration;
    }

    public int getIdleWorkers() {
        return idleWorkers;
    }

    public int getMinerals() {
        return minerals;
    }

    public int getGas() {
        return gas;
    }

    public int getPhotonCannons() {
        return photonCannons;
    }

    public int getPylons() {
        return pylons;
    }

    public int getNexus() {
        return nexus;
    }

    public int getGateways() {
        return gateways;
    }

    public int getCyberneticsCores() {
        return cyberneticsCores;
    }

    public int getStargates() {
        return stargates;
    }

    public int getVoidrays() {
        return voidrays;
    }

    public int getSupplyUsed() {
        return supplyUsed;
    }

    public int getSupplyCap() {
        return supplyCap;
    }

    public int getForge() {
        return forge;
    }

    public int getSentry() {
        return sentry;
    }

    public int getAssimilator() {return assimilator;}

    // Setter-Methoden mit Validierung (keine negativen Werte)

    public void setWorkers(int workers) {
        this.workers = Math.max(0, workers);
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public void setIdleWorkers(int idleWorkers) {
        this.idleWorkers = Math.max(0, idleWorkers);
    }

    public void setMinerals(int minerals) {
        this.minerals = Math.max(0, minerals);
    }

    public void setGas(int gas) {
        this.gas = Math.max(0, gas);
    }

    public void setPhotonCannons(int photonCannons) {
        this.photonCannons = Math.max(0, photonCannons);
    }

    public void setPylons(int pylons) {
        this.pylons = Math.max(0, pylons);
    }

    public void setNexus(int nexus) {
        this.nexus = Math.max(0, nexus);
    }

    public void setGateways(int gateways) {
        this.gateways = Math.max(0, gateways);
    }

    public void setCyberneticsCores(int cyberneticsCores) {
        this.cyberneticsCores = Math.max(0, cyberneticsCores);
    }

    public void setStargates(int stargates) {
        this.stargates = Math.max(0, stargates);
    }

    public void setVoidrays(int voidrays) {
        this.voidrays = Math.max(0, voidrays);
    }

    public void setSupplyUsed(int supplyUsed) {
        this.supplyUsed = Math.max(0, supplyUsed);
    }

    public void setSupplyCap(int supplyCap) {
        this.supplyCap = Math.max(0, supplyCap);
    }

    public void setForge(int forge) {
        this.forge = Math.max(0, forge);
    }

    public void setSentry(int sentry) {
        this.sentry = Math.max(0, sentry);
    }

    public void setAssimilator(int assimilator) {this.assimilator = Math.max(0, assimilator);}

    /**
     * Gibt eine String-Repräsentation des GameStatus-Objekts zurück.
     *
     * @return String-Darstellung des aktuellen Spielstatus.
     */
    @Override
    public String toString() {
        return "GameStatus{" +
                "iteration=" + iteration +
                ", workers=" + workers +
                ", idleWorkers=" + idleWorkers +
                ", minerals=" + minerals +
                ", gas=" + gas +
                ", photonCannons=" + photonCannons +
                ", pylons=" + pylons +
                ", nexus=" + nexus +
                ", gateways=" + gateways +
                ", cyberneticsCores=" + cyberneticsCores +
                ", stargates=" + stargates +
                ", voidrays=" + voidrays +
                ", supplyUsed=" + supplyUsed +
                ", supplyCap=" + supplyCap +
                ", forge=" + forge +
                ", sentry=" + sentry +
                ", assimilator=" + assimilator +
                '}';
    }

}
