package ru.ushell.app;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * <p>
 *    Загрузачный экран приложения
 * </p>
 * Сцена небольщой загрзки даннвых в котором появляеться лого и при необходимости подкачиваютьсмя данные
 */

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 3000;
    boolean flag = true;

    ImageView LogoApp;
    TextView NameApp, SloganApp;

    Animation topAnime, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.splash_screen_activity);

        LogoApp = findViewById(R.id.LogoApp);
        NameApp = findViewById(R.id.NameApp);
        SloganApp = findViewById(R.id.slogan);

        topAnime = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


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
        }, SPLASH_SCREEN);
    }


    private void MandatoryFunction(){
        if(flag){
            flag = false;
        }else {
            try {
//                NamesStudentsGroup(formattedDateDB(DataNow()));
//                VisitingStudent();

            } catch (NullPointerException error) {
                System.out.println("Предварительная загрузка данных невозмрожна" + error.getMessage());
            }
        }
    }
}
//@SuppressLint("CustomSplashScreen")
//public class SplashScreen extends AppCompatActivity {
//
//    private static final int SPLASH_SCREEN = 3000;
//
//    Animation topAnim, botAnim;
//    ImageView image;
//    TextView logo, slogan;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);// положение экрана фиксированное
//
//
//        setContentView(R.layout.activity_splash_screen);
//
//        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
//        botAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
//
//        image = findViewById(R.id.useLogo);
//        logo = findViewById(R.id.app_name);
//        slogan = findViewById(R.id.slogan);
//
////        image.setAnimation(topAnim);
//        logo.setAnimation(botAnim);
//        slogan.setAnimation(botAnim);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ImageView logoImageView = findViewById(R.id.useLogo);
//                logoImageView.setImageResource(R.drawable.logo_splash);
//                TranslateAnimation animation = new TranslateAnimation(
//                        Animation.RELATIVE_TO_SELF, 0.0f,
//                        Animation.RELATIVE_TO_PARENT, -1.0f,
//                        Animation.RELATIVE_TO_PARENT, 0.0f,
//                        Animation.RELATIVE_TO_SELF, -1.0f);
//                animation.setDuration(500); // Длительность анимации в миллисекундах
//                logoImageView.startAnimation(animation);
//
//                Bundle bundle = null;
//
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                    Pair[] pairs =  new Pair[2];
//                    pairs[0] = new Pair<View,String>(image,"logo_images");
//                    pairs[1] = new Pair<View,String>(logo,"logo_text");
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
//                    bundle = options.toBundle();
//                }
//
//                Intent intent = new Intent(SplashScreen.this, StartActivity.class);
//                if (bundle == null) {
//                    startActivity(intent);
//                } else {
//                    startActivity(intent, bundle);
//                }
//            }
//        },SPLASH_SCREEN);
//    }
//}
