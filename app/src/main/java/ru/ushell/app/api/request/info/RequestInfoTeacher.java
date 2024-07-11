package ru.ushell.app.api.request.info;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.BodyRequestTokenIdentifier;
import ru.ushell.app.api.response.ResponseInfoTeacher;
import ru.ushell.app.models.User;

public class RequestInfoTeacher {
    private static String infoTeacher;
    public static String getInfoTeacher() {

        RetrofitService retrofitService = new RetrofitService();
        BodyRequestTokenIdentifier tokenIdentifier = new BodyRequestTokenIdentifier();

        String AccessToken = User.getAccessToken();
        String TypeToken = User.getTypeToken();

        API api = retrofitService.getRetrofit().create(API.class);
        Call<ResponseInfoTeacher> infoTeacherResponseCall = api.getInfoTeacher(tokenIdentifier.setToken(TypeToken,AccessToken));
        infoTeacherResponseCall.enqueue(new Callback<ResponseInfoTeacher>() {
            @Override
            public void onResponse(Call<ResponseInfoTeacher> call, Response<ResponseInfoTeacher> response) {

            }

            @Override
            public void onFailure(Call<ResponseInfoTeacher> call, Throwable t) {

            }
        });
        return null;
    }
}
