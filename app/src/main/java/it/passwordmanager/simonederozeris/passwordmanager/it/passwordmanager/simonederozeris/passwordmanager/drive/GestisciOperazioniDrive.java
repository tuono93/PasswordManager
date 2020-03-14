package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.drive;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import it.passwordmanager.simonederozeris.passwordmanager.Constant;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.PasswordManagerDatabase;

public class GestisciOperazioniDrive {

    public com.google.api.services.drive.Drive googleDriveService;
    private DriveServiceHelper driveServiceHelper;
    private Context context;


    public GestisciOperazioniDrive (com.google.api.services.drive.Drive googleDriveService, Context context){
        this.context = context;
        this.driveServiceHelper = new DriveServiceHelper(googleDriveService,context);
    }


    public void createBackup(){
        driveServiceHelper.searchFolder()
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        if(googleDriveFileHolder.getId()==null){
                            createBackupAndFileFolder();
                        } else {
                            createBackupFile(googleDriveFileHolder.getId());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Google", "onFailure: " + e.getMessage());
                    }
                });
    }


    public void createBackupFile(String idFolderBackup){
        driveServiceHelper.createFile(idFolderBackup)
                    .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                        @Override
                        public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                            Gson gson = new Gson();
                            Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        }
                     })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Google", "onFailure: " + e.getMessage());
                        }
                    });
    }

    public void createBackupAndFileFolder(){
        driveServiceHelper.createFolder()
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        driveServiceHelper.createFile(googleDriveFileHolder.getId())
                                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                            @Override
                            public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                                Gson gson = new Gson();
                                Log.d("Google", "onSuccess: " + gson.toJson(googleDriveFileHolder));
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Google", "onFailure: " + e.getMessage());
                                    }
                                });;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Google", "onFailure: " + e.getMessage());
                    }
                });
    }

    public void createRestore(){
        driveServiceHelper.getFile()
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
                                            File directory = context.getFilesDir(); //or getExternalFilesDir(null); for external storage
                                            File fileBackup = new File(directory, "account_db");
                                            FileUtils.writeByteArrayToFile(fileBackup, byteFile);
                                            PasswordManagerDatabase.restoreDatabase(context,fileBackup);
                                        } catch (Exception e){
                                            Log.d("Google", "Errore nel restore: " + e.getMessage());
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Google", "onFailure: " + e.getMessage());
                                    }
                                });;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Google", "onFailure: " + e.getMessage());
                    }
                });
    }
}
