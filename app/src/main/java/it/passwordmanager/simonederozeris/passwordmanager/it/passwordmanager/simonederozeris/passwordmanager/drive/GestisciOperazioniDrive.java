package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.drive;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

public class GestisciOperazioniDrive {

    public com.google.api.services.drive.Drive googleDriveService;
    private DriveServiceHelper driveServiceHelper;


    public GestisciOperazioniDrive (com.google.api.services.drive.Drive googleDriveService){
        this.driveServiceHelper = new DriveServiceHelper(googleDriveService);
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
                        driveServiceHelper.createFile(googleDriveFileHolder.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Google", "onFailure: " + e.getMessage());
                    }
                });
    }

    public void searchFile(){
        DriveServiceHelper driveServiceHelper = new DriveServiceHelper(googleDriveService);
        driveServiceHelper.searchFile()
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson =  new Gson();
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
}
