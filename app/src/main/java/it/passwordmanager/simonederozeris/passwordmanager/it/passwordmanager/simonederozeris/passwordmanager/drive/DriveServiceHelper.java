package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.drive;

import android.util.Log;

import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    private String fileId;
    private String nameFileNewBackup = "Backup_password_manager_XzRtghu23" ;
    private String nameFolderNewBackup = "PasswordManager" ;
    //TODO: DA INSERIRE IL FILE BACKUP DEL DATABASE
    private String content = "Hello World";
    private String folderBackupId = "";
    private static DriveServiceHelper single_instance = null;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }


    public Task<GoogleDriveFileHolder> createFolder() {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {
                GoogleDriveFileHolder googleDriveFileHolder = null;

                try {
                    googleDriveFileHolder = new GoogleDriveFileHolder();
                    File metadata = new File()
                            .setParents(Arrays.asList(new ParentReference().setId("root")))
                            .setMimeType(DriveFolder.MIME_TYPE)
                            .setTitle(nameFolderNewBackup);

                    File googleFile = mDriveService.files().insert(metadata).execute();
                    if (googleFile == null) {
                        throw new IOException("Null result when requesting file creation.");
                    }
                    googleDriveFileHolder.setId(googleFile.getId());
                    return googleDriveFileHolder;
                } catch (Exception e) {
                    Log.d("Google", "onFailureInner: " + e.getMessage());
                    return googleDriveFileHolder;
                }
            }
        });
     }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */

    public Task<GoogleDriveFileHolder> createFile(String folderBackupIdParam) {
        this.folderBackupId = folderBackupIdParam;
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {
                GoogleDriveFileHolder googleDriveFileHolder = null;
                try {
                    File metadata = new File()
                            .setParents(Arrays.asList(new ParentReference().setId(folderBackupId)))
                            .setMimeType("text/plain")
                            .setTitle(nameFileNewBackup);
                    ByteArrayContent contentStream = ByteArrayContent.fromString("text/plain", content);

                    File googleFile = mDriveService.files().insert(metadata, contentStream).execute();

                    if (googleFile == null) {
                        throw new IOException("Null result when requesting file creation.");
                    }
                    googleDriveFileHolder = new GoogleDriveFileHolder();
                    googleDriveFileHolder.setId(googleFile.getId());
                    return googleDriveFileHolder;
                } catch (Exception e){
                    Log.d("Google", "onFailureInner: " + e.getMessage());
                    return googleDriveFileHolder;
                }
            }
        });
    }

    public Task<GoogleDriveFileHolder> searchFolder() {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {
                GoogleDriveFileHolder googleDriveFileHolder = null;
                try {
                    // Retrive the metadata as a File object.
                    FileList result = mDriveService.files().list()
                            .setQ("mimeType = '" + DriveFolder.MIME_TYPE + "' and title = '" + nameFolderNewBackup + "' ")
                            .setSpaces("drive")
                            .execute();

                    googleDriveFileHolder = new GoogleDriveFileHolder();

                    if (result.getItems().size() > 0) {
                        googleDriveFileHolder.setId(result.getItems().get(0).getId());
                        googleDriveFileHolder.setName(result.getItems().get(0).getTitle());
                    }
                    return googleDriveFileHolder;
                } catch (Exception e) {
                    Log.d("Google", "onFailureInner: " + e.getMessage());
                    return googleDriveFileHolder;
                }
            }
        });
    }

    public Task<GoogleDriveFileHolder> searchFile() {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {

                FileList result = mDriveService.files().list()
                        .setQ("title = '" + nameFileNewBackup + "' and mimeType ='" + "text/plain" + "'")
                        .setSpaces("drive")
                        .setOrderBy("createdDate desc")
                        .execute();
                GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
                if (result.getItems().size() > 0) {
                    googleDriveFileHolder.setId(result.getItems().get(0).getId());
                }

                return googleDriveFileHolder;
            }
        });
    }
}