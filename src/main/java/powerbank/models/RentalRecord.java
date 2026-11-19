package powerbank.models;

public class RentalRecord {
    private int id;
    private int customerId;
    private int powerBankId;
    private int startStationId;
    private int returnStationId;
    private String startTime;
    private String endTime;
    private double cost;

    public RentalRecord(int id, int customerId, int powerBankId, int startStationId,
                        int returnStationId, String start, String end, double cost) {
        this.id = id;
        this.customerId = customerId;
        this.powerBankId = powerBankId;
        this.startStationId = startStationId;
        this.returnStationId = returnStationId;
        this.startTime = start;
        this.endTime = end;
        this.cost = cost;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getPowerBankId() { return powerBankId; }
    public int getStartStationId() { return startStationId; }
    public int getReturnStationId() { return returnStationId; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public double getCost() { return cost; }

    @Override
    public String toString() {
        return "Rental{id=" + id + ", customer=" + customerId + ", powerBank=" + powerBankId
                + ", from=" + startStationId + ", returnedTo=" + returnStationId
                + ", start=" + startTime + ", end=" + endTime + ", cost=" + cost + "}";
    }
}
