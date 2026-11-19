package powerbank.models;

public class Station {
    private int id;
    private String name;
    private int totalPowerBanks;
    private int availablePowerBanks;
    private boolean active;
    private double pricePerHour;

    public Station(int id, String name, int total, int available) {
        this(id, name, total, available, true, 10.0);
    }

    public Station(int id, String name, int total, int available, boolean active, double pricePerHour) {
        this.id = id;
        this.name = name;
        this.totalPowerBanks = total;
        this.availablePowerBanks = available;
        this.active = active;
        this.pricePerHour = pricePerHour;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getTotalPowerBanks() { return totalPowerBanks; }
    public int getAvailablePowerBanks() { return availablePowerBanks; }
    public boolean isActive() { return active; }
    public double getPricePerHour() { return pricePerHour; }

    public void setName(String name) { this.name = name; }
    public void setAvailablePowerBanks(int n) { this.availablePowerBanks = n; }
    public void setTotalPowerBanks(int n) { this.totalPowerBanks = n; }
    public void setActive(boolean active) { this.active = active; }
    public void setPricePerHour(double price) { this.pricePerHour = price; }

    @Override
    public String toString() {
        return "Station{id=" + id + ", name=" + name + ", available="
                + availablePowerBanks + "/" + totalPowerBanks + ", active=" + active
                + ", price/hour=" + pricePerHour + "}";
    }
}
