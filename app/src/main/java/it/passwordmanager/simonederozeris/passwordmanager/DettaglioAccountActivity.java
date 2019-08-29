package it.passwordmanager.simonederozeris.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.dropdown_item_autocomplete, Constant.NOMI_ACCOUNT);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteNomeAccount);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.saveAccount:
                Log.i("SAVE","Click salva");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
