package com.unipi.p17053.ahelpp.Adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.unipi.p17053.ahelpp.Models.QuestionModel;
import com.unipi.p17053.ahelpp.R;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<QuestionModel> questionModelList;
    private StorageReference myStorageRef;

    public AnswersAdapter(List<QuestionModel> questionModelList) {
        this.questionModelList = questionModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout,parent,false);
        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {

        String ques = questionModelList.get(position).getQuestion();
        String a = questionModelList.get(position).getOptionA();
        String b = questionModelList.get(position).getOptionB();
        String c = questionModelList.get(position).getOptionC();
        int selected = questionModelList.get(position).getSelectedAnswer();
        int correctAnsw = questionModelList.get(position).getCorrectAnswer();

        holder.setData( position , ques,a,b, c,selected,correctAnsw);

    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView questionNo,answerA,answerB,answerC,result;
        ImageView question,unbookOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionNo = itemView.findViewById(R.id.questionNo);
            question = itemView.findViewById(R.id.question);
            result = itemView.findViewById(R.id.result);
            answerA = itemView.findViewById(R.id.answerA);
            answerB = itemView.findViewById(R.id.answerB);
            answerC = itemView.findViewById(R.id.answerC);
            unbookOption =itemView.findViewById(R.id.unbookOption);
        }

        private void setData(int pos , String ques,String a,String b,String c,int selected,int correctAnsw)
        {
            questionNo.setText("Ερώτηση : "+ (pos+1));
            unbookOption.setVisibility(View.GONE);

            //Set Image for Question from firebase storage
            myStorageRef = FirebaseStorage.getInstance().getReference("images/"+ques);
            Glide.with(itemView.getContext())
                    .load(ques)
                    .into(question);

            answerA.setText("A. " + a);
            answerB.setText("B. " + b);
            if(c.equals(""))
            {
                answerC.setVisibility(View.GONE);
            }
            else
            {
                answerC.setVisibility(View.VISIBLE);
                answerC.setText("C. " + c);
            }


            if(selected == -1)
            {
                result.setText("Αναπάντητη");
                result.setTextColor(itemView.getContext().getResources().getColor(R.color.notAnswered));
                setOptionColor(selected,R.color.black);
                setCorrectColor(correctAnsw,R.color.right);
            }
            else {
                if (selected == correctAnsw)
                {
                    result.setText("Σωστή");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.right));
                    setOptionColor(selected, R.color.right);
                }
                else
                {
                    result.setText("Λανθασμένη");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.wrong));
                    setOptionColor(selected, R.color.wrong);
                    setCorrectColor(correctAnsw, R.color.right);
                }
            }

        }
        private void setOptionColor(int selected, int color) {

            if (selected==1)
            {
                answerA.setTextColor(itemView.getContext().getResources().getColor(color));
                answerA.setBackgroundColor(itemView.getContext().getResources().getColor( R.color.white));
                answerA.setTypeface(null, Typeface.BOLD);
            }
            else
            {
                answerA.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                answerA.setBackgroundColor(itemView.getContext().getResources().getColor( R.color.white));
                answerA.setTypeface(null, Typeface.NORMAL);
            }
            if (selected==2)
            {
                answerB.setTextColor(itemView.getContext().getResources().getColor(color));
                answerB.setBackgroundColor(itemView.getContext().getResources().getColor( R.color.white));
                answerB.setTypeface(null, Typeface.BOLD);
            }
            else
            {
                answerB.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                answerB.setBackgroundColor(itemView.getContext().getResources().getColor( R.color.white));
                answerB.setTypeface(null, Typeface.NORMAL);
            }
            if (selected==3)
            {
                answerC.setTextColor(itemView.getContext().getResources().getColor(color));
                answerC.setBackgroundColor(itemView.getContext().getResources().getColor( R.color.white));
                answerC.setTypeface(null, Typeface.BOLD);
            }
            else
            {
                answerC.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                answerC.setBackgroundColor(itemView.getContext().getResources().getColor( R.color.white));
                answerC.setTypeface(null, Typeface.NORMAL);
            }
        }

        private void setCorrectColor(int correct, int corretColor)
        {

            if (correct==1)
            {
                answerA.setBackgroundColor(itemView.getContext().getResources().getColor(corretColor));
                answerA.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                answerA.setTypeface(null, Typeface.BOLD);
            }
            if (correct==2)
            {
                answerB.setBackgroundColor(itemView.getContext().getResources().getColor(corretColor));
                answerB.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                answerB.setTypeface(null, Typeface.BOLD);
            }
            if (correct==3)
            {
                answerC.setBackgroundColor(itemView.getContext().getResources().getColor(corretColor));
                answerC.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                answerC.setTypeface(null, Typeface.BOLD);
            }

        }

    }




}
