package com.example.shop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.shop.R;

public class AfterBuyActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btn_continue;
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_buy);
        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.thanks_video);
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
        if (view==btn_continue){
            Intent it= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
    }
}