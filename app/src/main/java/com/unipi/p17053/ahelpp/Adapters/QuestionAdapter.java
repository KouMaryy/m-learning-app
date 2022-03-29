package com.unipi.p17053.ahelpp.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.unipi.p17053.ahelpp.Models.QuestionModel;
import com.unipi.p17053.ahelpp.R;

import java.io.File;
import java.util.List;

import static com.unipi.p17053.ahelpp.DatabasePannel.answered;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalQuestionList;
import static com.unipi.p17053.ahelpp.DatabasePannel.notAnswered;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.Viewholder> {

    private StorageReference myStorageRef;

    private List<QuestionModel> questionsList;

    public QuestionAdapter(List<QuestionModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.Viewholder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView testQuestion;
        String imLoc;
        private Button optionA,optionB,optionC,currentlySelectedBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            testQuestion = itemView.findViewById(R.id.testQuestion);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            currentlySelectedBtn = null;
        }

        private  void setData(final int pos)
        {


            //Set Image for Question from firebase storage
            imLoc = questionsList.get(pos).getQuestion();
            myStorageRef = FirebaseStorage.getInstance().getReference("images/"+imLoc);
            Glide.with(itemView.getContext())
                    .load(imLoc)
                    .into(testQuestion);


            //Set Answer options
            optionA.setText(questionsList.get(pos).getOptionA());
            optionB.setText(questionsList.get(pos).getOptionB());
            if(questionsList.get(pos).getOptionC().equals(""))
            {
                optionC.setVisibility(View.GONE);
            }
            else
            {
                optionC.setVisibility(View.VISIBLE);
                optionC.setText(questionsList.get(pos).getOptionC());
            }


            initializeOption(optionA,1,pos);
            initializeOption(optionB,2,pos);
            initializeOption(optionC,3,pos);


            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectOption(optionA,1,pos);

                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionB,2,pos);
                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionC,3,pos);
                }
            });
        }

        private  void selectOption(Button btn,int option_num,int questID)
        {
            if(currentlySelectedBtn==null)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
                globalQuestionList.get(questID).setSelectedAnswer(option_num);
                globalQuestionList.get(questID).setStatus(answered);
                currentlySelectedBtn = btn;
            }
            else
            {
                if(currentlySelectedBtn.getId()==btn.getId())
                {
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    globalQuestionList.get(questID).setSelectedAnswer(-1);
                    globalQuestionList.get(questID).setStatus(notAnswered);
                    currentlySelectedBtn = null;
                }
                else
                {
                    currentlySelectedBtn.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);
                    globalQuestionList.get(questID).setSelectedAnswer(option_num);
                    globalQuestionList.get(questID).setStatus(answered);
                    currentlySelectedBtn = btn;

                }
            }
        }



        private void initializeOption(Button btn,int option_num,int questID)
        {
            if(globalQuestionList.get(questID).getSelectedAnswer()==option_num)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
            }
            else
            {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }


        }
    }


}
