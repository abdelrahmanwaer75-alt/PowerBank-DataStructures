package powerbank.models;

public class PowerBank {
    private int id;
    private int stationId;
    private String status;

    public PowerBank(int id, String status, int stationId) {
        this.id = id;
        this.status = status;
        this.stationId = stationId;
    }

    public int getId() { return id; }
    public int getStationId() { return stationId; }
    public String getStatus() { return status; }
    public void setId(int id) { this.id = id; }
    public void setStationId(int stationId) { this.stationId = stationId; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "PowerBank{id=" + id + ", status=" + status + ", stationId=" + stationId + "}";
    }
}
