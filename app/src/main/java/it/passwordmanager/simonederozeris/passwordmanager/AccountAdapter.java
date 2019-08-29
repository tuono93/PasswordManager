package it.passwordmanager.simonederozeris.passwordmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;

public class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder> {

    private List<Account> mModel;

    public AccountAdapter(final List<Account> model){
        this.mModel = model;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item,parent,false);
        return new AccountViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.bind(mModel.get(position));
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }
}
