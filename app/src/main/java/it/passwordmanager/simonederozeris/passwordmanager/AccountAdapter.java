package it.passwordmanager.simonederozeris.passwordmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;
import java.util.List;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.database.Account;

public class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder> implements AccountViewHolder.OnItemClickListener {

    private List<Account> mModel;

    private WeakReference<onAccountListener> onAccountListenerRef;

    public interface onAccountListener{
        void onAccountClicked(Account account, int position);
    }

    public void setOnAccountClickListener(final onAccountListener onAccountListener){
        this.onAccountListenerRef = new WeakReference<onAccountListener>(onAccountListener);
    }

    public AccountAdapter(final List<Account> model) {
        this.mModel = model;
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
        holder.setOnItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    @Override
    public void onItemClicked(int position) {
        onAccountListener listener;
        if(onAccountListenerRef != null && (listener = onAccountListenerRef.get()) != null){
            listener.onAccountClicked(mModel.get(position),position);
        }
    }
}
