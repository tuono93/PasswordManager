package it.passwordmanager.simonederozeris.passwordmanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;
import java.util.List;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;

public class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder> implements AccountViewHolder.OnItemClickListener{

    private List<Account> mModel;
    private Fragment frag;

    private WeakReference<onAccountListener> onAccountListenerRef;

    public interface onAccountListener{
        void onAccountClicked(Account account, int position,View v);
        void onAccountLongClicked(Account account, int position,View v);
    }

    public void setOnAccountClickListener(final onAccountListener onAccountListener){
        this.onAccountListenerRef = new WeakReference<onAccountListener>(onAccountListener);
    }

    public AccountAdapter(final List<Account> model, Fragment frag) {
        this.mModel = model;
        this.frag = frag;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new AccountViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.bind(mModel.get(position));
        if(mModel.get(position).isSelected()) {
            holder.getItemView().setBackgroundColor(frag.getResources().getColor(R.color.darkLightGrey));
        } else {
            holder.getItemView().setBackground(frag.getResources().getDrawable(R.drawable.ripple));
        }
        holder.setOnItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    @Override
    public void onItemClicked(int position,View v) {
        onAccountListener listener;
        if(onAccountListenerRef != null && (listener = onAccountListenerRef.get()) != null){
            listener.onAccountClicked(mModel.get(position),position,v);
        }
    }

    @Override
    public void onItemLongClicked(int position,View v) {
        onAccountListener listener;
        if(onAccountListenerRef != null && (listener = onAccountListenerRef.get()) != null){
            listener.onAccountLongClicked(mModel.get(position),position,v);
        }
    }
}
