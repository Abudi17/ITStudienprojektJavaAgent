package model;

public class Request {

    /**
     * Der Spielstatus, in dem sich der Spieler befindet.
     */
    private GameStatus gameStatus;

    /**
     * Default-Konstruktor, der für die JSON Serialisierung und
     * Deserialisierung benötigt wird.
     */
    public Request() {
        this(new GameStatus());
    }

    /**
     * Konstruktor zur Erzeugung eines Request-Objekts.
     *
     * @param gameStatus
     *            Der Spielstatus, in dem sich der anfragende Spieler befindet.
     */
    public Request(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Simpler Setter für den Spielstatus.
     *
     * @param gameStatus
     *            Der neue Spielstatus.
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Simpler Getter für den Spielstatus.
     *
     * @return Der aktuelle Spielstatus der Anfrage.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public String toString() {
        return "Request [gameStatus=" + gameStatus.toString();
    }
}

