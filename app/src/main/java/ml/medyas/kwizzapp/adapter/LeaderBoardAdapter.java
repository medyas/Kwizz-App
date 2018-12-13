package ml.medyas.kwizzapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.GlideApp;
import ml.medyas.kwizzapp.classes.UserLeaderBoardClass;


public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder> {
    private final Context context;
    private List<UserLeaderBoardClass> items;


    public LeaderBoardAdapter(List<UserLeaderBoardClass> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public LeaderBoardViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.leader_board_first_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leader_board_item, parent, false);
        }

        return new LeaderBoardViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(LeaderBoardViewHolder holder, int position) {
        UserLeaderBoardClass item = items.get(position);
        GlideApp.with(context)
                .load(item.getImageUrl())
                .apply(new RequestOptions().centerCrop().circleCrop())
                .into(holder.img);
        holder.username.setText(item.getUsername());
        if(position == 0) {
            holder.score.setText(String.format("Points : %s", item.getFormattedScore()));
        } else {
            holder.score.setText(String.format("%s", item.getFormattedScore()));
        }

        holder.rank.setText(String.format("%d", position+1));

    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView username;
        TextView score;
        TextView rank;

        public LeaderBoardViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.user_img);
            username = itemView.findViewById(R.id.user_name);
            score = itemView.findViewById(R.id.user_score);
            rank = itemView.findViewById(R.id.user_rank);
        }
    }
}