package powerbank.linkedlist;

import powerbank.models.RentalRecord;

public class RentalHistoryList {
    private ListNode head;

    public void insert(RentalRecord record) {
        if (record == null) return;
        ListNode node = new ListNode(record);
        if (head == null) { head = node; return; }
        ListNode current = head;
        while (current.next != null) current = current.next;
        current.next = node;
    }

    public RentalRecord search(int id) {
        ListNode current = head;
        while (current != null) {
            RentalRecord record = (RentalRecord) current.data;
            if (record.getId() == id) return record;
            current = current.next;
        }
        return null;
    }

    public boolean update(int id, RentalRecord updated) {
        if (updated == null) return false;
        ListNode current = head;
        while (current != null) {
            RentalRecord record = (RentalRecord) current.data;
            if (record.getId() == id) {
                current.data = updated;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean delete(int id) {
        if (head == null) return false;
        if (((RentalRecord) head.data).getId() == id) { head = head.next; return true; }
        ListNode current = head;
        while (current.next != null) {
            if (((RentalRecord) current.next.data).getId() == id) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int count() {
        int result = 0;
        ListNode current = head;
        while (current != null) { result++; current = current.next; }
        return result;
    }

    public RentalRecord[] toArray() {
        RentalRecord[] result = new RentalRecord[count()];
        ListNode current = head;
        int i = 0;
        while (current != null) {
            result[i++] = (RentalRecord) current.data;
            current = current.next;
        }
        return result;
    }

    public void display() {
        if (head == null) { System.out.println("No rental records found."); return; }
        ListNode current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
