package ru.ushell.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.ushell.app.R;

//package ru.ushell.app.ui;
//
//
//
public class TableListActivity extends AppCompatActivity {
//    private TableLayout tableLayout;
//    private ArrayList<String> fixedData = new ArrayList<>(); // заголовки таблицы
//
//    private static String Lessons;
//    private static String typeLesson;
//
//
//
//
//    public static void TableLesson(Lesson lesson){
//        Lessons = lesson.getLesson();
////        time  =  lesson.getTime();
//        typeLesson  = lesson.getType_lesson();
//    }
//
//
//
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table_list);



    }
}
//
//
//
////    private void DataTable(ArrayList<Table> tableData, ArrayList<Lesson> dailyLesson){
////        // Add fixed data to first row
////        TableRow headerRow = new TableRow(this);
////        HeadingTable(headerRow,dailyLesson);
////        StudentList(tableData,dailyLesson);
////    }
////
////    private void HeadingTable(TableRow headerRow, ArrayList<Lesson> dailyLesson ){
////        fixedData.add("ФИО");
////        for(Lesson less : dailyLesson){
////            String lesson = less.getLesson();
////            String[] l = lesson.split(" ");
////
////            if(l.length > 1){
////                lesson = lesson.replace(" ", "\n");
////                System.out.println(lesson);
////            }
////            fixedData.add(lesson);
////
////        }
////
////        for (String data : fixedData) {
////
////            TextView textView = new TextView(this);
////            textView.setText(data);
////            textView.setTypeface(null, Typeface.BOLD_ITALIC);
////            textView.setPadding(10, 10, 10, 10);// положение на сцене
////            int color = Color.parseColor("#FF0000"); // set the color to red
////            textView.setTextColor(color);
////            textView.setTextSize(10);
////
////            headerRow.addView(textView);
////
////
////        }
////        tableLayout.addView(headerRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
////                ViewGroup.LayoutParams.WRAP_CONTENT));
////
////    }
////
////    private void StudentList(ArrayList<Table> tableData, ArrayList<Lesson> dailyLesson ) {
////        int NumberLesson = Lesson.LessonsList.get(0).getNumberLesson();
////        Set<String> NameSet = new HashSet<>();
////
////        for (int i = 0; i < Table.TableStudentList.size(); i++) {
////            NameSet.add(Table.TableStudentList.get(i).getNameStudent());
////        }
////
////        if (tableData.size() == 0) {
////            AddTimeLessonNew(NameSet, tableData);
////        }
////        for (Table Name : tableData) {
////
////            String NameStudent = Name.getNameStudent();
////            TableRow tableRow = new TableRow(this);
////
////            TextView textV = new TextView(this);
////
////            textV.setPadding(10, 10, 100, 10);
////            textV.setTypeface(null, Typeface.BOLD_ITALIC);
////            int colorV = Color.parseColor("#000000");
////            textV.setTextColor(colorV);
////            textV.setText(NameStudent);
////            textV.setTextSize(20);
////
////            tableRow.addView(textV);
////
////            for (int i = 0; i < dailyLesson.size(); i++) {
////
////
////                TextView textView = new TextView(this);
////                String timelesson = dailyLesson.get(i).getTime();
////                String[] TimeLesson = timelesson.split("-");
////
////                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
////                try {
////                    if (NumberLesson > Name.getOutStudent().size()) {
//////                        Name.getOutStudent().add(TimeLesson[0]);
////                        Name.getOutStudent().put(TimeLesson[0], true);
////                    }
////
////                    Date timeIn = parser.parse(TimeLesson[0]);
////                    Date timeOn = parser.parse(TimeLesson[1]);
////
////                    // переписапть модуль по нахождению времени
////                    // сделать цыкл проверки всего времени сутдента
////                    for (String time : Name.getOutStudent().keySet()) {
////
////                        boolean Status = Boolean.TRUE.equals(Name.getOutStudent().get(time));
////                        Date userDate = parser.parse(time);
////
////                        if ((userDate.after(timeIn) && userDate.before(timeOn)) && Status) {
////                            textView.setBackgroundColor(Color.GREEN);
////                            textView.setText("П");
////                            break;
////                        } else {
////                            textView.setBackgroundColor(Color.RED);
////                            textView.setText("H");
////                        }
////                    }
////                } catch(ParseException ignored){}
////
////                int color = Color.parseColor("#000000");
////                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
////                textView.setTextColor(color);
////                textView.setPadding(10, 10, 10, 10);
////                textView.setTextSize(20);
////
////                final int index = i;
////
////                textView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if (v.getTag() == null || !(boolean) v.getTag()) {
////                            v.setBackgroundColor(Color.GREEN);
////                            v.setTag(true);
////                            boolean StatusStudent = true;
////                            textView.setText("П");
////
////                            String[] t = TimeLesson[0].split(":");
////                            int num = Integer.parseInt(t[1]) + 1;
////                            t[1] = String.valueOf(num);
////                            if (t[1].length() == 1) {
////                                t[1] = "0" + t[1];
////                            }
////                            String timele = t[0] + ":" + t[1];
//////                                Name.getOutStudent().set(index, timele);
////                            Name.getOutStudent().put(timele, StatusStudent);
////                            TableListWriteDb.WriteOneStudent(Name.getNameStudent(), timele, Name.getData(), StatusStudent);
////
////                        } else {
////                            v.setBackgroundColor(Color.RED);
////                            v.setTag(false);
////                            boolean StatusStudent = false;
////                            textView.setText("H");
////
////                            String[] t = TimeLesson[0].split(":");
////                            int num = Integer.parseInt(t[1]) + 1;
////                            t[1] = String.valueOf(num);
//                            if (t[1].length() == 1) {
//                                t[1] = "0" + t[1];
//                            }
//                            String timele = t[0] + ":" + t[1];
//
////                                Name.getOutStudent().set(index, TimeLesson[0]);
//                            Name.getOutStudent().put(timele, StatusStudent);
//                            TableListWriteDb.WriteOneStudent(Name.getNameStudent(), timele, Name.getData(), StatusStudent);
//                        }
//                    }
//                });
//                tableRow.addView(textView);
//                }
//            tableLayout.addView(tableRow);
//            }
////        System.out.println(tableData.size());
////        for (Table Name : tableData) {
////
////
////            String rowData = Name.getNameStudent();
////            TableRow tableRow = new TableRow(this);
////
////            for (int i = 0; i < Name.getOutStudent().size(); i++) {
////
////                TextView textView = new TextView(this);
////
////
////                if (i == 0){
////                    textView.setText(rowData);
////                }
////
//////                textView.setText(Name.getOutStudent().get(i));
////                textView.setPadding(10, 10, 100, 10);
////                int color = Color.parseColor("#000000"); // set the color to red
////                textView.setTextColor(color);
////
////                textView.setTextSize(20);
////                // Add click listener to change color when clicked
////                int index = i;
////
////                if((Objects.equals(Name.getOutStudent().get(i), "Yes"))){
////                    textView.setBackgroundColor(Color.GREEN);
////                    textView.setText("П");
////                }else if((Objects.equals(Name.getOutStudent().get(i), "NO"))) {
////                    textView.setBackgroundColor(Color.RED);
////                    textView.setText("H");
////                }
////
////                textView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                        if (index > 0 ) {
////                            if (v.getTag() == null || !(boolean) v.getTag()) {
////                                v.setBackgroundColor(Color.GREEN);
////                                v.setTag(true);
////                                textView.setText("П");
////
//////                                tableData[index][tableData[index].length - 1] = "Yes";
//////                                Name.getOutStudent()[index] = "Yes";
////
////
////                            } else {
////                                v.setBackgroundColor(Color.RED);
////                                v.setTag(false);
////                                textView.setText("H");
////
//////                                tableData[index][tableData[index].length - 1] = "No";
//////                                Name.getOutStudent()[index] = "No";
////
////                            }
////                        }
////                    }
////                });
////                tableRow.addView(textView);
////            }
////            tableLayout.addView(tableRow);
////        }
//        }
//
//    private static void AddTimeLessonNew(Set<String> NameSet, ArrayList<Table> tableData){
//
//            for( String NewName: NameSet) {
//                LinkedTreeMap<String, Boolean> list = new LinkedTreeMap<>();
//                list.put("00:00", true);
//                Table.TableStudentList.add(0, new Table(NewName, CalendarUtils.formattedDate(CalendarUtils.selectedDate), list));
//            }
//            for (Table Name : tableData) {
//
//                int len = Name.getOutStudent().size();
//                System.out.println(len+"len");
//                for(String time : Name.getOutStudent().keySet()) {
//                    String[] timelesson = time.split("-");
//
//                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
//                    try {
//                        Date timeIn = parser.parse(timelesson[0]);
//                        Date timeOn = parser.parse(timelesson[1]);
//                        Date userTime = parser.parse(String.valueOf(Name.getOutStudent().get(time)));
//
//                        String[] t = timelesson[0].split(":");
//                        int num = Integer.parseInt(t[1]) + 1;
//                        t[1] = String.valueOf(num);
//                        if (t[1].length() == 1) {
//                            t[1] = "0" + t[1];
//                        }
//                        String timele = t[0] + ":" + t[1];
//
//                        if (userTime.after(timeIn) && userTime.before(timeOn)) {
//                            break;
//                        }
//                        else if (!Name.getOutStudent().containsKey(timele) && !Name.getOutStudent().containsKey(timelesson[0])) {
////                        Name.getOutStudent().set(j, timelesson[0]);
//                            Name.getOutStudent().put(timelesson[0],true);
//                        }
//                    } catch (ParseException ignored) {}
//                }
//            }
//        }
//
//
//    public void Profile(View v) {
//        Intent intent = new Intent(this, ProfileActivity.class);
//        startActivity(intent);
//    }
//    public void Timetable(View v) {
//        Intent intent = new Intent(this, TimetableActivity.class);
//        startActivity(intent);
//    }
//}
