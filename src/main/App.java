package main;

import controller.ScheduleController;
import util.CalendarTextHelper;

import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // ✅ Generate calendar file if needed
        CalendarTextHelper.generateYearCalendar(2025, "data/calendar_2025.txt");

        // ✅ Display calendar in console
        try (Scanner sc = new Scanner(new File("data/calendar_2025.txt"))) {
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
        } catch (java.io.IOException e) {
            System.out.println("❌ Calendar file not found.");
        }

        // ✅ Launch console planner
        new ScheduleController().run();
    }
}
