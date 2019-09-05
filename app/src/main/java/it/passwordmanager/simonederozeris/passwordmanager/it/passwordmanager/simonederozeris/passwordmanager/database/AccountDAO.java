package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AccountDAO {

    @Query("SELECT * FROM account WHERE id = :id")
    public Account findById(int id);

    @Query("SELECT * FROM account WHERE nome LIKE :nome")
    public List<Account> findByName(String nome);

    @Query("SELECT * FROM account")
    public List<Account> findAllAccount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertAccount(Account account);

    @Delete
    public int deleteAccount(Account account);

    @Delete
    public int deleteAccount(List<Account> accountList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public int updateAccount (Account account);

    @Query("DELETE FROM account")
    public void clearAccount();

}
