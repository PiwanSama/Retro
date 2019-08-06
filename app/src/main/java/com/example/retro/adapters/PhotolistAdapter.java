package com.example.retro.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.retro.R;
import com.example.retro.models.RetroPhoto;
import com.example.retro.utils.NetworkState;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class PhotolistAdapter extends PagedListAdapter <RetroPhoto, RecyclerView.ViewHolder> {

    private Context context;
    private NetworkState networkState;

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;

    public PhotolistAdapter(Context context){
        super(RetroPhoto.DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_ITEM){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View mView = layoutInflater.inflate(R.layout.custom_row, parent, false);
            return new MovieViewHolder(mView);
        }
        else if(viewType==VIEW_TYPE_LOADING){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View lView = layoutInflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(lView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MovieViewHolder){
            populateItemRows((MovieViewHolder)viewHolder, position);
        }
        else if (viewHolder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder)viewHolder, position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() -1){
            return VIEW_TYPE_LOADING;
        }
        else {
            return VIEW_TYPE_ITEM;
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        TextView txtTitle;
        private ImageView coverImage;

        MovieViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{

        public final View lView;
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.lView = itemView;

            progressBar = lView.findViewById(R.id.progressBar);
        }
    }

    private boolean hasExtraRow(){
        if (networkState!=null && networkState != NetworkState.LOADED){
            return true;
        }
        else {
            return false;
        }
    }

    public void setNetworkState (NetworkState newNetworkState){
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow!=newExtraRow){
            if (previousExtraRow){
                notifyItemRemoved(getItemCount());
            }
            else {
                notifyItemInserted(getItemCount());
            }
        }
        else if (newExtraRow && previousState != newNetworkState){
            notifyItemChanged(getItemCount() -1);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position){

    }

    private void populateItemRows(MovieViewHolder viewHolder, int position){
        viewHolder.txtTitle.setText(getItem(position).getTitle());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(getItem(position).getThumbnailUrl())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(viewHolder.coverImage);
    }
}
