package it.passwordmanager.simonederozeris.passwordmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.FlussoCheckPwd;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.FlussoModificaPwd;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.TipoStatoPwd;


public class CheckPwdActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private String PASSCODE = "passcode";
    //public ProgressDialog loadEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pwd);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.icon_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.LightGoldenrodYellow));
        toolbar.setTitle(R.string.app_name);

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PASSCODE, Context.MODE_PRIVATE);

        if(sharedPreferences.getString(Constant.PASSCODE_VALUE,null)==null){
            Fragment fragmentCheckPwd = CheckPwdFragment.newInstance(new FlussoModificaPwd(), TipoStatoPwd.OK,R.id.anchor_point_pwd);
            getSupportFragmentManager().beginTransaction().replace(R.id.anchor_point_pwd,fragmentCheckPwd).commit();
        } else {
            Fragment fragmentCheckPwd = CheckPwdFragment.newInstance(new FlussoCheckPwd(), TipoStatoPwd.OK, R.id.anchor_point_pwd);
            getSupportFragmentManager().beginTransaction().replace(R.id.anchor_point_pwd, fragmentCheckPwd).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}