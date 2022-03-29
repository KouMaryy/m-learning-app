package com.unipi.p17053.ahelpp.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.unipi.p17053.ahelpp.Activities.ElementTestQuestionsActivity;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.R;
import com.unipi.p17053.ahelpp.Activities.TestQuestionsActivity;

import static com.unipi.p17053.ahelpp.DatabasePannel.answered;
import static com.unipi.p17053.ahelpp.DatabasePannel.notAnswered;
import static com.unipi.p17053.ahelpp.DatabasePannel.notVisited;

public class QuestionInfoGridAdapter extends BaseAdapter {

    private int numOfTestQ;
    private Context context;

    public QuestionInfoGridAdapter(Context context,int numOfTestQ) {
        this.numOfTestQ = numOfTestQ;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numOfTestQ;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View myView;

        if(convertView==null)
        {
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_info_grid_item,parent,false);
        }
        else
        {
           myView = convertView ;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof TestQuestionsActivity) {
                    ((TestQuestionsActivity)context).goToQuestion(position);
                }
                else if(context instanceof ElementTestQuestionsActivity) {
                    ((ElementTestQuestionsActivity)context).goToQuestion(position);
                }

            }
        });

        TextView infoQnum = myView.findViewById(R.id.infoQnum);
        infoQnum.setText(String.valueOf(position+1));

        switch (DatabasePannel.globalQuestionList.get(position).getStatus())
        {
            case notVisited :
                infoQnum.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.notVisited)));
                break;
            case notAnswered :
                infoQnum.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.notAnswered)));
                break;
            case answered :
                infoQnum.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.answered)));
                break;
            default:
                break;
        }

        return myView;
    }
}
