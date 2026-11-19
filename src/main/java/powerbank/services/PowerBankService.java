package powerbank.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import powerbank.bst.PowerBankBST;
import powerbank.linkedlist.RentalHistoryList;
import powerbank.linkedlist.StatusHistoryList;
import powerbank.models.PowerBank;
import powerbank.models.RentalRecord;
import powerbank.models.Reservation;
import powerbank.models.Station;
import powerbank.models.StatusHistory;
import powerbank.queue.ReservationQueue;
import powerbank.sorting.Sorting;

public class PowerBankService {
    private final PowerBankBST powerBanks = new PowerBankBST();
    private final ReservationQueue reservations = new ReservationQueue();
    private final RentalHistoryList rentalHistory = new RentalHistoryList();
    private final StatusHistoryList statusHistory = new StatusHistoryList();

    private Station[] stations = new Station[10];
    private int stationCount;
    private ActiveRental[] activeRentals = new ActiveRental[10];
    private int activeRentalCount;
    private int nextReservationId = 1;
    private int nextRentalId = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static class ActiveRental {
        int customerId;
        int powerBankId;
        int startStationId;
        LocalDateTime startTime;

        ActiveRental(int customerId, int powerBankId, int stationId, LocalDateTime startTime) {
            this.customerId = customerId;
            this.powerBankId = powerBankId;
            this.startStationId = stationId;
            this.startTime = startTime;
        }
    }

    public PowerBankService() {
        addStation(new Station(1, "Main Gate", 0, 0, true, 10.0));
        addStation(new Station(2, "Library", 0, 0, true, 12.0));
        addStation(new Station(3, "Food Court", 0, 0, true, 8.0));
        addPowerBank(101, 1);
        addPowerBank(102, 1);
        addPowerBank(103, 1);
        addPowerBank(201, 2);
        addPowerBank(202, 2);
        addPowerBank(301, 3);
        addPowerBank(302, 3);
    }

    public PowerBankBST getPowerBanks() { return powerBanks; }
    public ReservationQueue getReservations() { return reservations; }
    public RentalHistoryList getRentalHistory() { return rentalHistory; }
    public StatusHistoryList getStatusHistory() { return statusHistory; }

    public boolean addStation(Station station) {
        if (station == null || station.getId() <= 0 || station.getName() == null
                || station.getName().trim().isEmpty() || station.getPricePerHour() < 0
                || findStation(station.getId()) != null) return false;
        ensureStationCapacity();
        stations[stationCount++] = station;
        return true;
    }

    public Station findStation(int id) {
        for (int i = 0; i < stationCount; i++)
            if (stations[i].getId() == id) return stations[i];
        return null;
    }

    public boolean updateStation(int id, String name, double price) {
        Station station = findStation(id);
        if (station == null || name == null || name.trim().isEmpty() || price < 0) return false;
        station.setName(name);
        station.setPricePerHour(price);
        return true;
    }

    public boolean deactivateStation(int id) {
        Station station = findStation(id);
        if (station == null) return false;
        station.setActive(false);
        return true;
    }

    public void displayStations() {
        if (stationCount == 0) { System.out.println("No stations found."); return; }
        for (int i = 0; i < stationCount; i++) System.out.println(stations[i]);
    }

    public Station[] getStationsSortedByAvailability() {
        Station[] result = new Station[stationCount];
        for (int i = 0; i < stationCount; i++) result[i] = stations[i];
        Sorting.sortStationsByAvailability(result);
        return result;
    }

    public boolean addPowerBank(int id, int stationId) {
        Station station = findStation(stationId);
        if (id <= 0 || station == null || !station.isActive() || powerBanks.search(id) != null) return false;
        powerBanks.insert(new PowerBank(id, "AVAILABLE", stationId));
        station.setTotalPowerBanks(station.getTotalPowerBanks() + 1);
        station.setAvailablePowerBanks(station.getAvailablePowerBanks() + 1);
        return true;
    }

    public PowerBank searchPowerBank(int id) {
        return powerBanks.search(id);
    }

    public boolean updatePowerBankStatus(int id, String newStatus) {
        PowerBank bank = powerBanks.search(id);
        if (bank == null || newStatus == null) return false;
        String status = newStatus.trim().toUpperCase();
        if (!status.equals("AVAILABLE") && !status.equals("MAINTENANCE")) return false;
        if (bank.getStatus().equals("RENTED") || bank.getStatus().equals("RESERVED")) return false;
        if (bank.getStatus().equals(status)) return true;
        Station station = findStation(bank.getStationId());
        if (station != null) {
            if (bank.getStatus().equals("AVAILABLE") && status.equals("MAINTENANCE"))
                station.setAvailablePowerBanks(Math.max(0, station.getAvailablePowerBanks() - 1));
            else if (bank.getStatus().equals("MAINTENANCE") && status.equals("AVAILABLE"))
                station.setAvailablePowerBanks(station.getAvailablePowerBanks() + 1);
        }
        changeStatus(bank, status);
        return true;
    }

    public boolean reservePowerBank(int customerId, int stationId) {
        Station station = findStation(stationId);
        if (customerId <= 0 || station == null || !station.isActive()) return false;
        PowerBank bank = findAvailableBank(stationId);
        if (bank == null) return false;
        changeStatus(bank, "RESERVED");
        station.setAvailablePowerBanks(Math.max(0, station.getAvailablePowerBanks() - 1));
        Reservation reservation = new Reservation(nextReservationId++, customerId, stationId,
                bank.getId(), LocalDateTime.now().format(formatter), "PENDING");
        reservations.enqueue(reservation);
        return true;
    }

    public boolean startRental(int customerId, int powerBankId) {
        PowerBank bank = powerBanks.search(powerBankId);
        if (bank == null || customerId <= 0) return false;
        Reservation reservation = reservations.findByPowerBank(customerId, powerBankId);
        if ("RESERVED".equals(bank.getStatus())) {
            if (reservation == null) return false;
            reservation.setStatus("COMPLETED");
            reservations.removeById(reservation.getId());
        } else if (!"AVAILABLE".equals(bank.getStatus())) {
            return false;
        } else {
            Station station = findStation(bank.getStationId());
            if (station != null) station.setAvailablePowerBanks(Math.max(0, station.getAvailablePowerBanks() - 1));
        }
        changeStatus(bank, "RENTED");
        addActiveRental(new ActiveRental(customerId, powerBankId, bank.getStationId(), LocalDateTime.now()));
        return true;
    }

    public boolean returnPowerBank(int customerId, int powerBankId, int returnStationId) {
        PowerBank bank = powerBanks.search(powerBankId);
        Station returnStation = findStation(returnStationId);
        int activeIndex = findActiveRental(customerId, powerBankId);
        if (bank == null || returnStation == null || !returnStation.isActive()
                || !"RENTED".equals(bank.getStatus()) || activeIndex < 0) return false;

        ActiveRental active = activeRentals[activeIndex];
        LocalDateTime end = LocalDateTime.now();
        long minutes = Math.max(1, Duration.between(active.startTime, end).toMinutes());
        long hours = Math.max(1, (minutes + 59) / 60);
        Station startStation = findStation(active.startStationId);
        double rate = startStation == null ? returnStation.getPricePerHour() : startStation.getPricePerHour();
        double cost = hours * rate;

        RentalRecord record = new RentalRecord(nextRentalId++, customerId, powerBankId,
                active.startStationId, returnStationId, active.startTime.format(formatter),
                end.format(formatter), cost);
        rentalHistory.insert(record);

        if (active.startStationId != returnStationId && startStation != null)
            startStation.setTotalPowerBanks(Math.max(0, startStation.getTotalPowerBanks() - 1));
        if (active.startStationId != returnStationId)
            returnStation.setTotalPowerBanks(returnStation.getTotalPowerBanks() + 1);

        bank.setStationId(returnStationId);
        changeStatus(bank, "AVAILABLE");
        returnStation.setAvailablePowerBanks(returnStation.getAvailablePowerBanks() + 1);
        removeActiveRental(activeIndex);
        return true;
    }

    public boolean cancelReservation(int reservationId) {
        Reservation[] all = reservations.toArray();
        for (Reservation reservation : all) {
            if (reservation.getId() == reservationId && "PENDING".equals(reservation.getStatus())) {
                reservation.setStatus("CANCELLED");
                reservations.removeById(reservationId);
                PowerBank bank = powerBanks.search(reservation.getPowerBankId());
                if (bank != null && "RESERVED".equals(bank.getStatus())) {
                    changeStatus(bank, "AVAILABLE");
                    Station station = findStation(bank.getStationId());
                    if (station != null) station.setAvailablePowerBanks(station.getAvailablePowerBanks() + 1);
                }
                return true;
            }
        }
        return false;
    }

    public boolean cancelNextReservation() {
        Reservation reservation = reservations.peek();
        return reservation != null && cancelReservation(reservation.getId());
    }

    public void displayInventory() { powerBanks.displayInOrder(); }
    public void displayRentalHistory() { rentalHistory.display(); }
    public void displayStatusHistory() { statusHistory.display(); }
    public void displayReservations() { reservations.display(); }

    public PowerBank[] getPowerBanksSortedById() {
        PowerBank[] result = new PowerBank[powerBanks.count()];
        powerBanks.copyToArray(result, new int[]{0});
        Sorting.sortPowerBanksById(result);
        return result;
    }

    public Reservation[] getReservationsSortedByDate() {
        Reservation[] result = reservations.toArray();
        Sorting.sortReservationsByDate(result);
        return result;
    }

    public RentalRecord[] getRentalsSortedByDate() {
        RentalRecord[] result = rentalHistory.toArray();
        Sorting.sortRentalsByDate(result);
        return result;
    }

    public void displayReports() {
        int total = 0;
        int available = 0;
        int rented = 0;
        int reserved = 0;
        int maintenance = 0;
        for (PowerBank bank : getPowerBanksSortedById()) {
            total++;
            if ("AVAILABLE".equals(bank.getStatus())) available++;
            else if ("RENTED".equals(bank.getStatus())) rented++;
            else if ("RESERVED".equals(bank.getStatus())) reserved++;
            else if ("MAINTENANCE".equals(bank.getStatus())) maintenance++;
        }
        System.out.println("Stations: " + stationCount);
        System.out.println("Total PowerBanks: " + total);
        System.out.println("Available: " + available);
        System.out.println("Rented: " + rented);
        System.out.println("Reserved: " + reserved);
        System.out.println("Maintenance: " + maintenance);
        System.out.println("Pending reservations: " + reservations.count());
        System.out.println("Completed rentals: " + rentalHistory.count());
    }

    private PowerBank findAvailableBank(int stationId) {
        for (PowerBank bank : getPowerBanksSortedById()) {
            if (bank.getStationId() == stationId && "AVAILABLE".equals(bank.getStatus())) return bank;
        }
        return null;
    }

    private void changeStatus(PowerBank bank, String newStatus) {
        String oldStatus = bank.getStatus();
        bank.setStatus(newStatus);
        statusHistory.insert(new StatusHistory(bank.getId(), oldStatus, newStatus,
                LocalDateTime.now().format(formatter)));
    }

    private void ensureStationCapacity() {
        if (stationCount < stations.length) return;
        Station[] bigger = new Station[stations.length * 2];
        for (int i = 0; i < stations.length; i++) bigger[i] = stations[i];
        stations = bigger;
    }

    private void addActiveRental(ActiveRental rental) {
        if (activeRentalCount == activeRentals.length) {
            ActiveRental[] bigger = new ActiveRental[activeRentals.length * 2];
            for (int i = 0; i < activeRentals.length; i++) bigger[i] = activeRentals[i];
            activeRentals = bigger;
        }
        activeRentals[activeRentalCount++] = rental;
    }

    private int findActiveRental(int customerId, int powerBankId) {
        for (int i = 0; i < activeRentalCount; i++)
            if (activeRentals[i].customerId == customerId && activeRentals[i].powerBankId == powerBankId) return i;
        return -1;
    }

    private void removeActiveRental(int index) {
        for (int i = index; i < activeRentalCount - 1; i++) activeRentals[i] = activeRentals[i + 1];
        activeRentals[--activeRentalCount] = null;
    }
}
