package com.unipi.p17053.ahelpp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import androidx.cardview.widget.CardView;

import com.unipi.p17053.ahelpp.Activities.ChallengesActivity;
import com.unipi.p17053.ahelpp.Activities.ViewTheoryActivity;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.Models.LessonModel;
import com.unipi.p17053.ahelpp.R;

import java.util.List;

public class LessonAdapter extends BaseAdapter {
    private List<LessonModel> lessons_list;
    String activityTheory = "Theory";
    String activityTest = "Test";
    CardView myCardView;


    public LessonAdapter(List<LessonModel> lessons_list) {
        this.lessons_list = lessons_list;
    }

    @Override
    public int getCount() {
        return lessons_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View myView;
        if(convertView==null)
        {
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item_layout,parent,false);


        }
        else
        {
            myView = convertView;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Context clickedFrom = v.getContext();
                String clickedFromActivity = clickedFrom.toString();


                if ( clickedFromActivity.toLowerCase().contains(activityTest.toLowerCase()))
                {
                    DatabasePannel.selected_less_index = position;
                    Intent intent = new Intent(v.getContext(), ChallengesActivity.class);
                    v.getContext().startActivity(intent);
                }

                else  if ( clickedFromActivity.toLowerCase().contains(activityTheory.toLowerCase()))
                {
                    DatabasePannel.selected_less_index = position;
                    Intent intent = new Intent(v.getContext(), ViewTheoryActivity.class);
                    v.getContext().startActivity(intent);
                }

                else{
                    Log.d("FEEDBACK : ",clickedFromActivity );

                }

            }
        });




        Context con = myView.getContext();
        String conActivity = con.toString();

        TextView lessonName = myView.findViewById(R.id.lessonName);
        lessonName.setText(lessons_list.get(position).getLessonName());
        TextView  numOfTests = myView.findViewById(R.id.numOfTests);
        numOfTests.setText(lessons_list.get(position).getNumOfTests()+" Challenges");
        if ( conActivity.toLowerCase().contains(activityTheory.toLowerCase())) numOfTests.setText("Μελέτη");
        return myView;

    }
}
