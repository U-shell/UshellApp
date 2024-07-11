package ru.ushell.app.ui;

import static ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceReadDb.AttendanceGroupDayRead;
import static ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceReadDb.AttendanceStudentRead;
import static ru.ushell.app.ui.ModelTimeTable.lesson.Lesson.LessonsList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import ru.ushell.app.R;
import ru.ushell.app.SQLite.DatabaseHelper;
import ru.ushell.app.api.request.attendance.RequestAttendance;
import ru.ushell.app.api.request.timetable.RequestTimeTableGroup;
import ru.ushell.app.api.response.attendance.GroupTreeMap;
import ru.ushell.app.ui.ModelTimeTable.calendar.CalendarAdapter;
import ru.ushell.app.ui.ModelTimeTable.calendar.CalendarUtils;
import ru.ushell.app.models.ItemSpacingDecoration;
import ru.ushell.app.models.User;
import ru.ushell.app.ui.ModelTimeTable.Message;
import ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceGroup;
import ru.ushell.app.ui.ModelTimeTable.attendance.AttendanceStudent;
import ru.ushell.app.ui.ModelTimeTable.lesson.Lesson;
import ru.ushell.app.ui.ModelTimeTable.lesson.LessonAdapter;
import ru.ushell.app.ui.ModelTimeTable.lesson.LessonReadDb;
import ru.ushell.app.ui.Sudentset.StudentDataRiteDb;
import ru.ushell.app.ui.TableListStudent.Table.Table;
import ru.ushell.app.ui.TableListStudent.Table.TableListReadDb;
import ru.ushell.app.ui.TableListStudent.table_cell;

/**
 * Сцена с рассписанием предметов и возможность проставлять поссешаемость студентаов
 */
public class TimeTableActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, LessonAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    Context context;

    RecyclerView listLessonGroup;
    RecyclerView lessonRecycler;

    DatabaseHelper databaseHelperMain;

    LessonAdapter lessonAdapter;
    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        CalendarUtils.selectedDate = LocalDate.now();

        initWidgets();
        setWeekView();

        if (databaseHelperMain == null) {
            databaseHelperMain = new DatabaseHelper(this);
        }

        Cursor cursor = databaseHelperMain.readAllDataMain();
//        databaseHelperMain.deleteAllData();
        if(cursor.getCount() == 0) {
            RequestTimeTableGroup.getTimeTableGroup(User.getInstance(this).getIDGroup(), databaseHelperMain, infoGroupData -> {
                if (infoGroupData != null) {
                    System.out.println("ok");

                } else {
                    System.out.println("error");
                }
            });
        }


        if (LessonsList.isEmpty()){
//            ListLessonGroupFirst();
            LessonReadDb.ListLesson(databaseHelperMain);
            try {
                TimeUnit.SECONDS.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (Table.TableStudentList.isEmpty()) {
            try {
                TableListReadDb.NamesStudentsGroup(CalendarUtils.formattedDateDB(CalendarUtils.selectedDate));
                StudentDataRiteDb.VisitingStudent();
            }catch (JsonSyntaxException j){
                System.out.println(j.getMessage());
            }
        }

        RequestAttendance.getStudentAttendance(this,infoAttendanceStudent -> {
            System.out.println(infoAttendanceStudent);
            System.out.println("посещаесомть студента ");
            try {
                AttendanceStudentRead(this);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });


//        try {
            System.out.println("sas2487889");
//            AttendanceGroupDayRead(this);
//        } catch (JSONException e) {
//            System.out.println(05405);
//            throw new RuntimeException(e);
//        }
//        try {
//            System.out.println("sdsd121asa2");
//            AttendanceGroupDayRead(this);
//        } catch (JSONException e) {
//            System.out.println("sdsd1212");
//            throw new RuntimeException(e);
//        }
    }

    @RequiresApi(31)
    private void makeBlurImage(){
        RenderEffect blurEffect = RenderEffect.createBlurEffect(15f, 15f, Shader.TileMode.MIRROR);
        findViewById(R.id.image_back).setRenderEffect(blurEffect);
    }

    /**
     * Настройка верхней панели:
     * <pre>
     *Название Месяц
     *Кнопки Перехода
     *Даты Неделе
     * </pre>
     */
    private void initWidgets(){
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);// список чисел недели
        monthYearText = findViewById(R.id.monthYearTV);// название месяаца
        listLessonGroup = findViewById(R.id.ListItemLesson);// список назначений ивента
    }

    /**
     * Вывовд данных для верхней панели
     *<pre>
     *     НАзвание месяца
     *     Дни недели
     *</pre>
     */
    private void setWeekView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);
        TableListReadDb.NamesStudentsGroup(CalendarUtils.formattedDateDB(CalendarUtils.selectedDate));


        int spacing = 0; // Замените на нужное вам расстояние между элементами

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 6); // количество выводимых дней

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarRecyclerView.addItemDecoration(new ItemSpacingDecoration(spacing));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            makeBlurImage();
        }
        
        setEventAdapter();


    }

    /**
     * Переход на предыдуший неделю  <-
     */

    public void previousWeekAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    /**
     * Переход на следущаю неделю ->
     *
     */
    public void nextWeekAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    /**
     * При нажатии на число дня недели
     * @param position позитиция списка
     * @param date дата выбранного дня
     */
    @Override
    public void onItemClick(int position, LocalDate date){
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    /**
     * При нажатии на число дня недели обновляються/подгружаються данные
     */
    @Override
    protected void onResume(){
        super.onResume();
        setEventAdapter();
    }

    /**
     * Получение списов :
     * <pre>
     *     Список пар в выбранный день
     *     Список отметки о присутствии в конкретный день
     * </pre>
     */
    public void setEventAdapter(){

        ArrayList<Lesson> dailyLesson = Lesson.LessonsForDataWeek(CalendarUtils.ParityWeek(CalendarUtils.selectedDate),
                CalendarUtils.DayOfWeek(CalendarUtils.selectedDate),
                CalendarUtils.formattedDateToDbWeek(CalendarUtils.selectedDate));

        ArrayList<AttendanceStudent> dailyAttendanceStudents = AttendanceStudent.AttendanceForData(CalendarUtils.formattedDateDayAttendance(CalendarUtils.selectedDate));
        setLessonRecycler(dailyLesson, dailyAttendanceStudents);
    }

    /**
     * Обработка полученных данных
     * @param dailyLesson список уроков на день пользователя
     * @param dailyAttendanceStudents список присутствие на день пользователя

     * <p>
     * {@link LessonAdapter} функция заполнения данных из списоков в ячейку
     *
     */
    private void setLessonRecycler(ArrayList<Lesson> dailyLesson, ArrayList<AttendanceStudent> dailyAttendanceStudents) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        lessonRecycler = findViewById(R.id.ListItemLesson);
        lessonRecycler.setLayoutManager(layoutManager);

        // проверка на наличии пар на день
        if(!dailyLesson.isEmpty()){
            lessonAdapter = new LessonAdapter(this, dailyLesson, dailyAttendanceStudents, this);
            lessonRecycler.setAdapter(lessonAdapter);
        }else {
            message = new Message(this,"В этот день занятий нет");
            lessonRecycler.setAdapter(message);
        }

    }

    /**
     * При нажатии на выбранную пару открываеться окно со студентами
     * @param position позиция пары в списке
     * @param lesson получение название предмета
     */
    @Override
    public void onItemClick(int position, Lesson lesson){
        RequestAttendance.getGroupAttendance(this, CalendarUtils.formattedDateDayAttendance(CalendarUtils.selectedDate), new RequestAttendance.AttendanceGroupDayCallback() {
            @Override
            public void onAttendanceGroupReceived(TreeMap<String, GroupTreeMap> infoAttendanceGroup) {
                try {
                    AttendanceGroupDayRead(TimeTableActivity.this);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyAlertTheme);

        Context dialogContext = dialog.getContext();

        LayoutInflater inflater = LayoutInflater.from(this);

        View tableStudent = inflater.inflate(R.layout.table_cell,null);

        // Заголовок окна
        // --------------
        TextView title = tableStudent.findViewById(R.id.title);
        title.setText(lesson.getSubject());

        TextView timeLesson = tableStudent.findViewById(R.id.time_start_end_lesson);
        timeLesson.setText(lesson.getTimeLesson());

        TextView typeLesson = tableStudent.findViewById(R.id.selection_type);
        typeLesson.setText(lesson.getTypeLesson());

        // --------------
        dialog.setView(tableStudent);
        // Тело окна
        // --------------
        TableLayout tableLayout = (TableLayout) tableStudent.findViewById(R.id.table_layout);

        ArrayList<AttendanceGroup> attendanceGroupList = AttendanceGroup.AttendanceGroupForData(lesson.getNumLesson(), lesson.getSubgroup());

        table_cell.StudentList(lesson.getNumLesson(), attendanceGroupList, tableLayout, dialogContext);

        // --------------
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * Переход на другие сцены
     */
    public void Profile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);


    }

    public void TableList(View v){
        Intent intent = new Intent(this, TableListActivity.class);
        startActivity(intent);
    }

}