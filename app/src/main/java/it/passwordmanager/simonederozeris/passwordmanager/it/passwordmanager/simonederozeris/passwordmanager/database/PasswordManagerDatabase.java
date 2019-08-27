package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
}
