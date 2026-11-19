package powerbank.linkedlist;

import powerbank.models.StatusHistory;

public class StatusHistoryList {
    private ListNode head;

    public void insert(StatusHistory history) {
        if (history == null) return;
        ListNode node = new ListNode(history);
        if (head == null) { head = node; return; }
        ListNode current = head;
        while (current.next != null) current = current.next;
        current.next = node;
    }

    public StatusHistory search(int powerBankId) {
        ListNode current = head;
        while (current != null) {
            StatusHistory item = (StatusHistory) current.data;
            if (item.getPowerBankId() == powerBankId) return item;
            current = current.next;
        }
        return null;
    }

    public boolean delete(int powerBankId) {
        if (head == null) return false;
        if (((StatusHistory) head.data).getPowerBankId() == powerBankId) {
            head = head.next;
            return true;
        }
        ListNode current = head;
        while (current.next != null) {
            if (((StatusHistory) current.next.data).getPowerBankId() == powerBankId) {
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

    public void display() {
        if (head == null) { System.out.println("No status history found."); return; }
        ListNode current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
