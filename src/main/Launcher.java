package main;
import java.util.Scanner;

import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("🚀 Choose mode:");
        System.out.println("1. Console");
        System.out.println("2. Calendar GUI");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            App.main(null); // ✅ Launch console
        } else if (choice.equals("2")) {
            CalendarView.launch(); // ✅ Launch calendar GUI
        } else {
            System.out.println("❌ Invalid choice. Exiting.");
        }
    }
}
