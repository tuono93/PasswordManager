package it.passwordmanager.simonederozeris.passwordmanager;

import android.os.Bundle;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ListActivity extends AppCompatActivity {

    public Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.icon_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.LightGoldenrodYellow));
        toolbar.setTitle(R.string.app_name);
    }
}
