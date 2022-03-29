package com.unipi.p17053.ahelpp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.p17053.ahelpp.Activities.Index;
import com.unipi.p17053.ahelpp.Activities.StartElementTestActivity;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.Models.IndexElementModel;
import com.unipi.p17053.ahelpp.R;

import java.util.List;

public class IndexElementAdapter extends RecyclerView.Adapter<IndexElementAdapter.Viewholder> {

    private List<IndexElementModel> indexElementsList;
    public IndexElementAdapter(List<IndexElementModel> indexElementsList) {
        this. indexElementsList =  indexElementsList;
    }

    @NonNull
    @Override
    public IndexElementAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.indexelement_item_layout,parent,false);
        return new IndexElementAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  IndexElementAdapter.Viewholder holder, int position) {
        String recDiscl = indexElementsList.get(position).getRecommendationDiscl();
        String redirect = indexElementsList.get(position).getRedirectTo();
        holder.setData(position,recDiscl,redirect);
    }

    @Override
    public int getItemCount() {
        return  indexElementsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView recommendationDisc;
        ImageView elementImage;


        public Viewholder(@NonNull  View itemView)
        {
            super(itemView);

            //Initialize Views
            recommendationDisc = itemView.findViewById(R.id.recommendationDisc);
            elementImage = itemView.findViewById(R.id.elementImage);
        }

        private void  setData(final int pos,String recDiscl,String redirect)
        {
            recommendationDisc.setText(recDiscl);
            elementImage.setImageResource(R.drawable.ic_bookmark_filled_black);
            if(redirect.equals("Challenge Αδυναμιών"))
            {
                elementImage.setImageResource(R.drawable.ic_heal_black);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabasePannel.selected_element_index = pos;
                    DatabasePannel.clickedFrom ="Element";
                    DatabasePannel.clickedFromElement = redirect;
                    Intent intent = new Intent(itemView.getContext(), StartElementTestActivity.class);
                    itemView.getContext().startActivity(intent);

                }
            });

        }
    }
}
