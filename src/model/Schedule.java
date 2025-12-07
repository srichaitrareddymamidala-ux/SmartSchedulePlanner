package model;


import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void showAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("📭 No tasks scheduled.");
        } else {
            tasks.forEach(System.out::println);
        }
    }
    public Task removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task removed = tasks.remove(index);
            System.out.println("✅ Task removed.");
            return removed;
        } else {
            System.out.println("❌ Invalid task number.");
            return null;
        }
    }
    public void removeTask(String title) {
        tasks.removeIf(t -> t.getTitle().equalsIgnoreCase(title));
    }
}
