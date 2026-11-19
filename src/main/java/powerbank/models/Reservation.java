package powerbank.models;

public class Reservation {
    private int id;
    private int customerId;
    private int stationId;
    private int powerBankId;
    private String reservationDate;
    private String status;

    public Reservation(int id, int customerId, int stationId, String date, String status) {
        this(id, customerId, stationId, -1, date, status);
    }

    public Reservation(int id, int customerId, int stationId, int powerBankId, String date, String status) {
        this.id = id;
        this.customerId = customerId;
        this.stationId = stationId;
        this.powerBankId = powerBankId;
        this.reservationDate = date;
        this.status = status;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getStationId() { return stationId; }
    public int getPowerBankId() { return powerBankId; }
    public String getReservationDate() { return reservationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Reservation{id=" + id + ", customer=" + customerId + ", station=" + stationId
                + ", powerBank=" + powerBankId + ", date=" + reservationDate + ", status=" + status + "}";
    }
}
