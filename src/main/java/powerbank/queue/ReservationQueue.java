package powerbank.queue;

import powerbank.models.Reservation;

public class ReservationQueue {
    private QueueNode front;
    private QueueNode rear;

    public boolean isEmpty() { return front == null; }

    public void enqueue(Reservation reservation) {
        if (reservation == null) return;
        QueueNode node = new QueueNode(reservation);
        if (isEmpty()) front = rear = node;
        else {
            rear.next = node;
            rear = node;
        }
    }

    public Reservation dequeue() {
        if (isEmpty()) return null;
        Reservation result = (Reservation) front.data;
        front = front.next;
        if (front == null) rear = null;
        return result;
    }

    public Reservation peek() {
        return isEmpty() ? null : (Reservation) front.data;
    }

    public Reservation findByPowerBank(int customerId, int powerBankId) {
        QueueNode current = front;
        while (current != null) {
            Reservation reservation = (Reservation) current.data;
            if (reservation.getCustomerId() == customerId
                    && reservation.getPowerBankId() == powerBankId
                    && "PENDING".equals(reservation.getStatus())) return reservation;
            current = current.next;
        }
        return null;
    }

    public boolean removeById(int reservationId) {
        if (front == null) return false;
        if (((Reservation) front.data).getId() == reservationId) {
            dequeue();
            return true;
        }
        QueueNode previous = front;
        QueueNode current = front.next;
        while (current != null) {
            Reservation reservation = (Reservation) current.data;
            if (reservation.getId() == reservationId) {
                previous.next = current.next;
                if (current == rear) rear = previous;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public int count() {
        int count = 0;
        QueueNode current = front;
        while (current != null) { count++; current = current.next; }
        return count;
    }

    public Reservation[] toArray() {
        Reservation[] result = new Reservation[count()];
        QueueNode current = front;
        int i = 0;
        while (current != null) {
            result[i++] = (Reservation) current.data;
            current = current.next;
        }
        return result;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("No reservations in queue.");
            return;
        }
        QueueNode current = front;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
