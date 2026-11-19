package powerbank.sorting;

import powerbank.models.PowerBank;
import powerbank.models.Station;
import powerbank.models.Reservation;
import powerbank.models.RentalRecord;

public class Sorting {
    private Sorting() { }

    public static void sortPowerBanksById(PowerBank[] array) {
        for (int i = 1; i < array.length; i++) {
            PowerBank key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].getId() > key.getId()) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    public static void sortStationsByAvailability(Station[] array) {
        for (int i = 1; i < array.length; i++) {
            Station key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].getAvailablePowerBanks() < key.getAvailablePowerBanks()) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    // Dates must use yyyy-MM-dd or yyyy-MM-dd HH:mm format.
    public static void sortReservationsByDate(Reservation[] array) {
        for (int i = 1; i < array.length; i++) {
            Reservation key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].getReservationDate().compareTo(key.getReservationDate()) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    public static void sortRentalsByDate(RentalRecord[] array) {
        for (int i = 1; i < array.length; i++) {
            RentalRecord key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].getEndTime().compareTo(key.getEndTime()) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
