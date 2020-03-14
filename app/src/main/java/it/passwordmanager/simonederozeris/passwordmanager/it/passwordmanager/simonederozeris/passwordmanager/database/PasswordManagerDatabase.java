package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.api.client.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

@Database(entities ={Account.class}, version = 1, exportSchema = true)
public abstract class PasswordManagerDatabase extends RoomDatabase {
    public abstract AccountDAO getAccountDAO();

    private static PasswordManagerDatabase INSTANCE;

    public static PasswordManagerDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), PasswordManagerDatabase.class)
                            .build();
        }
        return INSTANCE;
    }

    public static PasswordManagerDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), PasswordManagerDatabase.class, "account_db")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static File createBackup(Context ctx,String titleFile) {
        return new File(ctx.getDatabasePath(titleFile).getAbsolutePath());
    }

    public static void restoreDatabase(Context ctx, File backupFile,String titleFile) {
        try {
            File currentDB = new File(ctx.getDatabasePath(titleFile).getAbsolutePath());
            if (currentDB.exists()) {

                FileChannel src = new FileInputStream(backupFile).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
            Log.d("Error Restore",  e.getMessage());
        }
    }
}
