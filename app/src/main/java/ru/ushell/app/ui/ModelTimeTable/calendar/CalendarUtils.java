package ru.ushell.app.ui.ModelTimeTable.calendar;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarUtils {
    public static LocalDate selectedDate;

    public static LocalDate DataNow() {
        return LocalDate.now();
    }

    public static String FormatDataCome(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        String formatterd = date.format(formatter);
        String[] reformat = formatterd.split(" ");
        String line = String.format("%s_%s_%s", reformat[0], reformat[1], reformat[2]);
        return line;
    }
    public static DateTimeFormatter getDataTimeNow(){
        return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    }

    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        return date.format(formatter);
    }

    public static String formattedDateDB(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return date.format(formatter);
    }

    public static String formattedDateToDbWeek(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public static String formattedDateDayAttendance(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String DayOfWeek(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", new Locale("RU", "ru"));
        return date.format(formatter);
    }

    public static String NumWeekOfYear(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("w");
        return date.format(formatter);
    }

    public static Long ParityWeek(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("w");
        if(Long.parseLong(String.valueOf(date.format(formatter))) % 2 == 0 ){
            return 0L;
        }else{
            return 1L;
        }
    }

    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL yyyy");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 2; i <= 42; i++)// вывод дней
        {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
        }
        return daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = mondayForDate(selectedDate);
        LocalDate CountWeek = current.plusWeeks(1);
        LocalDate endDate = current.plusDays(6);

        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate mondayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.MONDAY)//первый день недели
                return current;

            current = current.minusDays(1);
        }
        return null;
    }
}
