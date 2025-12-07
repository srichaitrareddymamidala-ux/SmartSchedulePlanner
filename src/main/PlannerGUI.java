package main;

import javax.swing.*;
import java.awt.*;
import model.Schedule;
import model.Task;
import java.time.LocalDateTime;

public class PlannerGUI {
    public static void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Smart Schedule Planner");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            JButton viewTasksBtn = new JButton("View Tasks");
            JButton addTaskBtn = new JButton("Add Task");
            JButton exitBtn = new JButton("Exit");

            JPanel panel = new JPanel(new GridLayout(3, 1));
            panel.add(viewTasksBtn);
            panel.add(addTaskBtn);
            panel.add(exitBtn);

            frame.add(panel);
            frame.setVisible(true);

            Schedule schedule = new Schedule();

            viewTasksBtn.addActionListener(e -> {
                StringBuilder sb = new StringBuilder();
                for (Task task : schedule.getTasks()) {
                    sb.append(task).append("\n");
                }
                JOptionPane.showMessageDialog(frame, sb.toString(), "Tasks", JOptionPane.INFORMATION_MESSAGE);
            });

            addTaskBtn.addActionListener(e -> {
                String title = JOptionPane.showInputDialog(frame, "Task Title:");
                String start = JOptionPane.showInputDialog(frame, "Start Time (YYYY-MM-DDTHH:MM):");
                String end = JOptionPane.showInputDialog(frame, "End Time (YYYY-MM-DDTHH:MM):");

                try {
                    Task task = new Task(title,
                            LocalDateTime.parse(start),
                            LocalDateTime.parse(end),
                            false);
                    schedule.addTask(task);
                    JOptionPane.showMessageDialog(frame, "✅ Task added.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "❌ Invalid input.");
                }
            });

            exitBtn.addActionListener(e -> frame.dispose());
        });
    }
}