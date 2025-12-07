
# Smart Schedule Planner

A Java-based application designed to help users plan, organize, and visualize their schedules efficiently.  
It provides a simple GUI interface, integrates calendar data, and supports task management with special day handling.


## Features
- Calendar integration: view and manage schedules by day, week, or month
- Task management: add, edit, and delete regular tasks
- Special days support: handle holidays, events, or exceptions with custom rules
- GUI interface: built with Java Swing for an interactive planner experience
- Data persistence: reads and writes schedules from text files (`data/` folder)
- Modular design: organized into `controller`, `model`, `util`, and `main` packages for clarity

## Project Structure
SmartSchedulePlanner/
├── data/                         # Text files for calendar and tasks
│   ├── calendar_2025.txt         # Predefined calendar data
│   ├── regular_tasks.txt         # List of recurring tasks
│   ├── schedule.txt              # Saved schedule output
│   └── special_days.txt          # Holidays and exceptions
│
├── src/
│   ├── controller/               # Controls application logic
│   │   └── ScheduleController.java
│   │
│   ├── main/                     # Entry point and GUI classes
│   │   ├── App.java
│   │   ├── CalendarView.java
│   │   ├── Launcher.java
│   │   └── PlannerGUI.java
│   │
│   ├── model/                    # Core data models
│   │   ├── Schedule.java
│   │   ├── SpecialDay.java
│   │   └── Task.java
│   │
│   └── util/                     # Utility/helper classes
│       ├── CalendarTextHelper.java
│       └── FileManager.java
│
├── .idea/                        # IntelliJ project settings
├── .gitignore                    # Git ignore rules
├── SmartSchedulePlanner.iml       # IntelliJ module file
└── README.md                     # Project documentation

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/srichaitrareddymamidala-ux/SmartSchedulePlanner.git


## Technologies Used- Java (JDK 17+ recommended)
- Swing (for GUI)
- Git and GitHub (for version control)

  
## Future Enhancements- Export schedules to PDF or Excel
- Add notifications and reminders
- Cloud sync for multi-device access

  
## Author
Developed by Sri Chaitra Reddy Mamidala
GitHub: srichaitrareddymamidala-ux
