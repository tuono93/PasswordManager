package it.passwordmanager.simonederozeris.passwordmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;

import java.util.Collections;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.GestioneFlussoApp;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.PasswordManagerDatabase;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.drive.GestisciOperazioniDrive;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.drive.GoogleDriveFileHolder;


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
    TextView textSignIn;
    SignInButton google_button;
    TextView textLogIn;
    LinearLayout buttons;
    TextView textChangeSignIn ;
    SignInButton google_button_change;
    Button buttonBackup;
    Button buttonRestore;
    private static final int REQUEST_CODE_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private OnFragmentInteractionListener mListener;
    Intent fromSignIn;
    public ProgressDialog loadServizio = null;


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
        View rootView = inflater.inflate(R.layout.fragment_backup_restore, container, false);

        textSignIn = (TextView) rootView.findViewById(R.id.textSignIn);
        google_button = (SignInButton) rootView.findViewById(R.id.google_button);
        textLogIn = (TextView) rootView.findViewById(R.id.textLogIn);
        buttons = (LinearLayout) rootView.findViewById(R.id.buttons);
        textChangeSignIn = (TextView) rootView.findViewById(R.id.textChangeSignIn);
        google_button_change = (SignInButton) rootView.findViewById(R.id.google_button_change);
        buttonBackup = (Button) rootView.findViewById(R.id.backup);
        buttonRestore = (Button) rootView.findViewById(R.id.restore);

        for (int i = 0; i < google_button.getChildCount(); i++) {
            View v = google_button.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(18);
                tv.setText("Sign-in");
            }
        }

        for (int i = 0; i < google_button_change.getChildCount(); i++) {
            View v = google_button_change.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(18);
                tv.setText("Sign-out");
            }
        }
        if (!MainActivity.stringSnackStatic.equals("")) {
            View parentLayout = mainActivity.findViewById(android.R.id.content);
            Snackbar.make(parentLayout, mainActivity.stringSnackStatic, Snackbar.LENGTH_SHORT).show();
            mainActivity.stringSnackStatic = "";
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mainActivity.getApplicationContext());
        if (account != null) {
            textSignIn.setVisibility(View.GONE);
            google_button.setVisibility(View.GONE);
            textLogIn.setText("Sei registrato come " + account.getEmail());
            textLogIn.setVisibility(View.VISIBLE);
            buttons.setVisibility(View.VISIBLE);
            textChangeSignIn.setVisibility(View.VISIBLE);
            google_button_change.setVisibility(View.VISIBLE);
        } else {
            textSignIn.setVisibility(View.VISIBLE);
            google_button.setVisibility(View.VISIBLE);
            textLogIn.setVisibility(View.GONE);
            buttons.setVisibility(View.GONE);
            textChangeSignIn.setVisibility(View.GONE);
            google_button_change.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        google_button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Sign-out";
                String message = "Vuoi effettuare il sign-out dal tuo account Google?";

                new AlertDialog.Builder(mainActivity)
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                signOut();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        buttonBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Backup";
                String message = "Vuoi effettuare il backup su Google Drive?";

                new AlertDialog.Builder(mainActivity)
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                createBackup();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Restore";
                String message = "Vuoi ripristinare i dati da Google Drive?";

                new AlertDialog.Builder(mainActivity)
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                createRestore();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    public void setProgressBar(){
        loadServizio = new ProgressDialog(mainActivity, android.app.AlertDialog.THEME_HOLO_DARK);
        loadServizio.setMessage("Operazione in corso...");
        loadServizio.setCancelable(false);
        loadServizio.setCanceledOnTouchOutside(false);
        loadServizio.show();

    }

    public void createBackup(){
        setProgressBar();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mainActivity.getApplicationContext());

        Log.i("Google",account.getEmail());
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        mainActivity.getApplicationContext(), Collections.singleton(DriveScopes.DRIVE));
        credential.setSelectedAccountName(account.getAccount().name);

        com.google.api.services.drive.Drive googleDriveService =
                new com.google.api.services.drive.Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),
                        credential)
                        .setApplicationName("PasswordManager")
                        .build();

        GestisciOperazioniDrive gestioneDrive = new GestisciOperazioniDrive(googleDriveService,mainActivity);
        gestioneDrive.createBackup(loadServizio);

    }

    public void createRestore(){
        setProgressBar();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mainActivity.getApplicationContext());


        Log.i("Google",account.getEmail());
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        mainActivity.getApplicationContext(), Collections.singleton(DriveScopes.DRIVE));
        credential.setSelectedAccountName(account.getAccount().name);

        com.google.api.services.drive.Drive googleDriveService =
                new com.google.api.services.drive.Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),
                        credential)
                        .setApplicationName("PasswordManager")
                        .build();

        GestisciOperazioniDrive gestioneDrive = new GestisciOperazioniDrive(googleDriveService,mainActivity);
        gestioneDrive.createRestore(loadServizio);

    }

    private void signOut() {
        setProgressBar();

        mGoogleSignInClient = buildGoogleSignInClient();
        mGoogleSignInClient.signOut()
                .addOnSuccessListener(new OnSuccessListener<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.i("Google", "Not more Signed");
                        loadServizio.dismiss();
                        MainActivity.stringSnackStatic = getString(R.string.deregistrazione_google);
                        Fragment fragmentBackupRestore = BackupRestoreFragment.newInstance();
                        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.anchor_point_main,fragmentBackupRestore).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadServizio.dismiss();
                        Toast.makeText(mainActivity, "Errore nel collegamento a Google", Toast.LENGTH_LONG).show();
                        Log.d("Google", "onFailure: " + e.getMessage());
                    }
         });
    }


     private void signIn() {
        setProgressBar();

        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(DriveScopes.DRIVE))
                        .requestEmail()
                        .build();
        return GoogleSignIn.getClient(mainActivity.getApplicationContext(), signInOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    handleSignInResultForBackup(resultData);
                }else {
                    Toast.makeText(mainActivity, "Errore nel collegamento a Google", Toast.LENGTH_LONG).show();
                    loadServizio.dismiss();
                }
                break;
            default:
                Toast.makeText(mainActivity, "Errore nel collegamento a Google", Toast.LENGTH_LONG).show();
                loadServizio.dismiss();
        }

        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void handleSignInResultForBackup(Intent result) {
        fromSignIn = result;
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.i("Google", "Signed in as " + googleSignInAccount.getEmail());

                        GoogleAccountCredential credential =
                                GoogleAccountCredential.usingOAuth2(
                                        mainActivity.getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccountName(googleSignInAccount.getAccount().name);
                        com.google.api.services.drive.Drive googleDriveService =
                                new com.google.api.services.drive.Drive.Builder(
                                        AndroidHttp.newCompatibleTransport(),
                                        new GsonFactory(),
                                        credential)
                                        .setApplicationName("PasswordManager")
                                        .build();

                        loadServizio.dismiss();
                        MainActivity.stringSnackStatic = getString(R.string.registrazione_google);
                        Fragment fragmentBackupRestore = BackupRestoreFragment.newInstance();
                        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.anchor_point_main,fragmentBackupRestore).commit();
                        Log.i("Google", "Sign in OK");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadServizio.dismiss();
                        Toast.makeText(mainActivity, "Errore nel collegamento a Google", Toast.LENGTH_LONG).show();
                        Log.e("Google", "Unable to sign in.", e);
                    }
                });
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
