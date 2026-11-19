package powerbank;

import java.util.Scanner;
import powerbank.models.PowerBank;
import powerbank.models.Reservation;
import powerbank.models.RentalRecord;
import powerbank.models.Station;
import powerbank.services.PowerBankService;
import powerbank.utils.InputHelper;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputHelper input = new InputHelper(scanner);
        PowerBankService service = new PowerBankService();
        int choice;

        do {
            System.out.println("\\n========== POWERBANK STATIONS ==========");
            System.out.println("1. PowerBank Management");
            System.out.println("2. Reservation Management");
            System.out.println("3. Rental Management");
            System.out.println("4. Station Management");
            System.out.println("5. History");
            System.out.println("6. Sorting");
            System.out.println("7. Reports");
            System.out.println("0. Exit");
            choice = input.readInt("Choose: ");

            switch (choice) {
                case 1: powerBankMenu(input, service); break;
                case 2: reservationMenu(input, service); break;
                case 3: rentalMenu(input, service); break;
                case 4: stationMenu(input, service); break;
                case 5: historyMenu(input, service); break;
                case 6: sortingMenu(input, service); break;
                case 7: service.displayReports(); break;
                case 0: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 0);
        scanner.close();
    }

    private static void powerBankMenu(InputHelper in, PowerBankService service) {
        int choice;
        do {
            System.out.println("\\n--- POWERBANKS ---");
            System.out.println("1. Display all (BST InOrder)");
            System.out.println("2. Search by ID");
            System.out.println("3. Add PowerBank");
            System.out.println("4. Change status (Available / Maintenance)");
            System.out.println("0. Back");
            choice = in.readInt("Choose: ");
            switch (choice) {
                case 1: service.displayInventory(); break;
                case 2: {
                    PowerBank bank = service.searchPowerBank(in.readInt("PowerBank ID: "));
                    System.out.println(bank == null ? "Not found." : bank);
                    break;
                }
                case 3: {
                    int id = in.readInt("PowerBank ID: ");
                    int station = in.readInt("Station ID: ");
                    System.out.println(service.addPowerBank(id, station) ? "Added." : "Could not add.");
                    break;
                }
                case 4: {
                    int id = in.readInt("PowerBank ID: ");
                    String status = in.readLine("New status (AVAILABLE/MAINTENANCE): ");
                    System.out.println(service.updatePowerBankStatus(id, status) ? "Updated." : "Could not update.");
                    break;
                }
                case 0: break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void reservationMenu(InputHelper in, PowerBankService service) {
        int choice;
        do {
            System.out.println("\\n--- RESERVATIONS (FIFO QUEUE) ---");
            System.out.println("1. Make reservation");
            System.out.println("2. Display queue");
            System.out.println("3. Cancel reservation by ID");
            System.out.println("4. Cancel next reservation");
            System.out.println("0. Back");
            choice = in.readInt("Choose: ");
            switch (choice) {
                case 1: {
                    int customer = in.readInt("Customer ID: ");
                    int station = in.readInt("Station ID: ");
                    System.out.println(service.reservePowerBank(customer, station) ? "Reservation created." : "No available PowerBank or invalid data.");
                    break;
                }
                case 2: service.displayReservations(); break;
                case 3: {
                    int id = in.readInt("Reservation ID: ");
                    System.out.println(service.cancelReservation(id) ? "Cancelled." : "Reservation not found.");
                    break;
                }
                case 4: System.out.println(service.cancelNextReservation() ? "Next reservation cancelled." : "Queue is empty."); break;
                case 0: break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void rentalMenu(InputHelper in, PowerBankService service) {
        int choice;
        do {
            System.out.println("\\n--- RENTALS ---");
            System.out.println("1. Start rental");
            System.out.println("2. Return PowerBank");
            System.out.println("3. Display rental history");
            System.out.println("0. Back");
            choice = in.readInt("Choose: ");
            switch (choice) {
                case 1: {
                    int customer = in.readInt("Customer ID: ");
                    int bank = in.readInt("PowerBank ID: ");
                    System.out.println(service.startRental(customer, bank) ? "Rental started." : "Could not start rental.");
                    break;
                }
                case 2: {
                    int customer = in.readInt("Customer ID: ");
                    int bank = in.readInt("PowerBank ID: ");
                    int station = in.readInt("Return station ID: ");
                    System.out.println(service.returnPowerBank(customer, bank, station) ? "Returned successfully." : "Return failed.");
                    break;
                }
                case 3: service.displayRentalHistory(); break;
                case 0: break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void stationMenu(InputHelper in, PowerBankService service) {
        int choice;
        do {
            System.out.println("\\n--- STATIONS ---");
            System.out.println("1. Display stations");
            System.out.println("2. Add station");
            System.out.println("3. Update station name and price");
            System.out.println("4. Deactivate station");
            System.out.println("5. Sort by available PowerBanks");
            System.out.println("0. Back");
            choice = in.readInt("Choose: ");
            switch (choice) {
                case 1: service.displayStations(); break;
                case 2: {
                    int id = in.readInt("Station ID: ");
                    String name = in.readLine("Station name: ");
                    double price = in.readDouble("Price per hour: ");
                    System.out.println(service.addStation(new Station(id, name, 0, 0, true, price)) ? "Added." : "Could not add.");
                    break;
                }
                case 3: {
                    int id = in.readInt("Station ID: ");
                    String name = in.readLine("New name: ");
                    double price = in.readDouble("New price per hour: ");
                    System.out.println(service.updateStation(id, name, price) ? "Updated." : "Could not update.");
                    break;
                }
                case 4: {
                    int id = in.readInt("Station ID: ");
                    System.out.println(service.deactivateStation(id) ? "Deactivated." : "Station not found.");
                    break;
                }
                case 5:
                    for (Station station : service.getStationsSortedByAvailability()) System.out.println(station);
                    break;
                case 0: break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void historyMenu(InputHelper in, PowerBankService service) {
        int choice;
        do {
            System.out.println("\\n--- HISTORY ---");
            System.out.println("1. Rental history");
            System.out.println("2. Status change history");
            System.out.println("0. Back");
            choice = in.readInt("Choose: ");
            if (choice == 1) service.displayRentalHistory();
            else if (choice == 2) service.displayStatusHistory();
            else if (choice != 0) System.out.println("Invalid choice.");
        } while (choice != 0);
    }

    private static void sortingMenu(InputHelper input, PowerBankService service) {
        int choice;
        System.out.println("\\n--- SORTING (INSERTION SORT) ---");
        System.out.println("1. PowerBanks by ID");
        System.out.println("2. Stations by availability");
        System.out.println("3. Reservations by date");
        System.out.println("4. Rental records by end date");
        choice = input.readInt("Choose: ");
        switch (choice) {
            case 1:
                for (PowerBank bank : service.getPowerBanksSortedById()) System.out.println(bank);
                break;
            case 2:
                for (Station station : service.getStationsSortedByAvailability()) System.out.println(station);
                break;
            case 3:
                for (Reservation reservation : service.getReservationsSortedByDate()) System.out.println(reservation);
                break;
            case 4:
                for (RentalRecord rental : service.getRentalsSortedByDate()) System.out.println(rental);
                break;
            default: System.out.println("Invalid choice.");
        }
    }
}
