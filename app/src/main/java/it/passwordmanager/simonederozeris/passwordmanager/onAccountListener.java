package it.passwordmanager.simonederozeris.passwordmanager;

import android.view.View;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;

public interface onAccountListener {
    public void onAccountClicked(Account account, int position,View v);
    public void onAccountLongClicked(Account account, int position, View v);
}
