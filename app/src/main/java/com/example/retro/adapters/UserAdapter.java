package com.example.retro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retro.R;
import com.example.retro.databinding.UserListItemBinding;
import com.example.retro.models.User;

public class UserAdapter extends PagedListAdapter<User, UserAdapter.UserViewHolder> {
    private Context context;

    public UserAdapter(Context context) {
        super(User.CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserListItemBinding userListItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        , R.layout.user_list_item, parent, false);

        return new UserViewHolder(userListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = getItem(position);
        user.setAvatar(user.getAvatar());

        holder.userListItemBinding.setUser(user);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private UserListItemBinding userListItemBinding;

        public UserViewHolder(@NonNull UserListItemBinding userListItemBinding) {
            super(userListItemBinding.getRoot());
            this.userListItemBinding = userListItemBinding;

            userListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        User selectedUser = getItem(position);

                    }
                }
            });
        }
    }
}