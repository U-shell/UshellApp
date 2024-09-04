package ru.ushell.app.api.request.info;


import androidx.annotation.NonNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.BodyRequestTokenIdentifier;
import ru.ushell.app.api.response.info.ResponseInfoTeacher;

public class RequestInfoTeacher {
    private static String academicStatus;
    private static String academicDegree;

    public interface InfoTeachCallback {
        void onInfoTeachReceived(List<Object> infoGroupData);
    }

    public static void getInfoTeacher(String AccessToken, String TypeToken, InfoTeachCallback callback) {

        RetrofitService retrofitService = new RetrofitService();
        BodyRequestTokenIdentifier tokenIdentifier = new BodyRequestTokenIdentifier();

        API api = retrofitService.getRetrofit().create(API.class);

        Call<ResponseInfoTeacher> infoTeacherResponseCall = api.getInfoTeacher(tokenIdentifier.setToken(TypeToken,AccessToken));

        infoTeacherResponseCall.enqueue(new Callback<ResponseInfoTeacher>() {
            @Override
            public void onResponse(@NonNull Call<ResponseInfoTeacher> call, @NonNull Response<ResponseInfoTeacher> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ResponseInfoTeacher infoTeacherResponse = response.body();

                    assert infoTeacherResponse != null;
                    List<Object> infoTeacherData = new ArrayList<>();

                    academicStatus = infoTeacherResponse.getAcademicStatus();
                    academicDegree = infoTeacherResponse.getAcademicDegree();

                    infoTeacherData.add(academicStatus);
                    infoTeacherData.add(academicDegree);

                    callback.onInfoTeachReceived(infoTeacherData);
                }else {
                    callback.onInfoTeachReceived(null); // Обработка ошибки

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseInfoTeacher> call, @NonNull Throwable t) {
                /*TODO*/
                // написать модуль обработки ошибок
                List<Object> errorInfo = new ArrayList<>();
                errorInfo.add(-1);
                errorInfo.add("404 Not Found");
                callback.onInfoTeachReceived(errorInfo); // Обработка ошибки
            }
        });
    }
}
