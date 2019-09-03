package it.passwordmanager.simonederozeris.passwordmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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
public class AccountListFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private AccountAdapter adapter;
    private AccountAdapter adapterlongClick;
    private AccountListFragment fragment;

    PasswordManagerDatabase db;
    Exception mException = null;
    List<Account> list;
    private String nome,password,note;
    private int id;

    private OnFragmentInteractionListener mListener;

    public AccountListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountListFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        View rootView = inflater.inflate(R.layout.fragment_account_list, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fabNewAccount);
        mRecyclerView = rootView.findViewById(R.id.listAccount);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.scrollToPosition(0);
        LineItemDecoration lineItemDecoration = new LineItemDecoration();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(lineItemDecoration);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        db = PasswordManagerDatabase.getDatabase(getActivity());
        getListDB();
    }

    public void getListDB(){
        mException = null;
        new ReadDBAsync().execute();
        db.destroyInstance();
    }

    private class ReadDBAsync extends AsyncTask<Void,Void, List<Account>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Account> doInBackground(Void... voids) {
            List<Account> accountList = null;
            try {
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
                list = result;
                adapter = new AccountAdapter(list,fragment);
                adapter.setOnAccountClickListener(new AccountAdapter.onAccountListener() {
                    @Override
                    public void onAccountClicked(Account account, int position, View v) {
                        id = account.getId();
                        nome = account.getNome();
                        password = account.getPassword();
                        note = account.getNota();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent toMain = new Intent(getActivity(), DettaglioAccountActivity.class);
                                toMain.putExtra("action",Action.UPDATE.getAction());
                                toMain.putExtra("id",id);
                                toMain.putExtra("nome",nome);
                                toMain.putExtra("password",password);
                                toMain.putExtra("note",note);
                                startActivity(toMain); //perform Task
                            }
                        }, 70);
                    }

                    @Override
                    public void onAccountLongClicked(Account account, int position, View v) {
                        id = account.getId();
                        nome = account.getNome();
                        password = account.getPassword();
                        note = account.getNota();

                        Log.i("LONG","" + id);
                        Log.i("LONG","" + nome);
                        Log.i("LONG","" + password);
                        Log.i("LONG","" + note);

                        account.setSelected(true);
                        adapter.notifyDataSetChanged();

                        adapterlongClick = new AccountAdapter(list,fragment);
                        adapterlongClick.setOnAccountClickListener(new AccountAdapter.onAccountListener() {
                            @Override
                            public void onAccountClicked(Account account, int position,View v) {
                                id = account.getId();
                                nome = account.getNome();
                                password = account.getPassword();
                                note = account.getNota();

                                account.setSelected(true);
                                adapterlongClick.notifyDataSetChanged();

                                Log.i("SHORT","" + id);
                                Log.i("SHORT","" + nome);
                                Log.i("SHORT","" + password);
                                Log.i("SHORT","" + note);
                            }

                            @Override
                            public void onAccountLongClicked(Account account, int position, View v) {
                                account.setSelected(true);
                                adapterlongClick.notifyDataSetChanged();                            }
                        });

                        mRecyclerView.setAdapter(adapterlongClick);
                    }
                });
                mRecyclerView.setAdapter(adapter);
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
                Intent toMain = new Intent(getActivity(), DettaglioAccountActivity.class);
                toMain.putExtra("action",Action.INSERT.getAction());
                startActivity(toMain);
            }
        });
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
