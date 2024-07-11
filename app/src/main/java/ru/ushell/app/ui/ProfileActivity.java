package ru.ushell.app.ui;

import static ru.ushell.app.ui.ModelTimeTable.lesson.Lesson.LessonsList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ru.ushell.app.R;
import ru.ushell.app.SQLite.DatabaseHelper;
import ru.ushell.app.StartActivity;
import ru.ushell.app.models.Student;
import ru.ushell.app.models.User;
import ru.ushell.app.ui.ModelProfile.DataClass;
import ru.ushell.app.ui.ModelProfile.InfoProfileAdapter;
import ru.ushell.app.ui.SavingSession.SaveUser;
import ru.ushell.app.ui.TableListStudent.Table.Table;


/**
 * Сцена профиля пользователя в которой отображаеться вся личная информация:
 * <pre>
 * ГРУППА
 * ФИО
 * И.Т.Д
 * </pre>
 */


public class ProfileActivity extends AppCompatActivity {
    private User user;
    private ProgressBar progressBar;
    RelativeLayout root;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    InfoProfileAdapter adapter;
    DataClass androidData;
    DatabaseHelper databaseHelperMain;

    ///



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// положение экрана фиксированное

        root = findViewById(R.id.RootProfile);

        recyclerView = findViewById(R.id.info_person);


        DataUserChanged();

        // Модуль обновления окна путем свайпа вниз
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
                    // Вернуть true, если можно прокрутить вверх
                    // Обновить сцену здесь, например, вызвать метод для обновления данных
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                DataProfile();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            makeBlurImage();
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProfileActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();

        androidData = new DataClass(R.string.userinfo,R.string.userinfo, R.drawable.profile_icon_group);
        dataList.add(androidData);

        androidData = new DataClass(R.string.notify,R.string.notify, R.drawable.profile_icon_notify);
        dataList.add(androidData);

        androidData = new DataClass(R.string.news,R.string.news, R.drawable.profile_icon_news);
        dataList.add(androidData);

        adapter = new InfoProfileAdapter(ProfileActivity.this, dataList);
        recyclerView.setAdapter(adapter);
        ///
//        RecyclerView и SwipeRefreshLayout
        ///
    }

    /**
     * метод onResume().
     * Этот метод вызывается, когда фрагмент или активность становятся видимыми для пользователя.
     * Внутри этого метода вы можете выполнить код для загрузки данных
     */
    @Override
    public void onResume() {
        super.onResume();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        DataProfile();
        DataUserChanged();
        Log.d("MyFragment", "Loading data...");

        progressBar.setVisibility(View.GONE); // Скрыть прогресс-бар после завершения загрузки данных
    }

    @RequiresApi(31)
    private void makeBlurImage() {
        RenderEffect blurEffect = RenderEffect.createBlurEffect(15f, 15f, Shader.TileMode.MIRROR);
        findViewById(R.id.image_back).setRenderEffect(blurEffect);

    }

    /**
     * Функция выводы данных о пользователе
     */
    private void DataProfile() {
        TextView NameUser = findViewById(R.id.NameUser);
        TextView NameGroup = findViewById(R.id.GroupUser);

        NameUser.setText(User.getInstance(this).getFullName());
        NameGroup.setText(User.getGroupName());

    }

    /** Временный модуль подсчета студентов
     *
     */


    /**
     * Преобразование ФИО в ИФ
     */
    @SuppressLint("Range")
    public void DataUserChanged() {
    }


    /** Кнопка выхода из приложения
     *
     * @param view
     */
    public void Exit(View view) {
        SaveUser.userLogOut(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
        Table.TableStudentList.clear();
        LessonsList.clear();
        if (databaseHelperMain == null) {
            databaseHelperMain = new DatabaseHelper(this);
        }
        databaseHelperMain.deleteAllData();
        User.clear();
        Student.StudentDataList.clear();
        finish();
    }

    /**
     * Переход на другие сцены
     */
    public void Timetable(View v){
        Intent intent = new Intent(this, TimeTableActivity.class);
        startActivity(intent);
        // Применение анимации появления для активности
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    public void TableList(View v){
        Intent intent = new Intent(this, TableListActivity.class);
        startActivity(intent);
    }
}