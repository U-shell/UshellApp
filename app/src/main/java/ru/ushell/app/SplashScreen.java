package ru.ushell.app;


import static ru.ushell.app.calendar.CalendarUtils.DataNow;
import static ru.ushell.app.calendar.CalendarUtils.formattedDateDB;
import static ru.ushell.app.ui.Sudentset.StudentDataRiteDb.VisitingStudent;
import static ru.ushell.app.ui.TableListStudent.Table.TableListReadDb.NamesStudentsGroup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


/**
 * <p>
 *    Загрузачный экран приложения
 * </p>
 * Сцена небольщой загрзки даннвых в котором появляеться лого и при необходимости подкачиваютьсмя данные
 */

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MandatoryFunction();
                startActivities(new Intent[]{new Intent(SplashScreen.this, StartActivity.class)});
                finish();
                /**
                 * *slide_in и slide_out применяються для перехода между загрузочным экраном и главными сценами  в приложении
                 */
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        }, 1);
    }


    private void MandatoryFunction(){
        if(flag){
            flag = false;
        }else {
            try {
                NamesStudentsGroup(formattedDateDB(DataNow()));
                VisitingStudent();

            } catch (NullPointerException error) {
                System.out.println("Предварительная загрузка данных невозмрожна" + error.getMessage());
            }
        }
    }
}
