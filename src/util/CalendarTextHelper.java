package util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarTextHelper {
    public static void generateYearCalendar(int year, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int month = 1; month <= 12; month++) {
                LocalDate firstDay = LocalDate.of(year, month, 1);
                writer.write("\n📅 " + firstDay.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year + "\n");
                writer.write("Sun Mon Tue Wed Thu Fri Sat\n");

                int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7;
                for (int i = 0; i < dayOfWeek; i++) writer.write("    ");

                int length = firstDay.lengthOfMonth();
                for (int day = 1; day <= length; day++) {
                    writer.write(String.format("%3d ", day));
                    if ((day + dayOfWeek) % 7 == 0) writer.write("\n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not generate calendar.");
        }
    }
}