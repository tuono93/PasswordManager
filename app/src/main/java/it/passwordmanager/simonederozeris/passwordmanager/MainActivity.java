package it.passwordmanager.simonederozeris.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.GestioneFlussoApp;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.PasswordManagerDatabase;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.FlussoModificaPwd;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.TipoStatoPwd;

public class MainActivity extends AppCompatActivity {

    TextView titleToolbar;
    public Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MainActivity mainActivity;
    static String stringSnackStatic = "";
    public Menu optionsMenu;
    public boolean startIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
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
        String stringSnack = getIntent().getStringExtra("view_snack");
        if(stringSnack != null && !stringSnack.equals("")){
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, stringSnack, Snackbar.LENGTH_LONG).show();
        }
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
        getMenuInflater().inflate(R.menu.menu_toolbar_main_long_click,menu);
        menu.getItem(0).setVisible(false);
        optionsMenu = menu;
        setSearchListener();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }
                }, 200);
                break;
            case R.id.eliminaAccount:
                break;
            case R.id.cercaAccount:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(GestioneFlussoApp.flussoRegolare) {
            if (!stringSnackStatic.equals("")) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, stringSnackStatic, Snackbar.LENGTH_LONG).show();
                stringSnackStatic = "";
            }
        } else {
            this.finish();
            Intent toCheck = new Intent(this, CheckPwdActivity.class);
            startActivity(toCheck);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(!startIntent) {
            GestioneFlussoApp.flussoRegolare = false;
        }
    }

    @Override
    public void onBackPressed() {
        if(!tellFragments()){
            super.onBackPressed();
        }
    }

    private boolean tellFragments(){
        boolean manageFromFragment = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments) {
            if (f != null && f.getClass().getName().equals(AccountListFragment.class.getName())) {
                manageFromFragment = true;
                ((AccountListFragment) f).onBackPressed();
            }
        }

        return manageFromFragment;
    }

    public void setSearchListener(){
        MenuItem searchItem = mainActivity.optionsMenu.getItem(1);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getColor(R.color.Gray));
        searchAutoComplete.setTextColor(getColor(R.color.White));
        ImageView searchIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setImageDrawable(getDrawable(R.drawable.search));

        ImageView searchCloseIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageDrawable(getDrawable(R.drawable.close));

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments) {
            if (f != null && f.getClass().getName().equals(AccountListFragment.class.getName())) {
                    final AccountListFragment fragList = ((AccountListFragment) f);
                    searchView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("SEARCH_VIEW","Search clicked");
                        }
                    });

                    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                        @Override
                        public boolean onClose() {
                            Log.d("SEARCH_VIEW","Search closed");
                            fragList.onSearchClose();
                            return false;
                        }
                    });

                    searchView.setOnSearchClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragList.onSearchOpen();
                            Log.d("SEARCH_VIEW","Search Click");
                        }
                    });

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            //searchItem.collapseActionView();
                            Log.d("SEARCH_VIEW","Query Text Submit! " + query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            Log.d("SEARCH_VIEW","Query Text Changed! " + newText);
                            fragList.onSearchQueryChange(newText+"%");
                            return false;
                        }
                    });
            }
        }
    }

}
