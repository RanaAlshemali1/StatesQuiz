package edu.uga.cs.statesquiz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuestionsFragment extends Fragment {
    View view;
    String userAnswer = "";
    String correctAnswer = "";
    int score ;
    int questionNumber;
    boolean answerCorrect = false;
    List<String> splitQuestions;
    List<String> singleQuestion;
    String questionsString;
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public
    View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create a bundle to get the bundle arguments
        bundle = getArguments();
        questionNumber = bundle.getInt("questionNumber");
        score = bundle.getInt("score");
        questionsString = bundle.getString("questionsString");
        // inflate the layout
        view = inflater.inflate(R.layout.questions_fragment, container, false);

        // Create different scenarios to display the questions
        int[][] shuffleOptions = {{1, 2, 3}, {1, 3, 2}, {2, 1, 3}, {2, 3, 1}, {3, 1, 2}, {3, 2, 1}};
        // Choose on of the shuffling options to randomly display the options
        Random random = new Random();
        int questionShuffle = random.nextInt(6);
        // set the options index
        int optionAindex = shuffleOptions[questionShuffle][0], optionBindex = shuffleOptions[questionShuffle][1], optionCindex = shuffleOptions[questionShuffle][2];

        // split the states and cites on /
        // S1,C1,C2,C3/S2,C1,C2,C3/S3,C1,C2,C3/S4,C1,C2,C3 S5,C1,C2,C3/S6,C1,C2,C3
        Log.i("QF - QUESTION NUMBER", Integer.toString(questionNumber));
        Log.i("QF - QUESTIONS STRING", questionsString);
        splitQuestions =  Arrays.asList(questionsString.split("/"));
        singleQuestion =  Arrays.asList(splitQuestions.get(questionNumber-1).split(","));
        // set the correctAnswer to compare it to the user's answer
        correctAnswer = singleQuestion.get(1);

        // define the view controls
        TextView question = (TextView) view.findViewById(R.id.question);
        TextView qNum = (TextView) view.findViewById(R.id.qNum);
        TextView testing = (TextView) view.findViewById(R.id.testing); // this is just for testing
        RadioGroup options = (RadioGroup) view.findViewById(R.id.options);
        RadioButton optionA = (RadioButton) view.findViewById(R.id.optionA);
        RadioButton optionB = (RadioButton) view.findViewById(R.id.optionB);
        RadioButton optionC = (RadioButton) view.findViewById(R.id.optionC);
        Button checkAnswer = (Button) view.findViewById(R.id.checkAnswer);
        // add click listener to the checkAnswer button
        checkAnswer.setOnClickListener(new CheckAnswer());
        // display the question
        //qNum.setText("Q#" + questionNumber + ":");
        question.setText("What is the capital of " + singleQuestion.get(0)+"?");
        optionA.setText(singleQuestion.get(optionAindex));
        optionB.setText(singleQuestion.get(optionBindex));
        optionC.setText(singleQuestion.get(optionCindex));

        // set CheckedChangeListener for the options
        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which option is clicked
                RadioButton checked = (RadioButton) group.findViewById(checkedId);
                TextView testing = (TextView) view.findViewById(R.id.testing);
                boolean isChecked = checked.isChecked();
                if (isChecked) {
                    userAnswer = checked.getText().toString();
                    score = bundle.getInt("score");
                    // if answer is correct, increment the score
                    if (userAnswer.equals(correctAnswer) && answerCorrect == false) {
                        answerCorrect = true;
                        score++;
                    } // if user changed answer from correct to wrong, decrement score
                    else if (!(userAnswer.equals(correctAnswer)) && answerCorrect == true) {
                        answerCorrect = false;
                        score--;

                    }
                    // put the score into the bundle to pass the value
                    bundle.putInt("score", score);
                    bundle.putInt("questionNumber", questionNumber);
                    setArguments(bundle);
                    testing.setText("You Chose: " + userAnswer + " - " + score);
                }
            }
        });
        return view;
    }
    /**
     *  Class for click listener to check the answer
     */
    private class CheckAnswer implements View.OnClickListener {
        @Override
        /**
         *  onClick listener to check the answer
         * @param v
         *
         */
        public void onClick(View v) {
            String alertString = "Choose an Answer First";
            // Compare the user's answer to the correct one
            if(userAnswer == "")
                Toast.makeText(getActivity(), "Choose an Answer First", Toast.LENGTH_LONG).show();
            else if (userAnswer.equals(correctAnswer)) {
                alertString = "Correct Answer :)!";
            } else {
                alertString = "Wrong Answer :(!";
            }
            // Create a dialog to display the correct answer
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(alertString);
            builder.setMessage("Answer is: " + correctAnswer);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            // allow X to exit the dialog
            builder.setCancelable(true);
            builder.show();

        }
    }

    @Override
    public
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public
    void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("questionsString",questionsString);
        outState.putInt("questionNumber",questionNumber);
        outState.putInt("score",score);
        Log.i("QF0 @@@@@@@@@@@@", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public
    void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            questionsString = savedInstanceState.getString("questionsString");
            score = savedInstanceState.getInt("score");
            questionNumber = savedInstanceState.getInt("questionNumber");

            bundle.putString("questionsString",savedInstanceState.getString("questionsString"));
            bundle.putInt("score",savedInstanceState.getInt("score"));
            bundle.putInt("questionNumber",savedInstanceState.getInt("questionNumber"));

            Log.i("QF1 @@@@@@@@@@@@", "onViewStateRestored - NOT NULL");

        }else {
            Log.i("QF2 @@@@@@@@@@@@", "onViewStateRestored - NULL");
        }

    }

    @Override
    public
    void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            questionsString = savedInstanceState.getString("questionsString");
            score = savedInstanceState.getInt("score");
            questionNumber = savedInstanceState.getInt("questionNumber");

            bundle.putString("questionsString",savedInstanceState.getString("questionsString"));
            bundle.putInt("score",savedInstanceState.getInt("score"));
            bundle.putInt("questionNumber",savedInstanceState.getInt("questionNumber"));
            Log.i("QF3 @@@@@@@@@@@@", "onActivityCreated - NOT NULL");

        }else {
            Log.i("QF4 @@@@@@@@@@@@", "onActivityCreated - NULL");
        }
    }
}

