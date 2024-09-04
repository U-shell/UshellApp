package ru.ushell.app.api.request;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.net.HttpURLConnection;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import ru.ushell.app.api.API;
import ru.ushell.app.api.Service;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.BodyRequestSingIn;
import ru.ushell.app.api.response.ResponseSingIn;
import ru.ushell.app.models.User;
import ru.ushell.app.utils.SavingSession.SaveUser;

public class Response {

    public static void loginUser(@Nullable String email,
                                 @Nullable String password,
                                 Context ClassContext,
                                 final OnLoginSuccess onSuccessfulLogin,
                                 final OnLoginError onLoginError
    ){
        RetrofitService retrofitService = new RetrofitService();

        BodyRequestSingIn singIn = new BodyRequestSingIn().setEmail(email).setPassword(password);

        Gson gson = new Gson();
        String json = gson.toJson(singIn);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        API api = retrofitService.getRetrofit().create(API.class);

        Call<ResponseSingIn> singInResponseCall = api.postSingIn(requestBody);

        singInResponseCall.enqueue(new Callback<ResponseSingIn>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSingIn> call, @NonNull retrofit2.Response<ResponseSingIn> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {

                    ResponseSingIn userData = response.body();

                    User user = new User(ClassContext);

                    user.SaveUserDara(userData,
                            () -> {
                                if (onSuccessfulLogin != null) {
                                    onSuccessfulLogin.onSuccess(); // Call the success callback
                                }
                                Toast.makeText(ClassContext.getApplicationContext(), "AUTHORIZED", Toast.LENGTH_SHORT).show();

                                SaveUser.setLogin(ClassContext);
                                new Service(ClassContext).buildData();

                            });

                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ){
                    Toast.makeText(ClassContext.getApplicationContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                    if (onLoginError != null) {
                        onLoginError.onError(); // Call the error callback
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSingIn> call, @NonNull Throwable t) {
                Toast.makeText(ClassContext.getApplicationContext(), "Ошибка Соединения\n проверьте соединение ", Toast.LENGTH_SHORT).show();
                if (onLoginError != null) {
                    onLoginError.onError(); // Call the error callback
                }
            }
        });
    }

    public interface OnLoginSuccess {
        void onSuccess();
    }

    public interface OnLoginError {
        void onError();
    }
}
