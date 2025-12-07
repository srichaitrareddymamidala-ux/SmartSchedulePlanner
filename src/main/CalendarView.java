package main;

import model.Schedule;
import model.Task;
import model.SpecialDay;
import util.FileManager;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.List;

public class CalendarView {
    private static YearMonth currentMonth = YearMonth.now();
    private static final Schedule schedule = new Schedule();
    private static final String SCHEDULE_FILE = "data/schedule.txt";
    private static final String REGULAR_FILE = "data/regular_tasks.txt";
    private static final String SPECIAL_FILE = "data/special_days.txt";

    public static void launch() {
        FileManager.load(schedule, SCHEDULE_FILE);
        JFrame frame = new JFrame("📅 Smart Schedule Planner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton prev = new JButton("<<");
        JButton next = new JButton(">>");
        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        topPanel.add(prev, BorderLayout.WEST);
        topPanel.add(monthLabel, BorderLayout.CENTER);
        topPanel.add(next, BorderLayout.EAST);

        JPanel calendarPanel = new JPanel(new GridLayout(0, 7));
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(infoPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addTask = new JButton("Add Task");
        JButton addRegular = new JButton("Add Regular Task");
        JButton addSpecial = new JButton("Add Special Day");
        JButton removeTask = new JButton("Remove Task");
        JButton viewAll = new JButton("View All Tasks");
        JButton saveExit = new JButton("Save & Exit");

        buttonPanel.add(addTask);
        buttonPanel.add(addRegular);
        buttonPanel.add(addSpecial);
        buttonPanel.add(removeTask);
        buttonPanel.add(viewAll);
        buttonPanel.add(saveExit);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(calendarPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        updateCalendar(calendarPanel, monthLabel, infoPanel);

        prev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar(calendarPanel, monthLabel, infoPanel);
        });

        next.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar(calendarPanel, monthLabel, infoPanel);
        });

        addTask.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Task Title:");
            String start = JOptionPane.showInputDialog("Start Time (YYYY-MM-DDTHH:MM):");
            String end = JOptionPane.showInputDialog("End Time (YYYY-MM-DDTHH:MM):");

            try {
                Task task = new Task(title, LocalDateTime.parse(start), LocalDateTime.parse(end), false);
                schedule.addTask(task);
                JOptionPane.showMessageDialog(null, "✅ Task added.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Invalid input.");
            }
        });

        addRegular.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Regular Task Title:");
            String start = JOptionPane.showInputDialog("Start Time (YYYY-MM-DDTHH:MM):");
            String end = JOptionPane.showInputDialog("End Time (YYYY-MM-DDTHH:MM):");

            try {
                Task task = new Task(title, LocalDateTime.parse(start), LocalDateTime.parse(end), true);
                FileManager.saveRegularTask(task, REGULAR_FILE);
                JOptionPane.showMessageDialog(null, "✅ Regular task saved.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Invalid input.");
            }
        });

        addSpecial.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Special Day Title:");
            String date = JOptionPane.showInputDialog("Date (YYYY-MM-DD):");

            try {
                SpecialDay day = new SpecialDay(title, LocalDate.parse(date));
                FileManager.saveSpecialDay(day, SPECIAL_FILE);
                JOptionPane.showMessageDialog(null, "✅ Special day saved.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Invalid input.");
            }
        });

        removeTask.addActionListener(e -> {
            String dateStr = JOptionPane.showInputDialog("Date of Task to Remove (YYYY-MM-DD):");
            LocalDate date = LocalDate.parse(dateStr);

            List<Task> tasks = schedule.getTasks().stream()
                    .filter(t -> t.getStart().toLocalDate().equals(date))
                    .toList();

            if (tasks.isEmpty()) {
                JOptionPane.showMessageDialog(null, "📭 No tasks found on that date.");
                return;
            }

            String[] options = tasks.stream().map(Task::getTitle).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(null, "Select task to remove:",
                    "Remove Task", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (selected != null) {
                schedule.removeTask(selected);
                JOptionPane.showMessageDialog(null, "✅ Task removed.");
            }
        });

        viewAll.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("📋 All Tasks:\n");
            for (Task t : schedule.getTasks()) {
                sb.append("- ").append(t.getTitle()).append(" (")
                        .append(t.getStart()).append(" to ").append(t.getEnd()).append(")\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        });

        saveExit.addActionListener(e -> {
            FileManager.save(schedule, SCHEDULE_FILE);
            frame.dispose();
        });

        frame.setVisible(true);
    }

    private static void updateCalendar(JPanel calendarPanel, JLabel monthLabel, JPanel infoPanel) {
        calendarPanel.removeAll();
        infoPanel.removeAll();
        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

        for (DayOfWeek day : DayOfWeek.values()) {
            calendarPanel.add(new JLabel(day.toString().substring(0, 3), SwingConstants.CENTER));
        }

        LocalDate first = currentMonth.atDay(1);
        int skip = first.getDayOfWeek().getValue() % 7;
        for (int i = 0; i < skip; i++) calendarPanel.add(new JLabel(""));

        int daysInMonth = currentMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            final LocalDate date = currentMonth.atDay(day);
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.addActionListener(e -> showDayDetails(date, infoPanel));
            calendarPanel.add(dayBtn);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private static void showDayDetails(LocalDate date, JPanel infoPanel) {
        infoPanel.removeAll();
        infoPanel.add(new JLabel("📅 Selected Date: " + date));

        List<SpecialDay> specials = FileManager.loadSpecialDays(SPECIAL_FILE);
        boolean hasSpecial = false;
        for (SpecialDay sd : specials) {
            if (sd.getDate().equals(date)) {
                infoPanel.add(new JLabel("🎉 " + sd.getTitle()));
                hasSpecial = true;
            }
        }
        if (!hasSpecial) infoPanel.add(new JLabel("🎉 No special days"));

        boolean hasTasks = false;
        for (Task t : schedule.getTasks()) {
            if (t.getStart().toLocalDate().equals(date)) {
                infoPanel.add(new JLabel("📋 " + t.getTitle() + " (" + t.getStart().toLocalTime() + "–" + t.getEnd().toLocalTime() + ")"));
                hasTasks = true;
            }
        }
        if (!hasTasks) infoPanel.add(new JLabel("📋 No tasks"));

        // 🔁 Regular Tasks
        List<Task> regulars = FileManager.loadRegularTasks(REGULAR_FILE);
        boolean hasRegular = false;
        for (Task rt : regulars) {
            if (rt.getStart().toLocalTime() != null) {
                infoPanel.add(new JLabel("🔁 " + rt.getTitle() + " (" + rt.getStart().toLocalTime() + "–" + rt.getEnd().toLocalTime() + ")"));
                hasRegular = true;
            }
        }
        if (!hasRegular) infoPanel.add(new JLabel("🔁 No regular tasks"));
        infoPanel.revalidate(); // ✅ refresh layout
        infoPanel.repaint();

    }
}