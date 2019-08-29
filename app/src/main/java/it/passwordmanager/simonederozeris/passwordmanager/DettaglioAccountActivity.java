package it.passwordmanager.simonederozeris.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DettaglioAccountActivity extends AppCompatActivity {

    TextView titleToolbar;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_account);
        toolbar = (Toolbar) findViewById(R.id.toolbarDettaglio);
        titleToolbar = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        titleToolbar.setText(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent toMain = new Intent(this, MainActivity.class);
                startActivity(toMain);
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
