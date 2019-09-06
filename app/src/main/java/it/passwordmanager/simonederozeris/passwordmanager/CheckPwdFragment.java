package it.passwordmanager.simonederozeris.passwordmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.GestioneFlussoApp;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.Flusso;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.FlussoCheckPwd;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.FlussoModificaPwd;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.TipoStatoPwd;

import static it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.TipoCheckPwd.CHECK;
import static it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.TipoNuovaPwd.CONFERMA;
import static it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.TipoNuovaPwd.NUOVA;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckPwdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckPwdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckPwdFragment extends Fragment {
    private static final String ARG_NUM_FLUSSO = "num_flusso";
    private static final String ARG_STEP_CORRENTE= "step_corrente";
    private static final String ARG_STATO = "stato";
    private static final String ARG_LAYOUT_CONTAINER = "layout_container";
    private static final String ARG_VALUE = "value";
    public EditText editTextPwd1, editTextPwd2, editTextPwd3, editTextPwd4;
    public LinearLayout linearLayoutPwd;
    public TextView textPwd;
    Animation animationPwd;
    SharedPreferences sharedPreferences;
    private Flusso flusso;
    private TipoStatoPwd stato;
    private int layout_container;

    private static int getNumFlusso(Flusso flusso){
        int numFlusso;

        if(flusso.getClass().getName().equals(FlussoModificaPwd.class.getName())){
            numFlusso = 1;
        } else {
            numFlusso = 2;
        }

        return numFlusso;
    }

    private static Flusso getFlusso(int numFlusso,int stepCorrente,String value){
        Flusso flusso = null;

        if(numFlusso==1){
            flusso = new FlussoModificaPwd(stepCorrente,value);
        } else {
            flusso = new FlussoCheckPwd(stepCorrente,value);
        }

        return flusso;
    }

    private OnFragmentInteractionListener mListener;

    public static CheckPwdFragment newInstance(Flusso flusso, TipoStatoPwd stato,int layout_container) {
        CheckPwdFragment fragment = new CheckPwdFragment();
        int numFlusso = getNumFlusso(flusso);
        Bundle args = new Bundle();
        args.putString(ARG_NUM_FLUSSO, ""+numFlusso);
        args.putString(ARG_STEP_CORRENTE, ""+flusso.getCurrentStep());
        args.putString(ARG_STATO, stato.getTipoStato());
        args.putString(ARG_LAYOUT_CONTAINER,"" + layout_container);
        args.putString(ARG_VALUE,"" + flusso.getValue());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flusso = getFlusso(Integer.parseInt(getArguments().getString(ARG_NUM_FLUSSO)),Integer.parseInt(getArguments().getString(ARG_STEP_CORRENTE)),getArguments().getString(ARG_VALUE));
            stato = TipoStatoPwd.getEnumStatoGestione(getArguments().getString(ARG_STATO));
            layout_container = Integer.parseInt(getArguments().getString(ARG_LAYOUT_CONTAINER));
        }
        sharedPreferences = getActivity().getSharedPreferences(Constant.PASSCODE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_pwd, container, false);
        editTextPwd1 = (EditText) rootView.findViewById(R.id.editText1);
        editTextPwd2 = (EditText) rootView.findViewById(R.id.editText2);
        editTextPwd3 = (EditText) rootView.findViewById(R.id.editText3);
        editTextPwd4 = (EditText) rootView.findViewById(R.id.editText4);
        textPwd = (TextView) rootView.findViewById(R.id.textPwd);
        linearLayoutPwd = (LinearLayout) rootView.findViewById(R.id.linearLayoutPwd);

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(flusso.getClass().getName() == FlussoModificaPwd.class.getName()){
            textPwd.setText(getResources().getString(R.string.insNewPassw));
        }
        setEditText();
        if(stato == TipoStatoPwd.ERR){
            textPwd.setText(getResources().getString(R.string.insPasswErr));
            textPwd.setTextColor(getResources().getColor(R.color.customRed));
            editTextPwd1.setBackground(getResources().getDrawable(R.drawable.edit_text_password_errata));
            editTextPwd2.setBackground(getResources().getDrawable(R.drawable.edit_text_password_errata));
            editTextPwd3.setBackground(getResources().getDrawable(R.drawable.edit_text_password_errata));
            editTextPwd4.setBackground(getResources().getDrawable(R.drawable.edit_text_password_errata));

            editTextPwd1.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    if (flusso.getCurrentStep() == 1) {
                        textPwd.setText(getResources().getString(R.string.insPassw));
                    }else {
                        textPwd.setText(getResources().getString(R.string.insConfNewPassw));
                    }
                    textPwd.setTextColor(getResources().getColor(R.color.White));
                    editTextPwd1.setBackground(getResources().getDrawable(R.drawable.edit_text_password));
                    editTextPwd2.setBackground(getResources().getDrawable(R.drawable.edit_text_password));
                    editTextPwd3.setBackground(getResources().getDrawable(R.drawable.edit_text_password));
                    editTextPwd4.setBackground(getResources().getDrawable(R.drawable.edit_text_password));
                    return false;
                }
            });
            stato = TipoStatoPwd.OK;
        }
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

    public void setEditText() {
        editTextPwd1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode != KeyEvent.KEYCODE_DEL) {
                    editTextPwd2.requestFocus();
                    editTextPwd2.setFocusableInTouchMode(true);
                    editTextPwd1.setFocusableInTouchMode(false);
                } else {
                }
                return false;
            }
        });

        editTextPwd2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode != KeyEvent.KEYCODE_DEL) {
                    editTextPwd3.requestFocus();
                    editTextPwd3.setFocusableInTouchMode(true);
                    editTextPwd2.setFocusableInTouchMode(false);
                } else {
                    editTextPwd1.setText("");
                    editTextPwd1.requestFocus();
                    editTextPwd2.setFocusableInTouchMode(false);
                    editTextPwd1.setFocusableInTouchMode(true);
                }

                return false;
            }
        });

        editTextPwd3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode != KeyEvent.KEYCODE_DEL) {
                    editTextPwd4.requestFocus();
                    editTextPwd4.setFocusableInTouchMode(true);
                    editTextPwd3.setFocusableInTouchMode(false);
                } else {
                    editTextPwd2.setText("");
                    editTextPwd2.requestFocus();
                    editTextPwd3.setFocusableInTouchMode(false);
                    editTextPwd2.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        editTextPwd4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return false;

                if (keyCode != KeyEvent.KEYCODE_DEL) {
                    //start controllo pwd
                    char unicodeChar = (char) event.getUnicodeChar();
                    String p1 = editTextPwd1.getText().toString();
                    String p2 = editTextPwd2.getText().toString();
                    String p3 = editTextPwd3.getText().toString();
                    String passcode = p1 + p2 + p3 + unicodeChar;

                    gestioneFlusso(passcode);

                } else {
                    editTextPwd3.setText("");
                    editTextPwd3.requestFocus();
                    editTextPwd4.setFocusableInTouchMode(false);
                    editTextPwd3.setFocusableInTouchMode(true);
                }
                return false;
            }
        });
    }

    public void gestioneFlusso(String passcode){
        if(flusso.getClass().getName().equals(FlussoModificaPwd.class.getName())){
            switch (((FlussoModificaPwd) flusso).getTipoNuovaPwd()){
                case NUOVA:
                    Log.i("nuova_passcode",passcode);
                    flusso.setValue(passcode);
                    flusso.goNextStep(NUOVA.getStep());
                    confermaPassword();
                    break;
                case CONFERMA:
                    Log.i("precedente_passcode", flusso.getValue());
                    if (passcode.equalsIgnoreCase(flusso.getValue())) {
                        flusso.goNextStep(CONFERMA.getStep());
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        //Find the currently focused view, so we can grab the correct window token from it.
                        View view = getActivity().getCurrentFocus();
                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                        if (view == null) {
                            view = new View(getActivity());
                        }
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constant.PASSCODE_VALUE,passcode);
                        editor.commit();

                       // Toast.makeText(getActivity(),"Nuovo passcode salvato",Toast.LENGTH_LONG).show();
                        passwordEsatta(true);
                    } else {
                        passwordErrata();
                    }
                    break;
                case FINISH:
                    break;
            }
        } else {
            switch (((FlussoCheckPwd) flusso).getTipoCheckPwd()){
                case CHECK:
                    String passcodeSalvato = sharedPreferences.getString(Constant.PASSCODE_VALUE,null);

                    Log.i("salvato_passcode",passcodeSalvato);
                    Log.i("check_passcode",passcode);

                    if (passcodeSalvato == null){
                        Toast.makeText(getActivity(),"Errore salvataggio passcode. Reinseriscilo in Cambia Password dal menu",Toast.LENGTH_LONG).show();
                        passwordEsatta(false);
                    } else {
                        if (passcode.equalsIgnoreCase(passcodeSalvato)) {
                            flusso.goNextStep(CHECK.getStep());
                            passwordEsatta(false);
                        } else {
                            passwordErrata();
                        }
                    }

                    break;
            }
        }
    }

    public void confermaPassword() {

        editTextPwd1.requestFocus();
        editTextPwd1.setFocusableInTouchMode(true);
        editTextPwd4.setFocusableInTouchMode(false);

        animationPwd = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_animation_exit);
        animationPwd.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                editTextPwd1.setText("");
                editTextPwd2.setText("");
                editTextPwd3.setText("");
                editTextPwd4.setText("");
                animationPwd = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_animation_enter);
                animationPwd.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        textPwd.setText(getResources().getString(R.string.insConfNewPassw));
                        editTextPwd1.setText("");
                        editTextPwd2.setText("");
                        editTextPwd3.setText("");
                        editTextPwd4.setText("");
                        editTextPwd1.requestFocus();
                        editTextPwd1.setFocusableInTouchMode(true);
                        editTextPwd4.setFocusableInTouchMode(false);
                    }
                });
                linearLayoutPwd.startAnimation(animationPwd);
            }
        });
        linearLayoutPwd.startAnimation(animationPwd);
    }

    public void passwordErrata() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        CheckPwdFragment fragmentCheckPwd = CheckPwdFragment.newInstance(flusso,TipoStatoPwd.ERR,layout_container);
        getActivity().getSupportFragmentManager().beginTransaction().replace(layout_container,fragmentCheckPwd).commit();
    }

    public void passwordEsatta(boolean viewSnack) {
        Intent toMain = new Intent(getActivity(), MainActivity.class);
        if(viewSnack) {
            toMain.putExtra("view_snack", getString(R.string.newPasscode));
        }
        GestioneFlussoApp.flussoRegolare = true;
        startActivity(toMain);
        getActivity().finish();
    }
}
