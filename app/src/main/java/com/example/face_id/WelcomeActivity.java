package com.example.face_id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;


public class WelcomeActivity extends AppCompatActivity {

    private ImageButton btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView bgPattern = findViewById(R.id.bgPattern);
        View background = findViewById(R.id.activity_welcome);

        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);

            Pair<View, String> p1 = Pair.create(bgPattern, "bgPattern");
            Pair<View, String> p2 = Pair.create(background, "background_trans");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(WelcomeActivity.this, p1, p2);
            Bundle bundle = options.toBundle();

            if (bundle != null) {
                startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }

            finish();
        });
    }
}


