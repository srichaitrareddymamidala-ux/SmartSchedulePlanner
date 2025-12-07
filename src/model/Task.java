package model;


import java.time.LocalDateTime;

public class Task {
    private final String title;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final boolean isRegular;

    public Task(String title, LocalDateTime start, LocalDateTime end, boolean isRegular) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.isRegular = isRegular;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean isRegular() {
        return isRegular;
    }

    @Override
    public String toString() {
        return (isRegular ? "[Regular] " : "") + title + " | " + start + " to " + end;
    }
    public static Task fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            System.out.println("❌ Invalid task line: " + line);
            return null;
        }

        String title = parts[0].trim();
        LocalDateTime start = LocalDateTime.parse(parts[1].trim());
        LocalDateTime end = LocalDateTime.parse(parts[2].trim());
        boolean isRegular = Boolean.parseBoolean(parts[3].trim());

        return new Task(title, start, end, isRegular);
    }
    public String toFileString() {
        return title + "," + start + "," + end + "," + isRegular;
    }
}
