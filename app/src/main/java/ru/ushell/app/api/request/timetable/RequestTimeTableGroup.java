package ru.ushell.app.api.request.timetable;

import static ru.ushell.app.SQLite.TableColumns.COLUMN_CLASSROOM;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBGROUP;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_SUBJECT;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TEACHER;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_END;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TIME_START;
import static ru.ushell.app.SQLite.TableColumns.COLUMN_TYPE_LESSON;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ushell.app.SQLite.DatabaseHelper;
import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.response.ResponseTimeTable;

public class RequestTimeTableGroup extends AppCompatActivity {

    public interface TimeTableGroupCallback {
        void onTimeTableGroupReceived(JSONObject infoGroupData);
    }

    public static void getTimeTableGroup(Integer IdGroup, DatabaseHelper databaseHelper, TimeTableGroupCallback callback){

        RetrofitService retrofitService = new RetrofitService();

        API api = retrofitService.getRetrofit().create(API.class);
        Call<ResponseTimeTable> timeTableResponseCall = api.getTimeTable(IdGroup);

        timeTableResponseCall.enqueue(new Callback<ResponseTimeTable>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTimeTable> call, @NonNull Response<ResponseTimeTable> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ResponseTimeTable timeTableResponse = response.body();

                    assert timeTableResponse != null;

                    RequestProcessing(timeTableResponse.getMain_schedule(), "mainSchedule",databaseHelper);

                    RequestProcessing(timeTableResponse.getSecondary_schedule(), "secondarySchedule",databaseHelper);

                    callback.onTimeTableGroupReceived(null); // Вызов коллбека для передачи данных

                }else{
                    callback.onTimeTableGroupReceived(null); // Обработка ошибки
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseTimeTable> call, @NonNull Throwable t) {
                callback.onTimeTableGroupReceived(null); // Обработка ошибки
            }
        });
    }

    private static void RequestProcessing(Map<String, Object> Schedule, String NameSchedule , DatabaseHelper databaseHelper){
        int week = -1;
        Date dateLesson = null;
        for (Map.Entry<String, Object> entry : Schedule.entrySet()) {

            if(NameSchedule.equals("mainSchedule")){
                week = Integer.parseInt(entry.getKey());
            }else{
                dateLesson = convertDate(entry.getKey());
            }

            Map<String, Object> daySchedule = (Map<String, Object>) entry.getValue();

            for (Map.Entry<String, Object> dayEntry : daySchedule.entrySet()) {
                String dayWeek = dayEntry.getKey();
                Map<String, Object> lessonSchedule = (Map<String, Object>) dayEntry.getValue();

                for (Map.Entry<String, Object> lessonEntry : lessonSchedule.entrySet()) {
                    Integer numLesson  = Integer.parseInt(lessonEntry.getKey());
                    JSONObject specifications = convertLogDataToJson(lessonEntry.getValue().toString());

                    if (specifications != null) {
                        String timeStartStr = null;
                        String timeEndStr = null;
                        try {
                            timeStartStr = specifications.getString(COLUMN_TIME_START);
                            timeEndStr = specifications.getString(COLUMN_TIME_END);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        Time timeStart = convertTime(timeStartStr);
                        Time timeEnd = convertTime(timeEndStr);

                        String lessonType, subject, teacher, classroom;
                        int subgroup;

                        try {
                            lessonType = specifications.getString(COLUMN_TYPE_LESSON);
                            subject = specifications.getString(COLUMN_SUBJECT);
                            subgroup = (int) Double.parseDouble(specifications.getString(COLUMN_SUBGROUP));
                            teacher = specifications.getString(COLUMN_TEACHER);
                            classroom = specifications.getString(COLUMN_CLASSROOM);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if(NameSchedule.equals("mainSchedule")) {
                            databaseHelper.addMainSchedule(week, dayWeek, numLesson,
                                    timeStart, timeEnd,
                                    lessonType, subject, subgroup, teacher, classroom);
                        }else{
                            databaseHelper.addSecondarySchedule(dateLesson, dayWeek, numLesson,
                                    timeStart, timeEnd,
                                    lessonType, subject, subgroup, teacher, classroom);

                        }
                    } else {
                        System.out.println("Failed to convert log data to JSON.");
                    }
                }
            }
        }
    }

    private static JSONObject convertLogDataToJson(String logData) {
        JSONObject jsonObject = new JSONObject();
        try {
            String[] parts = logData.substring(1, logData.length() - 1).split(", ");
            for (String part : parts) {

                String key = part.split("=")[0];
                String value = part.split("=")[1];

                jsonObject.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    @SuppressLint("SimpleDateFormat")
    private static Time convertTime(String TimeLesson){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Time timeLess = null;
        try {
            timeLess = new Time(Objects.requireNonNull(sdf.parse(TimeLesson)).getTime());
            return timeLess;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static Date convertDate(String DateLesson){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date dateLesson = null;
        try {
            dateLesson = new Date(Objects.requireNonNull(format.parse(DateLesson)).getTime());
            return dateLesson;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
