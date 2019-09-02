package it.passwordmanager.simonederozeris.passwordmanager;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ViewUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;

public class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ImageView imageAccount;
    private TextView textAccount;
    private WeakReference<OnItemClickListener> onItemClickListenerRef;

    public interface OnItemClickListener{
        void onItemClicked(int position);
    }

    public AccountViewHolder(View itemView){
        super(itemView);
        imageAccount = itemView.findViewById(R.id.imageItem);
        textAccount = itemView.findViewById(R.id.textItem);
        itemView.setOnClickListener(this);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener){
        this.onItemClickListenerRef = new WeakReference<OnItemClickListener>(onItemClickListener);
    }

    public void bind (Account account){
        int idKey = itemView.getResources().getIdentifier("account_key", "drawable", "it.passwordmanager.simonederozeris.passwordmanager");
        imageAccount.setImageResource(idKey);
        textAccount.setText(account.getNome().trim());
        int id = itemView.getResources().getIdentifier(account.getNome().trim().toLowerCase(), "drawable", "it.passwordmanager.simonederozeris.passwordmanager");
        if(id!=0) {
            imageAccount.setImageResource(id);
        }
    }

    @Override
    public void onClick(View v){
        OnItemClickListener listener;
        if(onItemClickListenerRef != null && (listener = onItemClickListenerRef.get()) != null){
            listener.onItemClicked(getLayoutPosition());
        }
    }
}