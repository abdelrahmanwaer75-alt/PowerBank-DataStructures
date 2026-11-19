package powerbank.utils;

import java.util.Scanner;

public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) { this.scanner = scanner; }

    public int readInt(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Enter a valid whole number: ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public double readDouble(String message) {
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            scanner.nextLine();
            System.out.print("Enter a valid number: ");
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    public String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
