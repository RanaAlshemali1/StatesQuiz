package edu.uga.cs.statesquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public
class SplashScreen extends AppCompatActivity {

    Button prevResults;
    Button startQuiz;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        TextView overView = (TextView) findViewById(R.id.overview);
        prevResults = (Button) findViewById(R.id.prevResult);
        startQuiz = (Button) findViewById(R.id.startQuiz);

        overView.setText(R.string.overview);
        prevResults.setOnClickListener(new buttonClicked());
        startQuiz.setOnClickListener(new buttonClicked());

    }

    private class buttonClicked implements View.OnClickListener{

        @Override
        public
        void onClick(View view) {
            if (view.getId() == prevResults.getId()){
                Intent intent = new Intent(view.getContext(), PreviousResults.class);
                view.getContext().startActivity(intent);
            }else if(view.getId() == startQuiz.getId()){
                Intent intent = new Intent(view.getContext(), QuizActivity.class);
                view.getContext().startActivity(intent);
            }
        }
    }
}
