package it.passwordmanager.simonederozeris.passwordmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.PasswordManagerDatabase;

public class DettaglioAccountActivity extends AppCompatActivity {

    TextView titleToolbar;
    public Toolbar toolbar;
    PasswordManagerDatabase db;
    Exception mException = null;
    DettaglioAccountActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
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
                AutoCompleteTextView nomeAccountAtv  = findViewById(R.id.autoCompleteNomeAccount);
                EditText passwordEt  = findViewById(R.id.key_text);
                EditText noteEt  = findViewById(R.id.note_text);
                String nomeAccount = nomeAccountAtv != null ? nomeAccountAtv.getText().toString().trim() : null;
                String password = passwordEt != null ? passwordEt.getText().toString().trim() : null;
                String note = noteEt != null ? noteEt.getText().toString().trim() : null;

                String error = checkTextinput(nomeAccount,password,note);
                if(error.equals("")){
                    Account account = new Account(nomeAccount,password,note);
                    db = PasswordManagerDatabase.getDatabase(this);
                    insertAccountDB(account);
                } else {
                    Toast.makeText(activity,error,Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String checkTextinput(String nomeAccount, String password,String note){
        String error = "";

        if(nomeAccount == null || nomeAccount.equals("")){
            error = "Il nome dell'account è obbligatorio";
        } else if (password == null || password.equals("")){
            error = "La password è obbligatoria";
        }

        return error;
    }

    public void insertAccountDB(Account account){
        mException = null;
        new ReadDBAsync(account).execute();
        db.destroyInstance();
    }

    private class ReadDBAsync extends AsyncTask<Void,Void, List<Account>> {

        private Account account;

        ReadDBAsync(Account account){
            this.account = account;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Account> doInBackground(Void... inputs) {
            List<Account> accountList = null;
            try {
                db.getAccountDAO().insertAccount(this.account);
                accountList = db.getAccountDAO().findAllAccount();
            }  catch (Exception e) {
                mException = e;
            }
            return accountList;
        }

        @Override
        protected void onProgressUpdate(Void... voids)
        {
            super.onProgressUpdate(voids);
        }

        @Override
        protected void onPostExecute(List<Account> result)
        {
            super.onPostExecute(result);
            if(mException == null){
                for (Account account : result) {
                    Log.i("ACCOUNT ", "ID ACCOUNT: " + account.getId());
                    Log.i("ACCOUNT ", "NOME ACCOUNT: " + account.getNome());
                    Log.i("ACCOUNT ", "PASSWORD ACCOUNT: " + account.getPassword());
                    Log.i("ACCOUNT ", "NOTE ACCOUNT: " + account.getNota());
                    MainActivity.stringSnackStatic = getString(R.string.newAccount);
                    activity.finish();
                }
            } else {
                Toast.makeText(activity,mException.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
