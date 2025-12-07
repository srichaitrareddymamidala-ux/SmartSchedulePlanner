package util;

import model.Schedule;
import model.Task;
import model.SpecialDay;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class FileManager {

    public static void load(Schedule schedule, String filePath) {
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String title = parts[0];
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                boolean isRegular = Boolean.parseBoolean(parts[3]);
                schedule.addTask(new Task(title, start, end, isRegular));
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not load schedule.");
        }
    }

    public static void save(Schedule schedule, String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Task t : schedule.getTasks()) {
                pw.println(t.toFileString()); // ✅ use consistent format
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not save schedule.");
        }
    }

    public static void saveRegularTasks(List<Task> tasks, String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Task t : tasks) {
                pw.println(t.toFileString()); // ✅ use consistent format
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not save regular tasks.");
        }
    }

    public static void loadRegularTasks(Schedule schedule, String filePath, LocalDateTime day) {
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String title = parts[0];
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                boolean isRegular = Boolean.parseBoolean(parts[3]);

                Task newTask = new Task(title, day.with(start.toLocalTime()), day.with(end.toLocalTime()), isRegular);

                boolean exists = schedule.getTasks().stream().anyMatch(t ->
                        t.getTitle().equals(newTask.getTitle()) &&
                                t.getStart().toLocalTime().equals(newTask.getStart().toLocalTime()) &&
                                t.getEnd().toLocalTime().equals(newTask.getEnd().toLocalTime()) &&
                                t.isRegular()
                );

                if (!exists) {
                    schedule.addTask(newTask);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not load regular tasks.");
        }
    }

    public static void appendRegularTasks(List<Task> tasks, String filePath) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(filePath, true))) {
            for (Task t : tasks) {
                out.println(t.toFileString()); // ✅ use consistent format
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not append regular tasks.");
        }
    }

    public static void removeRegularTask(Task taskToRemove, String filePath) {
        File inputFile = new File(filePath);
        File tempFile = new File("data/temp_regular_tasks.txt");

        try (
                Scanner sc = new Scanner(inputFile);
                PrintWriter pw = new PrintWriter(tempFile)
        ) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String title = parts[0];
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);

                boolean isSame = title.equals(taskToRemove.getTitle()) &&
                        start.toLocalTime().equals(taskToRemove.getStart().toLocalTime()) &&
                        end.toLocalTime().equals(taskToRemove.getEnd().toLocalTime());

                if (!isSame) {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not update regular tasks file.");
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("⚠️ Could not finalize regular task removal.");
        }
    }

    public static void saveSpecialDay(SpecialDay day, String filePath) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(filePath, true))) {
            out.println(day.getTitle() + "," + day.getDate());
        } catch (IOException e) {
            System.out.println("⚠️ Could not save special day.");
        }
    }

    public static List<SpecialDay> loadSpecialDays(String filePath) {
        List<SpecialDay> days = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 2) {
                    days.add(new SpecialDay(parts[0], LocalDate.parse(parts[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not load special days.");
        }
        return days;
    }

    public static List<Task> loadRegularTasks(String filename) {
        List<Task> regulars = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task t = Task.fromString(line);
                if (t != null && t.isRegular()) {
                    regulars.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading regular tasks: " + e.getMessage());
        }
        return regulars;
    }

    public static void saveRegularTask(Task task, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(task.toFileString()); // ✅ use consistent format
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving regular task: " + e.getMessage());
        }
    }
}
