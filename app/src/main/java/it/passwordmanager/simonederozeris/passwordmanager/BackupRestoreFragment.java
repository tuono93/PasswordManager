package it.passwordmanager.simonederozeris.passwordmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.GestioneFlussoApp;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BackupRestoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BackupRestoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackupRestoreFragment extends Fragment implements OnBackPressed {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MainActivity mainActivity;
    Exception mException = null;
    BackupRestoreFragment fragment;


    private OnFragmentInteractionListener mListener;

    public BackupRestoreFragment() {}

    public static BackupRestoreFragment newInstance() {
        BackupRestoreFragment fragment = new BackupRestoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_backup_restore, container, false);
    }

    @Override
    public void onBackPressed() {
         if (mainActivity.drawerOpened){
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
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
