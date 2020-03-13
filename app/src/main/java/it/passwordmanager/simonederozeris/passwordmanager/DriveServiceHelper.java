package it.passwordmanager.simonederozeris.passwordmanager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
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
    private String name = "Prova Drive";
    private String content = "Hello World";
    private ContentResolver contentResolver;
    private Uri uri;
    private String parentId = "1qV_mrjwQx1kRb0fW9wfH_fyoUcXMVtqQ";

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */

    public Task<GoogleDriveFileHolder> createTextFile() {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {
                GoogleDriveFileHolder googleDriveFileHolder = null;
                try {
                    File metadata = new File()
                            .setParents(Arrays.asList(new ParentReference().setId(parentId)))
                            .setMimeType("text/plain")
                            .setLastModifyingUserName(name);
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
}