package controller;

import model.Schedule;
import model.Task;
import util.FileManager;
import model.SpecialDay;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ScheduleController {
    private final Schedule schedule = new Schedule();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        FileManager.load(schedule, "data/schedule.txt");

        // ✅ Load regular tasks for today
        FileManager.loadRegularTasks(schedule, "data/regular_tasks.txt", LocalDateTime.now());

        // ✅ Load and show today's special days
        List<SpecialDay> specialDays = FileManager.loadSpecialDays("data/special_days.txt");
        System.out.println("\n🎉 Special Days Today:");
        boolean found = false;
        for (SpecialDay sd : specialDays) {
            if (sd.getDate().equals(LocalDate.now())) {
                System.out.println("- " + sd.getTitle());
                found = true;
            }
        }
        if (!found) {
            System.out.println("None scheduled.");
        }

        if (new java.io.File("data/regular_tasks.txt").length() == 0) {
            setupRegularTasks();
        }

        while (true) {
            System.out.println("\n1. Add Task\n2. View Tasks\n3. View Free Time Today\n4. Remove Task\n5. Add Regular Task\n6. Save & Exit\n7. Add special days\n8. View special days");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> addTask();
                case 2 -> schedule.showAllTasks();
                case 3 -> suggestFreeSlots(LocalDateTime.now());
                case 4 -> removeTask();
                case 5 -> addRegularTask();
                case 6 -> {
                    FileManager.save(schedule, "data/schedule.txt");
                    System.out.println("👋 Goodbye!");
                    return;
                }
                case 7 -> addSpecialDay();
                case 8 -> viewSpecialDays();
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    private void setupRegularTasks() {
        System.out.println("➕ Add new regular tasks (existing ones will be kept).");

        List<Task> newRegulars = new ArrayList<>();
        while (true) {
            System.out.print("Task name (or 'done'): ");
            String title = scanner.nextLine();
            if (title.equalsIgnoreCase("done")) break;

            System.out.print("Start time (HH:mm): ");
            LocalTime start = LocalTime.parse(scanner.nextLine());
            System.out.print("End time (HH:mm): ");
            LocalTime end = LocalTime.parse(scanner.nextLine());

            newRegulars.add(new Task(title, LocalDateTime.now().with(start), LocalDateTime.now().with(end), true));
        }

        if (!newRegulars.isEmpty()) {
            FileManager.appendRegularTasks(newRegulars, "data/regular_tasks.txt");
            System.out.println("✅ New regular tasks added.");
        } else {
            System.out.println("ℹ️ No new tasks entered.");
        }
    }

    private void addTask() {
        FileManager.loadRegularTasks(schedule, "data/regular_tasks.txt", LocalDateTime.now());

        System.out.println("📌 Suggested free slots:");
        suggestFreeSlots(LocalDateTime.now());

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Start (yyyy-MM-ddTHH:mm): ");
        LocalDateTime start = LocalDateTime.parse(scanner.nextLine());

        System.out.print("End (yyyy-MM-ddTHH:mm): ");
        LocalDateTime end = LocalDateTime.parse(scanner.nextLine());

        Task task = new Task(title, start, end, false);
        schedule.addTask(task);
    }

    private void suggestFreeSlots(LocalDateTime day) {
        List<Task> all = schedule.getTasks().stream()
                .filter(t -> t.getStart().toLocalDate().equals(day.toLocalDate()))
                .sorted(Comparator.comparing(Task::getStart))
                .toList();

        LocalTime last = LocalTime.of(6, 0);
        for (Task t : all) {
            LocalTime taskStart = t.getStart().toLocalTime();
            if (last.isBefore(taskStart)) {
                System.out.println("🕒 Free: " + last + " to " + taskStart);
            }
            last = t.getEnd().toLocalTime();
        }

        if (last.isBefore(LocalTime.of(22, 0))) {
            System.out.println("🕒 Free: " + last + " to 22:00");
        }
    }

    private void removeTask() {
        List<Task> tasks = schedule.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("📭 No tasks to remove.");
            return;
        }

        System.out.println("🗑️ Select a task to remove:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }

        System.out.print("Enter task number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            Task removed = schedule.removeTask(index);

            if (removed != null && removed.isRegular()) {
                FileManager.removeRegularTask(removed, "data/regular_tasks.txt");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input.");
        }
    }

    private void addRegularTask() {
        System.out.println("➕ Add a new regular task:");

        System.out.print("Task name: ");
        String title = scanner.nextLine();

        System.out.print("Start time (HH:mm): ");
        LocalTime start = LocalTime.parse(scanner.nextLine());

        System.out.print("End time (HH:mm): ");
        LocalTime end = LocalTime.parse(scanner.nextLine());

        Task task = new Task(title, LocalDateTime.now().with(start), LocalDateTime.now().with(end), true);
        FileManager.appendRegularTasks(List.of(task), "data/regular_tasks.txt");

        FileManager.loadRegularTasks(schedule, "data/regular_tasks.txt", LocalDateTime.now());

        System.out.println("✅ Regular task added.");
    }

    private void addSpecialDay() {
        System.out.print("Enter title (e.g., Mom's Birthday): ");
        String title = scanner.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        SpecialDay day = new SpecialDay(title, date);
        FileManager.saveSpecialDay(day, "data/special_days.txt");

        System.out.println("✅ Special day saved.");
    }

    private void viewSpecialDays() {
        List<SpecialDay> days = FileManager.loadSpecialDays("data/special_days.txt");
        if (days.isEmpty()) {
            System.out.println("📭 No special days saved.");
        } else {
            System.out.println("📅 Special Days:");
            for (SpecialDay d : days) {
                System.out.println("• " + d);
            }
        }
    }
}
