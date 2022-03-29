package com.unipi.p17053.ahelpp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.Distribution;
import com.unipi.p17053.ahelpp.Activities.ChallengesActivity;
import com.unipi.p17053.ahelpp.Activities.ViewTheoryActivity;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.Models.ChallengesModel;
import com.unipi.p17053.ahelpp.R;
import com.unipi.p17053.ahelpp.Activities.StartTestActivity;

import java.util.List;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder>{

    private List<ChallengesModel> testList;
    String activityToTheTop = "ToTheTopActivity";
    String activityChallenges = "Challenges";
    String clickedFromActivity;
    Context clickedFrom;

    public ChallengesAdapter(List<ChallengesModel> testList) {
        this.testList = testList;
    }


    @NonNull
    @Override
    public ChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);
        clickedFrom = view.getContext();
        clickedFromActivity = clickedFrom.toString();
        return new ChallengesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ChallengesAdapter.ViewHolder holder, int position) {

            int progress = testList.get(position).getTopScore();
            String testID = testList.get(position).getTestId();
            holder.setData(position,testID,progress);
    }



    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView test_title, test_topScore;
        private ProgressBar test_progressBar;
        private LinearLayout challengeItemLinear;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            test_title = itemView.findViewById(R.id.test_title);
            test_topScore = itemView.findViewById(R.id.test_topScore);
            test_progressBar = itemView.findViewById(R.id.test_progress_bar);
            challengeItemLinear = itemView.findViewById(R.id.challengeItemLinear);


        }

        private void  setData(final int pos,String testID,int progress)
        {
            if ( clickedFromActivity.toLowerCase().contains(activityChallenges.toLowerCase()))
            {
                test_title.setText("Κεφάλαιο "+ String.valueOf(DatabasePannel.selected_less_index+1) + " - Challenge "+String.valueOf(pos+1));
                challengeItemLinear.setBackgroundResource(R.drawable.gradient_effect);


            }

            else  if ( clickedFromActivity.toLowerCase().contains(activityToTheTop.toLowerCase()))
            {

                int challenge =  Integer.parseInt(testID.substring(testID.length()-1));
                int from_lesson = Integer.parseInt(testID.substring(6,7));
                test_title.setText("Κεφάλαιο "+ String.valueOf(from_lesson) + " - Challenge "+String.valueOf(challenge));
                if(progress<100)
                {

                    challengeItemLinear.setBackgroundResource(R.drawable.gradient_effect_light);
                }
                else
                {

                    challengeItemLinear.setBackgroundResource(R.drawable.perfected_background);
                }


            }
            test_topScore.setText(String.valueOf(progress)+" %");
            test_progressBar.setProgress(progress);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String label = (String) test_title.getText();
                    int posHelper_Challenge = Integer.parseInt(label.substring(label.length()-1));

                    if ( clickedFromActivity.toLowerCase().contains(activityToTheTop.toLowerCase()))
                    {

                        int posHelper_lesson = Integer.parseInt(label.substring(9,10));


                        String id = "lesson"+posHelper_lesson+"test"+posHelper_Challenge;
                        int myPosCh = 0;

                        for (int i=0;i<DatabasePannel.globalDoBetterTestList.size();i++)
                        {
                            if (DatabasePannel.globalDoBetterTestList.get(i).getTestId().equals(id) )
                            {
                                Log.d("We are in Do Better : The Challenge-to-take  ID is EQUAL TO : ", DatabasePannel.globalDoBetterTestList.get(i).getTestId() +" and it's in position "+i);
                                myPosCh  = i ;
                                break;
                            }
                        }

                        DatabasePannel.selected_less_index =posHelper_lesson-1 ;
                        DatabasePannel.selected_test_index = myPosCh ;
                        DatabasePannel.clickedFrom ="ToTheTop";
                        Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                        itemView.getContext().startActivity(intent);

                    }

                    else
                    {

                        DatabasePannel.selected_test_index = posHelper_Challenge-1 ;
                        DatabasePannel.clickedFrom ="Challenges";
                        Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                        itemView.getContext().startActivity(intent);
                    }


                }
            });

        }
    }







}
