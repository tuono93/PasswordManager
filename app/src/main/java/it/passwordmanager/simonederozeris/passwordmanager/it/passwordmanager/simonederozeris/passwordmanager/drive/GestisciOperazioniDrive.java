package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.drive;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.util.Date;

import it.passwordmanager.simonederozeris.passwordmanager.MainActivity;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.PasswordManagerDatabase;

public class GestisciOperazioniDrive {

    public com.google.api.services.drive.Drive googleDriveService;
    private DriveServiceHelper driveServiceHelper;
    private MainActivity mainActivity;
    private String idFolderBackup;
    private String titleFile;
    private ProgressDialog loadServizio;


    public GestisciOperazioniDrive (com.google.api.services.drive.Drive googleDriveService, MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.driveServiceHelper = new DriveServiceHelper(googleDriveService,mainActivity);
    }


    public void createBackup(ProgressDialog loadServizioParam){
        this.loadServizio = loadServizioParam;
        driveServiceHelper.searchFolder("PasswordManager")
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        if(googleDriveFileHolder!= null) {
                            if (googleDriveFileHolder.getId() == null) {
                                createRootBackupFolder();
                            } else {
                                createBackupSubFolder(googleDriveFileHolder.getId());
                            }
                        } else {
                            loadServizio.dismiss();
                            Toast.makeText(mainActivity, "Errore nel collegamento a Google", Toast.LENGTH_LONG).show();
                        }
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


    public void createRootBackupFolder(){
        driveServiceHelper.createFolder("PasswordManager","root")
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        createBackupSubFolder(googleDriveFileHolder.getId());
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

    public void createBackupSubFolder(String parentFolder){
        this.idFolderBackup = parentFolder;
        Date date = new Date();
        long timeMilli = date.getTime();
        String newFolder = "" + timeMilli;
        driveServiceHelper.createFolder(newFolder,idFolderBackup)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        createBackupFile(googleDriveFileHolder.getId(),"account_db");
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


    public void createBackupFile(String idFolderBackupParam,String titleFileParam){
        this.titleFile = titleFileParam;
        this.idFolderBackup = idFolderBackupParam;
        driveServiceHelper.createFile(idFolderBackup,titleFile)
                    .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                        @Override
                        public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                            Gson gson = new Gson();
                            Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                            if(titleFile.equals("account_db")){
                                createBackupFile(idFolderBackup,"account_db-shm");
                            } else if(titleFile.equals("account_db-shm")){
                                createBackupFile(idFolderBackup,"account_db-wal");
                            } else {
                                View parentLayout = mainActivity.findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Backup effettuato con successo", Snackbar.LENGTH_SHORT).show();
                                loadServizio.dismiss();
                            }
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


    public void createRestore(ProgressDialog loadServizioParam){
        this.loadServizio = loadServizioParam;
        createRestore("account_db");
    }

    public void createRestore(String titleFileParam){
        this.titleFile = titleFileParam;
        driveServiceHelper.getFile(titleFile)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson =  new Gson();
                        Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        driveServiceHelper.getStream()
                                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                                    @Override
                                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                                        Gson gson = new Gson();
                                        Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                                        try {
                                            byte[] byteFile = googleDriveFileHolder.getStream().toByteArray();
                                            File directory = mainActivity.getFilesDir(); //or getExternalFilesDir(null); for external storage
                                            File fileBackup = new File(directory, titleFile);
                                            FileUtils.writeByteArrayToFile(fileBackup, byteFile);
                                            PasswordManagerDatabase.restoreDatabase(mainActivity,fileBackup,titleFile);
                                            if(titleFile.equals("account_db")){
                                                createRestore("account_db-shm");
                                            } else if(titleFile.equals("account_db-shm")){
                                                createRestore("account_db-wal");
                                            } else {
                                                View parentLayout = mainActivity.findViewById(android.R.id.content);
                                                Snackbar.make(parentLayout, "Restore effettuato con successo", Snackbar.LENGTH_SHORT).show();
                                                loadServizio.dismiss();
                                            }
                                        } catch (Exception e){
                                            loadServizio.dismiss();
                                            Toast.makeText(mainActivity, "Errore nel collegamento a Google", Toast.LENGTH_LONG).show();
                                            Log.d("Google", "Errore nel restore: " + e.getMessage());
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadServizio.dismiss();
                                        Toast.makeText(mainActivity, "Errore nel collegamento a Google", Toast.LENGTH_LONG).show();
                                        Log.d("Google", "onFailure: " + e.getMessage());
                                    }
                                });;
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
}
