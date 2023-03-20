package com.example.shop.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.shop.R;
import com.example.shop.archives.BasicActivity;

public class HomeActivity extends BasicActivity implements View.OnClickListener {

    ImageView btn_start;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video);
        videoView.setVideoURI(uri);
        videoView.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    public void onClick(View view) {
        if (view==btn_start){  // start the application
            Intent it= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
    }
}