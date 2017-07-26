package com.zachbartholomew.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.editTextCity)
    EditText cityEditText;

    @OnClick(R.id.buttonSearchCity)
    public void launchIntent(View view) {
        if (!cityEditText.getText().toString().equals("")) {
            Intent intent = new Intent(view.getContext(), CityWeatherActivity.class);
            intent.putExtra("city", cityEditText.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(view.getContext(), "Please enter a city before clicking search.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }
}
