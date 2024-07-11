package ru.ushell.app.ui.ModelProfile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.ushell.app.R;

public class DetailProfile extends AppCompatActivity {
    TextView detailDesc, detailTitle;
    ImageView detailImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info_detail);
        detailDesc = findViewById(R.id.detailDes);
        detailTitle = findViewById(R.id.detailTitle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            System.out.println("not null");
//            detailDesc.setText(bundle.getInt("Desc"));
            detailTitle.setText(bundle.getInt("Titel"));
        }
    }
}