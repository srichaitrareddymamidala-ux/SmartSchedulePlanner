package model;


import java.time.LocalDate;

public class SpecialDay {
    private String title;
    private LocalDate date;

    public SpecialDay(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() { return title; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return title + " on " + date;
    }
    public static SpecialDay fromString(String line) {
        String[] parts = line.split(",");
        String title = parts[0].trim();
        LocalDate date = LocalDate.parse(parts[1].trim());
        return new SpecialDay(title, date);
    }
}
