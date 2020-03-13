package edu.uga.cs.statesquiz;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;
import edu.uga.cs.statesquiz.database.StatesQuizDBHelper;

public class QuizActivity extends AppCompatActivity {

    /**
     *  questionsIndex to generate 6 random numbers for the questions
     *  questions the states pulled from the database
     */
    ArrayList<Integer> questionsIndex;
    ArrayList<String> questionsArray;
    int score;
    int questionNumber;
    String questionsString ;
    Bundle bundle  = new Bundle();
    ViewPager viewPager;
    SwipeAdapter swipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        // Randomize 6 unique numbers
        questionsIndex = new ArrayList<Integer>(6);
        int count =0;
        Random random = new Random();
        while(count < 6){
            int questionShuffle = random.nextInt(50)+1;
            if(!(questionsIndex.contains(questionShuffle))){
                questionsIndex.add(questionShuffle);
                Log.i("NUMBERS ARRAY", Integer.toString(questionsIndex.get(count)));
                count++;
            }
        }
        // create StatesQuizDBHelper to populate the states database for the first time only
        StatesQuizDBHelper dbHelper = new StatesQuizDBHelper(this);
        // get the states corresponding to the index array
        // Each array item is in this form: S1,C1,C2,C3
        questionsArray = dbHelper.getQuizQuestions(questionsIndex);
        // combine the 6 states (array items) into a string for easier transfer btw fragments
        // S1,C1,C2,C3/S2,C1,C2,C3/S3,C1,C2,C3/S4,C1,C2,C3 S5,C1,C2,C3/S6,C1,C2,C3
        questionsString = questionsArray.get(0)+"/";
        for(int i=1;i<questionsArray.size()-1;i++){
            questionsString+=questionsArray.get(i)+"/";
            if(i==4)
                questionsString+=questionsArray.get(i+1);
        }

        //questionsString = "Kentucky,Frankfort,Louisville,Harrodsburg/South Dakota,Pierre,Sioux Falls,Rapid City/North Dakota,Bismarck,Fargo,Pembina/North Carolina,Raleigh,Charlotte,Bath/Maryland,Annapolis,Baltimore,St. Marys City/Indiana,Indianapolis,Vincennes,Fort Wayne";
        Log.i("QA - QUESTIONS STRING", questionsString);

        // create a ViewPager for the swipe view
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(0);
    }

    /**
     *  SwipeAdapter class to create the swipe view
     *
     */
    public class SwipeAdapter extends FragmentStatePagerAdapter {

        public SwipeAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        /**
         *  Display the instructions fragment on page 0
         *  Display the questions fragment on pages 1-6
         *  Display the results fragment on page 7
         *
         *  @param position - the number of page currently displayed
         *
         */
        public Fragment getItem(int position) {
            // put the score, questionsString, and questionNumber into a bundle to pass to the fragments
            if(bundle.getInt("score") != score)
                Log.i("QA:SwipeA - Score Dif:",Integer.toString(score));
            else
                Log.i("QA:SwipeA - Score Same:",Integer.toString(score));

            bundle.putString("questionsString", questionsString);
            score = bundle.getInt("score");
            //questionNumber = position;
            //questionNumber = bundle.getInt("questionNumber");
            //position = bundle.getInt("questionNumber");
            //if (position==0)
            //    position++;
            Log.i("QA:SwipeA - score",Integer.toString(score));
            //Log.i("QA:Swipe - Bscore",Integer.toString(bundle.getInt("score")));
            Log.i("QA:Swipe - quesNum",Integer.toString(questionNumber));
            Log.i("QA:Swipe - PquesNum",Integer.toString(position));
            Log.i("QA:Swipe - BquesNum",Integer.toString(bundle.getInt("questionNumber")));

            switch (position) {
                case 0:
                    // if it's the first page, display the instructions
                    CurrentResultFragment currentResultFragment0 = new CurrentResultFragment();
                    bundle.putInt("questionNumber", 0);
                    currentResultFragment0.setArguments(bundle);
                    return currentResultFragment0;
                case 1:
                    // display question 1 fragment and pass the values
                    QuestionsFragment questionFragment1 = new QuestionsFragment();
                    bundle.putInt("questionNumber", 1);
                    score = bundle.getInt("score");
                    bundle.putInt("score", score);
                    questionFragment1.setArguments(bundle);
                    return questionFragment1;
                case 2:
                    // display question 2 fragment and pass the values
                    QuestionsFragment questionFragment2 =  new QuestionsFragment();
                    bundle.putInt("questionNumber", 2);
                    questionFragment2.setArguments(bundle);
                    return questionFragment2;
                case 3:
                    // display question 3 fragment and pass the values
                    QuestionsFragment questionFragment3 =  new QuestionsFragment();
                    bundle.putInt("questionNumber", 3);
                    questionFragment3.setArguments(bundle);
                    return questionFragment3;
                case 4:
                    // display question 4 fragment and pass the values
                    QuestionsFragment questionFragment4 =  new QuestionsFragment();
                    bundle.putInt("questionNumber", 4);
                    questionFragment4.setArguments(bundle);
                    return questionFragment4;
                case 5:
                    // display question 5 fragment and pass the values
                    QuestionsFragment questionFragment5 =  new QuestionsFragment();
                    bundle.putInt("questionNumber", 5);
                    questionFragment5.setArguments(bundle);
                    return questionFragment5;
                case 6:
                    // display question 6 fragment and pass the values
                    QuestionsFragment questionFragment6 =  new QuestionsFragment();
                    bundle.putInt("questionNumber", 6);
                    questionFragment6.setArguments(bundle);
                    return questionFragment6;
                case 7:
                    // display the current result fragment
                    CurrentResultFragment currentResultFragment = new CurrentResultFragment();
                    bundle.putInt("questionNumber", 7);
                    currentResultFragment.setArguments(bundle);
                    return currentResultFragment;
            }
            return null;
        }

        @Override
        /**
         *  The numbers of pages for the swipeAdapter
         *  1 fragment for the instructions
         *  6 fragments for the questions
         *  1 fragment for the results
         *
         */
        public int getCount() {
            return 8;
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        score = bundle.getInt("score");
        questionNumber = bundle.getInt("questionNumber");
        outState.putString("questionsString",questionsString);
        outState.putInt("questionNumber",questionNumber);
        outState.putInt("score",score);

        Log.i("QA0 ***********", "onSaveInstanceState");
        Log.i("QA00 *********** score", Integer.toString(score));
        Log.i("QA000 ******* quesNum", Integer.toString(questionNumber));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected
    void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questionsString = savedInstanceState.getString("questionsString");
        score = savedInstanceState.getInt("score");
        questionNumber = savedInstanceState.getInt("questionNumber");
        //questionNumber--;
        bundle.putString("questionsString",savedInstanceState.getString("questionsString"));
        bundle.putInt("score",savedInstanceState.getInt("score"));
        bundle.putInt("questionNumber",savedInstanceState.getInt("questionNumber"));
        //swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        //viewPager.setAdapter(swipeAdapter);
        //viewPager.setCurrentItem(questionNumber);
        swipeAdapter.notifyDataSetChanged();
        Log.i("QA1 ***********", "onSaveInstanceState");
        Log.i("QA11 *********** score", Integer.toString(score));
        Log.i("QA111 ******* quesNum", Integer.toString(questionNumber));

    }
}


