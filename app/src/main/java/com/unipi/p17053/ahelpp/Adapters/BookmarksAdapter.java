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
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.Models.QuestionModel;
import com.unipi.p17053.ahelpp.R;

import java.util.List;

import static com.unipi.p17053.ahelpp.DatabasePannel.globalBookmrkIDList;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalQuestionList;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {

    private List<QuestionModel> questionModelList;
    private StorageReference myStorageRef;

    public BookmarksAdapter(List<QuestionModel> questionModelList) {
        this.questionModelList = questionModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout,parent,false);
        return new BookmarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.ViewHolder holder, int position) {

        String ques = questionModelList.get(position).getQuestion();
        String a = questionModelList.get(position).getOptionA();
        String b = questionModelList.get(position).getOptionB();
        String c = questionModelList.get(position).getOptionC();
        int correctAnsw = questionModelList.get(position).getCorrectAnswer();

        holder.setData( position , ques,a,b, c,correctAnsw);

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
            answerA = itemView.findViewById(R.id.answerA);
            answerB = itemView.findViewById(R.id.answerB);
            answerC = itemView.findViewById(R.id.answerC);
            unbookOption =itemView.findViewById(R.id.unbookOption);
        }

        private void setData(int pos , String ques,String a,String b,String c,int correctAnsw)
        {
            questionNo.setText("Ερώτηση : "+ (pos+1));

            //Set Image for Question from firebase storage
            myStorageRef = FirebaseStorage.getInstance().getReference("images/"+ques);
            Glide.with(itemView.getContext())
                    .load(ques)
                    .into(question);

            result = itemView.findViewById(R.id.result);
            result.setText("");
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

            if(correctAnsw==1)
            {
                answerA.setTextColor(itemView.getContext().getResources().getColor(R.color.right));
                answerA.setTypeface(null, Typeface.BOLD);
            }
            else if(correctAnsw==2)
            {
                answerB.setTextColor(itemView.getContext().getResources().getColor(R.color.right));
                answerB.setTypeface(null, Typeface.BOLD);
            }
            else if(correctAnsw==3)
            {
                answerC.setTextColor(itemView.getContext().getResources().getColor(R.color.right));
                answerC.setTypeface(null, Typeface.BOLD);
            }


        }

    }
}
