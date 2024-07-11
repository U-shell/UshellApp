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
import ru.ushell.app.api.response.ResponseInfoGroup;

public class RequestInfoGroup {
    private static String nameGroup;
    private static Integer idGroup;

    public interface InfoGroupCallback {
        void onInfoGroupReceived(List<Object> infoGroupData);
    }

    public static void getInfoGroup(String AccessToken, String TypeToken, InfoGroupCallback callback) {

        RetrofitService retrofitService = new RetrofitService();
        BodyRequestTokenIdentifier tokenIdentifier = new BodyRequestTokenIdentifier();

        API api = retrofitService.getRetrofit().create(API.class);
        Call<ResponseInfoGroup> infoGroupResponseCall = api.getInfoGroup(tokenIdentifier.setToken(TypeToken, AccessToken));
        // написать проеверку, есть ли записанные даные или нет 
        infoGroupResponseCall.enqueue(new Callback<ResponseInfoGroup>() {
            @Override
            public void onResponse(@NonNull Call<ResponseInfoGroup> call, @NonNull Response<ResponseInfoGroup> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ResponseInfoGroup infoGroupResponse = response.body();

                    assert infoGroupResponse != null;
                    idGroup = infoGroupResponse.getId();
                    nameGroup = infoGroupResponse.getName();

                    List<Object> infoGroupData = new ArrayList<>();

                    infoGroupData.add(idGroup);
                    infoGroupData.add(nameGroup);

                    callback.onInfoGroupReceived(infoGroupData); // Вызов коллбека для передачи данных
                }
                else {
                    callback.onInfoGroupReceived(null); // Обработка ошибки
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseInfoGroup> call, @NonNull Throwable t) {
                List<Object> errorInfo = new ArrayList<>();
                errorInfo.add(-1);
                errorInfo.add("404 Not Found");
                callback.onInfoGroupReceived(errorInfo); // Обработка ошибки
            }
        });
    }
}
