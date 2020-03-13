package edu.uga.cs.statesquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import edu.uga.cs.statesquiz.database.StatesQuizDBHelper;

public
class PreviousResults extends AppCompatActivity {

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous_results);

        StatesQuizDBHelper dbHelper = new StatesQuizDBHelper(this);
        ArrayList<String> results = dbHelper.getAllQuizResults();
        if (results.size()==0)
            results.add("You Have No Previous Results");
        ListView listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.results_listview,R.id.singleResult,results);
        listView.setAdapter(adapter);

    }
}
