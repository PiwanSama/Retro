package com.example.retro.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.retro.R;
import com.example.retro.models.RetroPhoto;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RetroPhoto> dataList;
    private Context context;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public CustomAdapter(Context context,List<RetroPhoto> dataList){
        this.context = context;
        this.dataList = dataList;
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

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return dataList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position){

    }

    private void populateItemRows(MovieViewHolder viewHolder, int position){
        viewHolder.txtTitle.setText(dataList.get(position).getTitle());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(dataList.get(position).getThumbnailUrl())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(viewHolder.coverImage);
    }
}
