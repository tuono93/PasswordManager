package it.passwordmanager.simonederozeris.passwordmanager;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.FlussoModificaPwd;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.TipoStatoPwd;

public class MainActivity extends AppCompatActivity {

    TextView titleToolbar;
    public Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        titleToolbar = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        titleToolbar.setText(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.menu_button2);
        setDrawer();
        setNavigationView();
        Fragment fragmentAccountList = AccountListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.anchor_point_main,fragmentAccountList).commit();
    }

    public void setDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener(){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                Log.d("drawer","Drawer Opened");
            }

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                Log.d("drawer","Drawer Closed");
            }
        });
    }

    public void setNavigationView(){
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d("navView","Selected" + menuItem);
                final int itemId = menuItem.getItemId();
                switch(itemId){
                    case R.id.list_account:
                        Fragment fragmentAccountList = AccountListFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.anchor_point_main,fragmentAccountList).commit();
                        break;
                    case R.id.cambia_pwd:
                        Fragment fragmentCheckPwd = CheckPwdFragment.newInstance(new FlussoModificaPwd(), TipoStatoPwd.OK,R.id.anchor_point_main);
                        getSupportFragmentManager().beginTransaction().replace(R.id.anchor_point_main,fragmentCheckPwd).commit();
                        break;
                    case R.id.esci:
                        finish();
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
