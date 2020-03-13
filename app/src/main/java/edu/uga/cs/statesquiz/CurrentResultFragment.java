package edu.uga.cs.statesquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Date;
import edu.uga.cs.statesquiz.database.StatesQuizDBHelper;

public class CurrentResultFragment extends Fragment {
    // create the controls to be accessed in the methods bellow
    Button homeScreen;
    Button goToPrevResult;
    Button clickToDisplay;
    TextView currentResult;
    int score;
    @Nullable
    @Override

    /**
     *  Returns the view to be painted
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     *
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // create the view to be painted
        View view;
        // get the bundle's arguments
        Bundle bundle = getArguments();
        // get the question Number to know which page we're in
        int questionNumber = bundle.getInt("questionNumber");
        // inflate the layout
        view = inflater.inflate(R.layout.current_result_fragment, container, false);
        // define the view controls
        TextView info = (TextView) view.findViewById(R.id.prompt);
        currentResult = (TextView) view.findViewById(R.id.currentResult);
        goToPrevResult = (Button) view.findViewById(R.id.goToPrevResult);
        homeScreen = (Button) view.findViewById(R.id.homescreen);
        clickToDisplay = (Button) view.findViewById(R.id.clickToDisplay);

        // Check if we're in the first page to display the instructions
        if(questionNumber >= 0 && questionNumber < 6) {
            // make the buttons disappear to display the results
            goToPrevResult.setVisibility(View.GONE);
            homeScreen.setVisibility(View.GONE);
            clickToDisplay.setVisibility(View.GONE);
            // set the text to override the "result" text
            info.setText("Instructions");
            // set the text size and get the instructions string from the string.xml
            currentResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            currentResult.setText(R.string.beforeStart);
        }
        else {
            // if we're in the result page, then set the listener to the button
            goToPrevResult.setOnClickListener(new buttonClicked());
            homeScreen.setOnClickListener(new buttonClicked());
            clickToDisplay.setOnClickListener(new buttonClicked());
        }
        // return the constructed view
        return view;
    }
    /**
     *  OnClick listener class for the buttons to:
     *  - Display Current Result
     *  - Display Previous Results
     *  - Go to home screen
     *
     */
    private class buttonClicked implements View.OnClickListener{

        @Override

        /**
         *  OnClick listener method fot the buttons
         * @param view
         *
         */
        public void onClick(View view) {
            // if the goToPrevResult button is clicked, then the previous results should be displayed
            if (view.getId() == goToPrevResult.getId()){
                // create an intent to call the previous results Activity
                Intent intent = new Intent(view.getContext(), PreviousResults.class);
                view.getContext().startActivity(intent);

            } // if the homeScreen button is clicked, then the splash screen should be displayed
            else if(view.getId() == homeScreen.getId()){
                // create an intent to call the splash screen Activity
                Intent intent = new Intent(view.getContext(), SplashScreen.class);
                view.getContext().startActivity(intent);

            }// if the clickToDisplay button is clicked, then the current score should be displayed
            else if (view.getId() == clickToDisplay.getId()){
                // get the score from the bundle
                score = getArguments().getInt("score");
                //get the date to be inserted to the database
                Date date =  new Date();
                // format the date to be short
                String dateString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
                // create a StatesQuizDBHelper to insert the result into the database
                StatesQuizDBHelper dbHelper = new StatesQuizDBHelper(getContext());
                // call putQuizResults() to insert the date and the score
                dbHelper.putQuizResults( dateString, score);
                // finally display the result
                currentResult.setText(score + "/6");
            }
        }
    }
}
