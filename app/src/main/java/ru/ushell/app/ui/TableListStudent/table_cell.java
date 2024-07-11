package ru.ushell.app.ui.TableListStudent;

import static ru.ushell.app.R.*;
import static ru.ushell.app.R.drawable.button_attendance_false;
import static ru.ushell.app.R.drawable.button_attendance_none;
import static ru.ushell.app.R.drawable.button_attendance_true;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Iterator;

import ru.ushell.app.R;
import ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceGroup;

public class table_cell {

    public static void StudentList(Integer NumberLesson, ArrayList<AttendanceGroup> attendanceGroups, TableLayout tableLayout, Context dialogContext) {
        tableLayout.removeAllViews();

        for (AttendanceGroup nameStudent : attendanceGroups) {
            System.out.println(nameStudent.getNameStudent());
            String OldNameStudent = nameStudent.getNameStudent();
            int index_sim = OldNameStudent.lastIndexOf(" ");
            String NameStudent = OldNameStudent.substring(0, index_sim) + '\n' + OldNameStudent.substring(index_sim + 1);

            TableRow tableRow = new TableRow(dialogContext);
            // Колонка с именами студентов
            TextView textV = new TextView(dialogContext);

            Typeface alataFonts = ResourcesCompat.getFont(dialogContext, R.font.montserrat_regular);
            textV.setTypeface(alataFonts);
            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            param.gravity = Gravity.CENTER;
            textV.setLayoutParams(param);
            textV.setTextColor(Color.parseColor("#000000"));
            textV.setText(NameStudent);
            textV.setTextSize(15);
            textV.setPadding(15 * 3, 15, 360, 15);
            tableRow.addView(textV);


            Button attendanceButton = new Button(dialogContext);

            Typeface Fonts = ResourcesCompat.getFont(dialogContext, font.montserrat_bold);

            TableRow.LayoutParams params = new TableRow.LayoutParams(68 * 3, 22 * 3);
            params.gravity = Gravity.CENTER;
            attendanceButton.setLayoutParams(params);

            attendanceButton.setTypeface(Fonts);
            attendanceButton.setTextSize(18);
            attendanceButton.setPadding(0, 0, 0, 0);

            JSONObject attendanceStudentGroup = nameStudent.getAttendance();
            Iterator<String> attendanceStudent = attendanceStudentGroup.keys();

            attendanceButton.setBackground(ContextCompat.getDrawable(dialogContext, button_attendance_none));
            attendanceButton.setText(" ");

            while (attendanceStudent.hasNext()) {

                String numLesson = attendanceStudent.next();
                Object attendance = null;
                try {
                    attendance = attendanceStudentGroup.get(numLesson);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if (Integer.parseInt(numLesson) == NumberLesson) {
                    if(attendance.equals(true)){
                        attendanceButton.setBackground(ContextCompat.getDrawable(dialogContext, button_attendance_true));
                        attendanceButton.setText("П");
                        attendanceButton.setTextColor(Color.WHITE);
                    }else{
                        attendanceButton.setBackground(ContextCompat.getDrawable(dialogContext, button_attendance_false));
                        attendanceButton.setText("H");
                        attendanceButton.setTextColor(Color.parseColor("#3B085F"));
                    }
                    break;
                }
            }

            attendanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer tag = (Integer) v.getTag();
                    if (tag == null || tag == 2) {
                        v.setTag(1);
                        attendanceButton.setBackground(ContextCompat.getDrawable(dialogContext, button_attendance_true));
                        attendanceButton.setText("П");
                        attendanceButton.setTextColor(Color.WHITE);
//                            boolean StatusStudent = true ;
//                            String TimeLine = ReversTime(TimeLesson[0].split(":"));
//                            Name.setOutStudent(WriteData(TimeLine, StatusStudent));
//                            TableListWriteDb.WriteOneStudent(Name.getNameStudent(), TimeLine, Name.getData(), StatusStudent);
                    } else if (tag == 1) {
                        v.setTag(0);
                        attendanceButton.setBackground(ContextCompat.getDrawable(dialogContext, button_attendance_false));
                        attendanceButton.setText("H");
                        attendanceButton.setTextColor(Color.parseColor("#3B085F"));
//                            boolean StatusStudent = false;
//                            String TimeLine = ReversTime(TimeLesson[0].split(":"));
//                            Name.setOutStudent(WriteData(TimeLine, StatusStudent));
//                            TableListWriteDb.WriteOneStudent(Name.getNameStudent(), TimeLine, Name.getData(), StatusStudent);
                    } else {
                        v.setTag(2);
                        attendanceButton.setBackground(ContextCompat.getDrawable(dialogContext, button_attendance_none));
                        attendanceButton.setText(" ");
                    }
                }
            });
            tableRow.addView(attendanceButton);
            tableLayout.addView(tableRow);
        }
    }
}