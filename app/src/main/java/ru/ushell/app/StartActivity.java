package ru.ushell.app;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.BodyRequestSingIn;
import ru.ushell.app.api.response.ResponseSingIn;
import ru.ushell.app.interfaces.OnDataSavedListener;
import ru.ushell.app.models.User;
import ru.ushell.app.ui.ProfileActivity;
import ru.ushell.app.ui.SavingSession.SaveUser;


/**
 * Начальная страница взаимодействаия пользователя с приложением.Окно входа в приложение.
 * Модуль входа в приложение
 */
public class StartActivity extends AppCompatActivity {
    Button btnSingIn;
    TextView btnRegister; // Добавляем кнопки входа и регистрации
    RelativeLayout root;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// положение экрана фиксированное

        //сделать функцию сохр. логов пользователя
        if(SaveUser.isLogin(getApplicationContext())) {
            startActivity(new Intent(StartActivity.this, ProfileActivity.class));
            finish();
        }else{
            SaveUser.userLogOut(getApplicationContext());
        }

        root = findViewById(R.id.root_element);

        btnSingIn = findViewById(R.id.btnSingIn);
        btnRegister = findViewById(R.id.btnRegister);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            makeBlurImageNull();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                shouldShowRequestWindow();
            }
        });

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    makeBlurImage();
                }
                shouldShowSingInWinDow();
            }
        });

    }

    @RequiresApi(31)
    private void makeBlurImageNull(){
        findViewById(R.id.root_element).setRenderEffect(null);

    }

    @RequiresApi(31)
    private void makeBlurImage(){
        RenderEffect blurEffect = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP);
        findViewById(R.id.root_element).setRenderEffect(blurEffect);

    }


    private void TransitionToNextActivity(){
        SaveUser.setLogin(getApplicationContext());

        startActivity(new Intent(StartActivity.this, ProfileActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        finish();
    }




    private void loginUser(String email, String password) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View overlayView = inflater.inflate(R.layout.progress_bar, null);
        ProgressBar progressBar = overlayView.findViewById(R.id.progress);

        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        rootView.addView(overlayView);

        RetrofitService retrofitService = new RetrofitService();

        BodyRequestSingIn singIn = new BodyRequestSingIn().setEmail(email).setPassword(password);

        Gson gson = new Gson();
        String json = gson.toJson(singIn);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        API api = retrofitService.getRetrofit().create(API.class);

        Call<ResponseSingIn> singInResponseCall = api.postSingIn(requestBody);

        singInResponseCall.enqueue(new Callback<ResponseSingIn>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSingIn> call, @NonNull Response<ResponseSingIn> response) {

                if (response.isSuccessful() && response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {

                    progressBar.setVisibility(View.INVISIBLE); // запустить анимацию

                    ResponseSingIn userData = response.body();

                    User user = new User(StartActivity.this);

                    user.saveUserData(userData.getId(), userData.getFirstname(), userData.getLastname(), userData.getMiddlename(),
                            userData.getEmail(), new HashSet<>(userData.getRoles()),
                            userData.getAccessToken(), userData.getTokenType(),
                            new OnDataSavedListener() {
                                @Override
                                public void onDataSaved() {

                                    progressBar.setVisibility(View.GONE); // Скрыть прогресс-бар после завершения загрузки данных

                                    Toast.makeText(StartActivity.this, "AUTHORIZED", Toast.LENGTH_SHORT).show();

                                    TransitionToNextActivity();
                                }
                            });


                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ){
                    progressBar.setVisibility(View.GONE); // Скрыть прогресс-бар после завершения загрузки данных
                    overlayView.setVisibility(View.GONE);
                    Toast.makeText(StartActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSingIn> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE); // Скрыть прогресс-бар после завершения загрузки данных
                overlayView.setVisibility(View.GONE);
                Toast.makeText(StartActivity.this, "Ошибка Соединения\n проверьте соединение ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void registerUser(String username, String password) {
        return;
    }

    // окна входа / регистрации
    private void shouldShowSingInWinDow(){
        // диалоговое окно
        AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.CustomDialogs);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные");

        // получение шаболона для регистрации
        LayoutInflater inflater = LayoutInflater.from(this);// получение шаблона
        View sine_in_window = inflater.inflate(R.layout.sing_in_window,null);// обрашение к шаблону входа
        dialog.setView(sine_in_window); // шаблон для всплываюшего окна

        // ID полей входа
        final TextInputEditText email = sine_in_window.findViewById(R.id.EmailField);
        final TextInputEditText password = sine_in_window.findViewById(R.id.PasswordField);

        // кнопка отмены
        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // закрытие окна

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    makeBlurImageNull();
                }
            }
        });

        // кнопка входа
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(Objects.requireNonNull(email.getText()).toString())){
                    Snackbar.make(root, "Введи email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(Objects.requireNonNull(password.getText()).toString().isEmpty()){
                    Snackbar.make(root, "Введи пароль",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                TransitionToNextActivity();

                // авторизация пользователя

                loginUser(email.getText().toString(),password.getText().toString());

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    makeBlurImageNull();}            }
        });

        dialog.show();
    }

    private void shouldShowRequestWindow() {
        // всплываюшие окно
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Зарегистрирроваться");
        dialog.setMessage("Введите данные");

        // получение шаболона для регистрации
        LayoutInflater inflater = LayoutInflater.from(this);// получение шаблона
        View regoster_window = inflater.inflate(R.layout.register_window,null);

        dialog.setView(regoster_window);// шаблон для всплываюшего окна

        final MaterialEditText email = regoster_window.findViewById(R.id.EmailField);
        final MaterialEditText password = regoster_window.findViewById(R.id.passField);
        final MaterialEditText name = regoster_window.findViewById(R.id.nameField);
        final MaterialEditText group = regoster_window.findViewById(R.id.groupField);

        // создание кнопок
        // кнопка отмены
        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();// закрытие окна
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Snackbar.make(root, "Введи ФИО",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(group.getText().toString())){
                    Snackbar.make(root, "Введи Группу",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root, "Введи email",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() < 5){
                    Snackbar.make(root, "Введи passord len > 5",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // регистрация пользователей
                registerUser(email.getText().toString(),password.getText().toString());
            }
        });
        dialog.show();
    }
}