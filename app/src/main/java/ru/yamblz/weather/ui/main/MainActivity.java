package ru.yamblz.weather.ui.main;

import android.os.Bundle;

import ru.yamblz.weather.R;
import ru.yamblz.weather.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
