package powerbank;

import powerbank.bst.PowerBankBST;
import powerbank.linkedlist.RentalHistoryList;
import powerbank.models.PowerBank;
import powerbank.models.RentalRecord;
import powerbank.models.Reservation;
import powerbank.queue.ReservationQueue;
import powerbank.services.PowerBankService;
import powerbank.sorting.Sorting;

public class ProjectTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testBSTInsertSearchDelete();
        testBSTUpdate();
        testQueueFIFOAndEmpty();
        testLinkedListOperations();
        testSorting();
        testReservationRentalReturn();
        System.out.println("\\nRESULT: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) System.exit(1);
    }

    private static void check(String name, boolean condition) {
        if (condition) { passed++; System.out.println("PASS: " + name); }
        else { failed++; System.out.println("FAIL: " + name); }
    }

    private static void testBSTInsertSearchDelete() {
        PowerBankBST tree = new PowerBankBST();
        tree.insert(new PowerBank(20, "AVAILABLE", 1));
        tree.insert(new PowerBank(10, "AVAILABLE", 1));
        tree.insert(new PowerBank(30, "AVAILABLE", 1));
        check("BST insert and search", tree.search(10) != null && tree.search(99) == null);
        check("BST delete root with two children", tree.delete(20) && tree.search(20) == null && tree.count() == 2);
    }

    private static void testBSTUpdate() {
        PowerBankBST tree = new PowerBankBST();
        tree.insert(new PowerBank(5, "AVAILABLE", 1));
        check("BST update", tree.update(5, new PowerBank(6, "MAINTENANCE", 2))
                && tree.search(6) != null && tree.search(5) == null);
    }

    private static void testQueueFIFOAndEmpty() {
        ReservationQueue queue = new ReservationQueue();
        check("Empty queue dequeue", queue.dequeue() == null);
        queue.enqueue(new Reservation(1, 10, 1, "2026-01-01", "PENDING"));
        queue.enqueue(new Reservation(2, 20, 1, "2026-01-02", "PENDING"));
        check("Queue FIFO", queue.dequeue().getId() == 1 && queue.dequeue().getId() == 2 && queue.isEmpty());
    }

    private static void testLinkedListOperations() {
        RentalHistoryList list = new RentalHistoryList();
        RentalRecord a = new RentalRecord(1, 10, 101, 1, 1, "start", "end", 10);
        list.insert(a);
        check("Linked list insert/search", list.search(1) == a);
        RentalRecord replacement = new RentalRecord(1, 10, 101, 1, 2, "start", "end", 20);
        check("Linked list update", list.update(1, replacement) && list.search(1).getCost() == 20);
        check("Linked list delete", list.delete(1) && list.search(1) == null);
    }

    private static void testSorting() {
        PowerBank[] banks = {
            new PowerBank(30, "AVAILABLE", 1),
            new PowerBank(10, "AVAILABLE", 1),
            new PowerBank(20, "AVAILABLE", 1)
        };
        Sorting.sortPowerBanksById(banks);
        check("Insertion sort PowerBanks", banks[0].getId() == 10 && banks[1].getId() == 20 && banks[2].getId() == 30);
    }

    private static void testReservationRentalReturn() {
        PowerBankService service = new PowerBankService();
        check("Reservation succeeds", service.reservePowerBank(50, 1));
        Reservation reservation = service.getReservations().peek();
        check("Reservation stored in queue", reservation != null && reservation.getCustomerId() == 50);
        check("Reserved rental starts", service.startRental(50, reservation.getPowerBankId()));
        check("Return succeeds", service.returnPowerBank(50, reservation.getPowerBankId(), 2));
        check("Rental history saved", service.getRentalHistory().count() == 1);
        check("Status history saved", service.getStatusHistory().count() >= 3);
    }
}
