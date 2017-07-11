package com.zachbartholomew.rxandroidsimpleexample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zachbartholomew.rxandroidsimpleexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding mainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainBinding.startActivity1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SampleRxActivity1.class));
            }
        });

        mainBinding.startActivity2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SampleRxActivity2.class));
            }
        });

        mainBinding.startActivity3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SampleRxActivity3.class));
            }
        });
    }
}
