package it.passwordmanager.simonederozeris.passwordmanager;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ViewUtils;
import androidx.recyclerview.widget.RecyclerView;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;

public class AccountViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageAccount;
    private TextView textAccount;

    public AccountViewHolder(View itemView){
        super(itemView);
        imageAccount = itemView.findViewById(R.id.imageItem);
        textAccount = itemView.findViewById(R.id.textItem);
    }

    public void bind (Account account){
        textAccount.setText(account.getNome().trim());
        int id = itemView.getResources().getIdentifier(account.getNome().trim().toLowerCase(), "drawable", "it.passwordmanager.simonederozeris.passwordmanager");
        if(id!=0) {
            imageAccount.setImageResource(id);
        }
    }
}