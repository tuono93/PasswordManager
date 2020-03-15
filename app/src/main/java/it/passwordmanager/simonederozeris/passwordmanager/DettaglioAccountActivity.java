package it.passwordmanager.simonederozeris.passwordmanager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.GestioneFlussoApp;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.PasswordManagerDatabase;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.password.ManagePassword;

public class DettaglioAccountActivity extends AppCompatActivity {

    TextView titleToolbar;
    public Toolbar toolbar;
    PasswordManagerDatabase db;
    Exception mException = null;
    DettaglioAccountActivity activity;
    Action action;
    private int id;
    private int scrollPosition = 0;
    public boolean startIntent = false;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        activity = this;
        setContentView(R.layout.activity_dettaglio_account);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbarDettaglio);
        titleToolbar = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        titleToolbar.setText(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.back);

        scrollPosition = getIntent().getIntExtra("scroll_position",0);

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.dropdown_item_autocomplete, Constant.NOMI_ACCOUNT);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteNomeAccount);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        editPassword = findViewById(R.id.key_text);
        EditText editNomeAccount = findViewById(R.id.nome_utente_text);
        EditText editNote = findViewById(R.id.note_text);

        action = Action.getEnumStatoGestione(getIntent().getStringExtra("action"));
        if(action == Action.UPDATE){
            try {
                String nomeAccount = getIntent().getStringExtra("nomeAccount");
                String nomeUtente = getIntent().getStringExtra("nomeUtente");
                String password = getIntent().getStringExtra("password");
                String note = getIntent().getStringExtra("note");
                id = getIntent().getIntExtra("id", 0);
                ManagePassword pwd = new ManagePassword();
                password = pwd.decrypt(password);
                actv.setText(nomeAccount);
                editNomeAccount.setText(nomeUtente);
                editPassword.setText(password);
                editNote.setText(note);
            } catch (Exception e){
                String error = "Errore nel decriptaggio dell'account";
                Toast.makeText(activity,error,Toast.LENGTH_SHORT).show();
            }
        };
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backToMain();
                    }
                }, 100);
                break;
            case R.id.saveAccount:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AutoCompleteTextView nomeAccountAtv  = findViewById(R.id.autoCompleteNomeAccount);
                        EditText nomeUtenteEt  = findViewById(R.id.nome_utente_text);
                        EditText passwordEt  = findViewById(R.id.key_text);
                        EditText noteEt  = findViewById(R.id.note_text);
                        String nomeAccount = nomeAccountAtv != null ? nomeAccountAtv.getText().toString().trim() : null;
                        String nomeUtente = nomeUtenteEt != null ? nomeUtenteEt.getText().toString().trim() : null;
                        String password = passwordEt != null ? passwordEt.getText().toString().trim() : null;
                        String note = noteEt != null ? noteEt.getText().toString().trim() : null;

                        String error = checkTextinput(nomeAccount,nomeUtente,password,note);
                        if(error.equals("")){
                            try {
                                ManagePassword pwd = new ManagePassword();
                                password = pwd.encrypt(password);
                                if (action == Action.INSERT) {
                                    Account account = new Account(nomeAccount, nomeUtente, password, note);
                                    db = PasswordManagerDatabase.getDatabase(activity);
                                    insertUpdateAccountDB(account, true);
                                } else {
                                    Account account = new Account(nomeAccount, nomeUtente, password, note);
                                    account.setId(id);
                                    db = PasswordManagerDatabase.getDatabase(activity);
                                    insertUpdateAccountDB(account, false);
                                }
                            }catch (Exception e){
                                error = "Errore nel criptaggio dell'account";
                                Toast.makeText(activity,error,Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 100);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String checkTextinput(String nomeAccount, String nomeUtente, String password,String note){
        String error = "";

        if(nomeAccount == null || nomeAccount.equals("")){
            error = "l'account è obbligatorio";
        } else if (nomeUtente == null || nomeUtente.equals("")){
            error = "Il nome utente è obbligatorio";
        } else if (password == null || password.equals("")){
            error = "La password è obbligatoria";
        }

        return error;
    }

    public void insertUpdateAccountDB(Account account,boolean insert){
        mException = null;
        new ReadDBAsync(account,insert).execute();
        db.destroyInstance();
    }

    private class ReadDBAsync extends AsyncTask<Void,Void,Void>{

        private Account account;
        private boolean insert;

        ReadDBAsync(Account account,boolean insert){
            this.account = account;
            this.insert = insert;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... inputs) {
            List<Account> accountList = null;
            try {
                if(insert) {
                    db.getAccountDAO().insertAccount(this.account);
                } else {
                    db.getAccountDAO().updateAccount(this.account);
                }
            }  catch (Exception e) {
                mException = e;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate(voids);
        }

        @Override
        protected void onPostExecute(Void voidParm) {
            super.onPostExecute(voidParm);
            if(mException == null){
                if(insert) {
                    MainActivity.stringSnackStatic = getString(R.string.newAccount);
                } else {
                    MainActivity.stringSnackStatic = getString(R.string.modifyAccount);
                }
                backToMain();
            } else {
                Toast.makeText(activity,mException.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(!startIntent) {
            GestioneFlussoApp.flussoRegolare = false;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }


    @Override
    protected void onStart(){
        super.onStart();
        if(!GestioneFlussoApp.flussoRegolare) {
            this.finish();
            Intent toCheck = new Intent(this, CheckPwdActivity.class);
            startActivity(toCheck);
        }
     }



    @Override
    public void onBackPressed() {
        backToMain();
    }

    public void backToMain(){
        AccountListFragment.positionScroll = scrollPosition;
        GestioneFlussoApp.flussoRegolare = true;
        startIntent = true;
        Intent toMain = new Intent(activity, MainActivity.class);
        startActivity(toMain);
        activity.finish();
    }
}
