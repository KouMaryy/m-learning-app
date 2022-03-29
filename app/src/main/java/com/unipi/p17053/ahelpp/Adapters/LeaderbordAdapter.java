package com.unipi.p17053.ahelpp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.p17053.ahelpp.Models.LeaderboardModel;
import com.unipi.p17053.ahelpp.R;

import java.util.List;


public class LeaderbordAdapter extends RecyclerView.Adapter<LeaderbordAdapter.ViewHolder> {

    private List<LeaderboardModel> userList;

    public LeaderbordAdapter(List<LeaderboardModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public LeaderbordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderbordAdapter.ViewHolder holder, int position) {

        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();

        holder.setData(name,score,rank);

    }

    @Override
    public int getItemCount() {
        if (userList.size()>10)
        {
            return 10;
        }
        else
        {
            return userList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameLeader,rankLeader,scoreLeader,capitalLeader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scoreLeader = itemView.findViewById(R.id.scoreLeader);
            rankLeader = itemView.findViewById(R.id.rankLeader);
            nameLeader = itemView.findViewById(R.id.nameLeader);
            capitalLeader = itemView.findViewById(R.id.capitalLeader);
        }

        private void setData(String name, int score,int rank)
        {
            nameLeader.setText(name);
            scoreLeader.setText("Score : " + score);
            rankLeader.setText(String.valueOf(rank));
            capitalLeader.setText(name.toUpperCase().substring(0,1));
        }
    }
}
