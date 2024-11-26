package model;

public class GameStatus {
    private int iteration;
    private int workers;
    private int idleWorkers;
    private int minerals;
    private int gas;
    private int cannons;
    private int pylons;
    private int nexus;
    private int gateways;
    private int cyberneticsCores;
    private int stargates;
    private int voidrays;
    private int supplyUsed;
    private int supplyCap;

    // Leerer Konstruktur für Default Werte
    public GameStatus() {
    }

    // Konstruktor, der alle Felder initialisiert
    public GameStatus(int iteration, int workers, int idleWorkers, int minerals, int gas, int cannons, int pylons,
                      int nexus, int gateways, int cyberneticsCores, int stargates, int voidrays, int supplyUsed, int supplyCap) {
        this.iteration = iteration;
        this.workers = workers;
        this.idleWorkers = idleWorkers;
        this.minerals = minerals;
        this.gas = gas;
        this.cannons = cannons;
        this.pylons = pylons;
        this.nexus = nexus;
        this.gateways = gateways;
        this.cyberneticsCores = cyberneticsCores;
        this.stargates = stargates;
        this.voidrays = voidrays;
        this.supplyUsed = supplyUsed;
        this.supplyCap = supplyCap;
    }

    // Getter und Setter
    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getIdleWorkers() {
        return idleWorkers;
    }

    public void setIdleWorkers(int idleWorkers) {
        this.idleWorkers = idleWorkers;
    }

    public int getMinerals() {
        return minerals;
    }

    public void setMinerals(int minerals) {
        this.minerals = minerals;
    }

    public int getGas() {
        return gas;
    }

    public void setGas(int gas) {
        this.gas = gas;
    }

    public int getCannons() {
        return cannons;
    }

    public void setCannons(int cannons) {
        this.cannons = cannons;
    }

    public int getPylons() {
        return pylons;
    }

    public void setPylons(int pylons) {
        this.pylons = pylons;
    }

    public int getNexus() {
        return nexus;
    }

    public void setNexus(int nexus) {
        this.nexus = nexus;
    }

    public int getGateways() {
        return gateways;
    }

    public void setGateways(int gateways) {
        this.gateways = gateways;
    }

    public int getCyberneticsCores() {
        return cyberneticsCores;
    }

    public void setCyberneticsCores(int cyberneticsCores) {
        this.cyberneticsCores = cyberneticsCores;
    }

    public int getStargates() {
        return stargates;
    }

    public void setStargates(int stargates) {
        this.stargates = stargates;
    }

    public int getVoidrays() {
        return voidrays;
    }

    public void setVoidrays(int voidrays) {
        this.voidrays = voidrays;
    }

    public int getSupplyUsed() {
        return supplyUsed;
    }

    public void setSupplyUsed(int supplyUsed) {
        this.supplyUsed = supplyUsed;
    }

    public int getSupplyCap() {
        return supplyCap;
    }

    public void setSupplyCap(int supplyCap) {
        this.supplyCap = supplyCap;
    }

    @Override
    public String toString() {
        return "###################################################################" + "\n" +
                "Iteration: " + iteration + "\n" +
                "Workers: " + workers + "\n" +
                "Idle Workers: " + idleWorkers + "\n" +
                "Mineralien: " + minerals + "\n" +
                "Gas: " + gas + "\n" +
                "Cannons: " + cannons + "\n" +
                "Pylons: " + pylons + "\n" +
                "Nexus: " + nexus + "\n" +
                "Gateways: " + gateways + "\n" +
                "Cybernetics Cores: " + cyberneticsCores + "\n" +
                "Stargates: " + stargates + "\n" +
                "Voidrays: " + voidrays + "\n" +
                "Supply Used: " + supplyUsed + "\n" +
                "Supply Cap: " + supplyCap + "\n" +
                "###################################################################";
    }

}
