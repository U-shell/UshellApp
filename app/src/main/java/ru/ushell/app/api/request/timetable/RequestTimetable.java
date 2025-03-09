package ru.ushell.app.api.request.timetable;

import static ru.ushell.app.SQLite.TableColumns.*;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

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

public class RequestTimetable {

    public interface TimeTableGroupCallback {
        void onTimeTableGroupReceived(boolean infoGroupData);
    }

    public static void getTimeTableGroup(Integer IdGroup, DatabaseHelper databaseHelper, TimeTableGroupCallback callback){

        RetrofitService retrofitService = new RetrofitService();

        API api = retrofitService.getRetrofit().create(API.class);
        Call<ResponseTimeTable> timeTableResponseCall = api.getTimeTableGroup(IdGroup);
        getTimeTable(
                timeTableResponseCall,
                databaseHelper,
                callback
        );
    }

    public static void getTimeTableTeacher(Integer IdGroup, DatabaseHelper databaseHelper, TimeTableGroupCallback callback){

        RetrofitService retrofitService = new RetrofitService();

        API api = retrofitService.getRetrofit().create(API.class);
        Call<ResponseTimeTable> timeTableResponseCall = api.getTimeTableTeacher(IdGroup);

        getTimeTable(
                timeTableResponseCall,
                databaseHelper,
                callback
        );
    }

    private static void getTimeTable(
            Call<ResponseTimeTable> timeTableResponseCall,
            DatabaseHelper databaseHelper,
            TimeTableGroupCallback callback
    ){
        timeTableResponseCall.enqueue(new Callback<ResponseTimeTable>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTimeTable> call, @NonNull Response<ResponseTimeTable> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ResponseTimeTable timeTableResponse = response.body();
                    assert timeTableResponse != null;

                    databaseHelper.deleteAllData();

                    RequestProcessing(timeTableResponse.getMain_schedule(), "mainSchedule", databaseHelper);
                    RequestProcessing(timeTableResponse.getSecondary_schedule(), "secondarySchedule", databaseHelper);

                    callback.onTimeTableGroupReceived(true); // Вызов коллбека для передачи данных

                }else{
                    callback.onTimeTableGroupReceived(false); // Обработка ошибки
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseTimeTable> call, @NonNull Throwable t) {
                callback.onTimeTableGroupReceived(false); // Обработка ошибки
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
                    try {
                        int numLesson = Integer.parseInt(lessonEntry.getKey());

                        JSONObject specifications = convertLogDataToJson(lessonEntry.getValue().toString());
                        if (specifications == null) {
                            System.out.println("Failed to convert log data to JSON.");
                            continue; // Переход к следующему уроку
                        }

                        // Пересмотреть вывод рассписания если нет часть данных
                        Time timeStart = convertTime(specifications.getString(COLUMN_TIME_START));
                        Time timeEnd = convertTime(specifications.getString(COLUMN_TIME_END));
                        String lessonType = specifications.getString(COLUMN_TYPE_LESSON);
                        String subject = specifications.getString(COLUMN_SUBJECT);
                        int subgroup = specifications.getInt(COLUMN_SUBGROUP);
                        String withWhom = specifications.getString(COLUMN_WITH_WHOM);
                        int id_group = specifications.has(COLUMN_ID_GROUP) ? specifications.getInt(COLUMN_ID_GROUP) : -1;
                        String classroom = specifications.getString(COLUMN_CLASSROOM);

                        withWhom = withWhom.equals("null") ? " " : withWhom;

                        // Добавление данных в базу данных
                        if (NameSchedule.equals("mainSchedule")) {
                            databaseHelper.addMainSchedule(
                                    week, dayWeek, numLesson,
                                    timeStart, timeEnd,
                                    lessonType, subject, subgroup,
                                    withWhom, id_group, classroom
                            );
                        } else {
                            databaseHelper.addSecondarySchedule(
                                    dateLesson, dayWeek, numLesson,
                                    timeStart, timeEnd,
                                    lessonType, subject, subgroup,
                                    withWhom, id_group, classroom
                            );
                        }
                    } catch (JSONException e) {
                        System.err.println("Error parsing JSON: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing numLesson: " + e.getMessage());
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

        Time timeLess;

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

        Date dateLesson;
        try {
            dateLesson = new Date(Objects.requireNonNull(format.parse(DateLesson)).getTime());
            return dateLesson;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
