package it.passwordmanager.simonederozeris.passwordmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.GestioneFlussoApp;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.PasswordManagerDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountListFragment extends Fragment implements OnBackPressed,ManageSearchView {

    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private AccountAdapter adapter;
    private AccountAdapter adapterlongClick;
    private AccountListFragment fragment;
    public boolean longClickState = false;
    public static int positionScroll = 0;
    private MainActivity mainActivity;
    private int countSelect = 0;
    PasswordManagerDatabase db;
    Exception mException = null;
    List<Account> list;
    private String nomeAccount,nomeUtente,password,note;
    private int id;
    boolean searchClick = false;
    TextView textEmpty;
    Toolbar toolbar;
    TextView titleToolbar;


    private OnFragmentInteractionListener mListener;

    public AccountListFragment() {}

    public static AccountListFragment newInstance() {
        AccountListFragment fragment = new AccountListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbarMain);
        titleToolbar = (TextView) mainActivity.findViewById(R.id.toolbar_title);
        mainActivity.setSupportActionBar(toolbar);
        titleToolbar.setText(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.menu_button2);

        View rootView = inflater.inflate(R.layout.fragment_account_list, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fabNewAccount);
        mRecyclerView = rootView.findViewById(R.id.listAccount);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.scrollToPosition(positionScroll);
        LineItemDecoration lineItemDecoration = new LineItemDecoration();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(lineItemDecoration);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        db = PasswordManagerDatabase.getDatabase(getActivity());
        getListDB(false,"");
    }


    public void backFromLongClick(){
        if(searchClick){
            searchClick = false;
            toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbarMain);
            titleToolbar = (TextView) mainActivity.findViewById(R.id.toolbar_title);
            mainActivity.setSupportActionBar(toolbar);
            titleToolbar.setText(R.string.app_name);
            toolbar.setNavigationIcon(R.drawable.menu_button2);

            getListDB(false,"");
            setInitialAdapter(adapter);
            mRecyclerView.setAdapter(adapter);
            ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPosition(positionScroll);

            mainActivity.toolbar.setNavigationIcon(R.drawable.menu_button2);
            mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.drawerLayout.openDrawer(Gravity.LEFT);
                        }
                    }, 200);
                }
            });
        } else {
            longClickState = false;
            for (Account account : list) {
                account.setSelected(false);
            }
            setInitialAdapter(adapter);
            mRecyclerView.setAdapter(adapter);
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(positionScroll);

            countSelect = 0;
            titleToolbar.setText(R.string.app_name);

            mainActivity.optionsMenu.getItem(0).setVisible(false);
            mainActivity.optionsMenu.getItem(1).setVisible(true);

            mainActivity.toolbar.setNavigationIcon(R.drawable.menu_button2);
            mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.drawerLayout.openDrawer(Gravity.LEFT);
                        }
                    }, 200);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(longClickState || searchClick){
            backFromLongClick();
        } else if (mainActivity.drawerOpened){
            mainActivity.drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            String title = "Uscita";
            String message = "Vuoi davvero uscire dall'app?";

            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            GestioneFlussoApp.flussoRegolare = true;
                            getActivity().finish();
                            mainActivity.startIntent = true;
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    private void updateTitleLongClick(boolean moreCount){
        if (moreCount){
            countSelect++;
        } else {
            countSelect--;
        }

        if (countSelect == 0){
            backFromLongClick();
        } else {
            String title = countSelect == 1 ? countSelect + " nomeAccount selezionato" : countSelect + " nomeAccount selezionati";
            titleToolbar.setText(title);
        }
    }

    private void setInitialAdapter(final AccountAdapter adapter){
        adapter.setOnAccountClickListener(new AccountAdapter.onAccountListener() {
            @Override
            public void onAccountClicked(Account account, int position, View v) {
                id = account.getId();
                nomeAccount = account.getNome();
                nomeUtente = account.getNomeUtente();
                password = account.getPassword();
                note = account.getNota();

                positionScroll = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GestioneFlussoApp.flussoRegolare = true;
                        mainActivity.startIntent = true;
                        Intent toMain = new Intent(getActivity(), DettaglioAccountActivity.class);
                        toMain.putExtra("action",Action.UPDATE.getAction());
                        toMain.putExtra("id",id);
                        toMain.putExtra("nomeAccount", nomeAccount);
                        toMain.putExtra("nomeUtente",nomeUtente);
                        toMain.putExtra("password",password);
                        toMain.putExtra("note",note);
                        toMain.putExtra("scroll_position",positionScroll);
                        startActivity(toMain);
                        mainActivity.finish();
                    }
                }, 70);
            }

            @Override
            public void onAccountLongClicked(Account account, int position, View v) {
                id = account.getId();
                AccountListFragment.this.nomeAccount = account.getNome();
                password = account.getPassword();
                note = account.getNota();

                positionScroll = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                account.setSelected(true);
                adapter.notifyDataSetChanged();
                updateTitleLongClick(true);

                longClickState = true;

                mainActivity.toolbar.setNavigationIcon(R.drawable.back);
                mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                backFromLongClick();
                            }
                        }, 200);
                    }
                });
                mainActivity.optionsMenu.getItem(1).setVisible(false);
                MenuItem eliminaItem = mainActivity.optionsMenu.getItem(0);
                eliminaItem.setVisible(true);
                eliminaItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eliminaAccountDB();
                            }
                        }, 200);
                        return false;
                    }
                });



                adapterlongClick = new AccountAdapter(list,fragment);
                adapterlongClick.setOnAccountClickListener(new AccountAdapter.onAccountListener() {
                    @Override
                    public void onAccountClicked(Account account, int position,View v) {
                        id = account.getId();
                        AccountListFragment.this.nomeAccount = account.getNome();
                        password = account.getPassword();
                        note = account.getNota();

                        if(account.isSelected()){
                            account.setSelected(false);
                            updateTitleLongClick(false);
                        } else {
                            account.setSelected(true);
                            updateTitleLongClick(true);
                        }
                        adapterlongClick.notifyDataSetChanged();
                    }

                    @Override
                    public void onAccountLongClicked(Account account, int position, View v) {
                        if(account.isSelected()){
                            account.setSelected(false);
                            updateTitleLongClick(false);
                        } else {
                            account.setSelected(true);
                            updateTitleLongClick(true);
                        }
                        adapterlongClick.notifyDataSetChanged();
                    }
                });

                mRecyclerView.setAdapter(adapterlongClick);
                ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPosition(positionScroll);
            }
        });
    }

    public void getListDB(boolean findByName,String query){
        mException = null;
        new ReadDBAsync(findByName,query).execute();
        db.destroyInstance();
    }

    @Override
    public void onSearchOpen() {
        searchClick = true;
    }

    @Override
    public void onSearchClose() {
        backFromLongClick();
    }

    @Override
    public void onSearchQueryChange(String query) {
        getListDB(true,query);
    }

    private class ReadDBAsync extends AsyncTask<Void,Void, List<Account>> {

        private boolean findByName;
        private String query;

        ReadDBAsync(boolean findByName,String query){
            this.findByName = findByName;
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Account> doInBackground(Void... voids) {
            List<Account> accountList = null;
            try {
                if(!findByName) {
                    accountList = db.getAccountDAO().findAllAccount();
                } else {
                    accountList = db.getAccountDAO().findByName(query);
                }
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
                list = result;
                textEmpty.setVisibility(View.GONE);
                adapter = new AccountAdapter(list, fragment);
                setInitialAdapter(adapter);
                mRecyclerView.setAdapter(adapter);
                if(list.isEmpty()) {
                    ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(positionScroll);
                    textEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getActivity(),mException.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    public void eliminaAccountDB(){
        String title = "Eliminazione nomeAccount";
        String message = countSelect==1 ? "Vuoi davvero eliminare l'nomeAccount selezionato?" : "Vuoi davvero eliminare i " + countSelect + " nomeAccount selezionati?";

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mException = null;
                        new EliminaDBAsync(list).execute();
                        db.destroyInstance();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private class EliminaDBAsync extends AsyncTask<Void,Void, List<Account>> {

        private List<Account> accountList;

        EliminaDBAsync(List<Account> accountList){
            this.accountList = accountList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Account> doInBackground(Void... voids) {
            try {
                List<Account> accountListDaEliminare = new ArrayList<>();
                for (Account account : accountList){
                    if (account.isSelected()){
                        accountListDaEliminare.add(account);
                    }
                }
                db.getAccountDAO().deleteAccount(accountListDaEliminare);
                accountList = db.getAccountDAO().findAllAccount();
            }  catch (Exception e) {
                mException = e;
            }
            return accountList;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate(voids);
        }

        @Override
        protected void onPostExecute(List<Account> accountList) {
            super.onPostExecute(accountList);
            if(mException == null){
                list = accountList;
                adapter = new AccountAdapter(list,fragment);
                backFromLongClick();
                View parentLayout = getActivity().findViewById(android.R.id.content);
                Snackbar.make(parentLayout,getActivity().getString(R.string.deleteAccount),Snackbar.LENGTH_SHORT).show();
                if(list.isEmpty()){
                    TextView textEmpty = getActivity().findViewById(R.id.empty_view);
                    textEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getActivity(),mException.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestioneFlussoApp.flussoRegolare = true;
                mainActivity.startIntent = true;
                Intent toMain = new Intent(getActivity(), DettaglioAccountActivity.class);
                toMain.putExtra("action",Action.INSERT.getAction());
                startActivity(toMain);
                mainActivity.finish();
            }
        });
        textEmpty = getActivity().findViewById(R.id.empty_view);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
