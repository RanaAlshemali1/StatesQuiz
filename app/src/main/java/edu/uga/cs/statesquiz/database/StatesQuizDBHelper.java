package edu.uga.cs.statesquiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import edu.uga.cs.statesquiz.R;

public class StatesQuizDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "statesquiz.db";
    private Context context;

    private static final String CREATE_STATESQA_TABLE =
            "CREATE TABLE " + StatesQuizDBContract.StatesQA.TABLE_NAME +
            " (" + StatesQuizDBContract.StatesQA._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            StatesQuizDBContract.StatesQA.STATE + " TEXT," +
            StatesQuizDBContract.StatesQA.CAPITAL + " TEXT," +
            StatesQuizDBContract.StatesQA.SECOND_CITY + " TEXT," +
            StatesQuizDBContract.StatesQA.THIRD_CITY + " TEXT)";

    private static final String CREATE_QUIZRESULTS_TABLE =
            "CREATE TABLE " + StatesQuizDBContract.QuizResults.TABLE_NAME +
            " (" + StatesQuizDBContract.QuizResults._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            StatesQuizDBContract.QuizResults.DATE + " INTEGER," +
            StatesQuizDBContract.QuizResults.RESULTS + " TINYINT)";

    private static final String DELETE_STATESQA_TABLE =
            "DROP TABLE IF EXISTS " + StatesQuizDBContract.StatesQA.TABLE_NAME;

    private static final String DELETE_QUIZRESULTS_TABLE =
            "DROP TABLE IF EXISTS " + StatesQuizDBContract.QuizResults.TABLE_NAME;

    public StatesQuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        Log.i("StatesQuizDBHelper","CONSTRUCTORE IS CALLED ***************");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_STATESQA_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUIZRESULTS_TABLE);
        Log.i("IN ON CREATE","THIS SHOULD BE CALLED ***************");
        populateStatesQATable(sqLiteDatabase);

    }

    /**
     * Opens the included csv file that contains the data for the statesqa table and
     * fills in that data.
     *
     * @param db - The SQLiteDatabase object passed into onCreate.
     */
    //TODO: Figure out how to fix the issue with "St. Mary's City" not printing correctly.
    private void populateStatesQATable(SQLiteDatabase db) {
        try {
            /* Solution below from
            https://stackoverflow.com/questions/16672074/import-csv-file-to-sqlite-in-android */
            // Open the CSV file for reading.
            InputStream csv = this.context.getResources().openRawResource(R.raw.state_capitals);
            InputStreamReader csvReader = new InputStreamReader(csv);
            BufferedReader csvBufReader = new BufferedReader(csvReader);

            db.beginTransaction();

            String line;
            while((line = csvBufReader.readLine()) != null) {
                String[] columns = line.split(",");

                ContentValues cv = new ContentValues();
                cv.put(StatesQuizDBContract.StatesQA.STATE, columns[0].trim());
                cv.put(StatesQuizDBContract.StatesQA.CAPITAL, columns[1].trim());
                cv.put(StatesQuizDBContract.StatesQA.SECOND_CITY, columns[2].trim());
                cv.put(StatesQuizDBContract.StatesQA.THIRD_CITY, columns[3].trim());
                db.insert(StatesQuizDBContract.StatesQA.TABLE_NAME, null, cv);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (IOException e) {
            // Unhandled. File to be open is packaged with app. Exception will not occur.
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Currently, any updates to the table will just constitute rebuilding
        // the table. If necessary, I'll create a more sophisticated way
        // to upgrade later that preserves user's scores.

        sqLiteDatabase.execSQL(DELETE_STATESQA_TABLE);
        sqLiteDatabase.execSQL(DELETE_QUIZRESULTS_TABLE);
        onCreate(sqLiteDatabase);
    }


    /**
     *  Returns 6 Questions and Answers from the database.
     *
     * @param indicies - An ArrayList of Integers of the indicies.
     * @return - An ArrayList of Questions. Each String is in the format of
     *          state,capital,secondCity,thirdCity.
     */
    //TODO: Test getQuizQuestions method.
    public ArrayList<String> getQuizQuestions(ArrayList<Integer> indicies) {

        // Open the database for reading.
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> questionsAndAnswers = new ArrayList<>();

        // For each index listed in indicies.
        for(int i = 0; i < indicies.size(); i++) {
            // Obtain the row at the given index.
            String[] columns = {StatesQuizDBContract.StatesQA.STATE, StatesQuizDBContract.StatesQA.CAPITAL,
                    StatesQuizDBContract.StatesQA.SECOND_CITY, StatesQuizDBContract.StatesQA.THIRD_CITY};
            Cursor selection = db.query(StatesQuizDBContract.StatesQA.TABLE_NAME,
                    columns,
                    StatesQuizDBContract.StatesQA._ID + " = " + indicies.get(i),
                    null,
                    null,
                    null,
                    null,
                    null);
            selection.moveToFirst();

            // Create the string from the row's contents.
            String question = selection.getString(0) + "," + selection.getString(1) +
                    "," + selection.getString(2) + "," +selection.getString(3);

            // Put the string into the ArrayList of Strings.
            questionsAndAnswers.add(question);
        }
        return questionsAndAnswers;
    }

    /**
     * Puts the results of a quiz into the database.
     * @param quizDate - A Date object representing the time the Quiz
     *                 was started.
     * @param quizScore - An Integer, between 0 and 100, representing
     */
    public void putQuizResults(String quizDate, int quizScore) {
        // Open the database for writing.
        SQLiteDatabase db = this.getWritableDatabase();

        // Store the data into a ContentValues object.
        ContentValues row = new ContentValues();
        row.put(StatesQuizDBContract.QuizResults.DATE, quizDate);
        row.put(StatesQuizDBContract.QuizResults.RESULTS, quizScore);

        // Insert the new row into the database.
        db.beginTransaction();
        db.insert(StatesQuizDBContract.QuizResults.TABLE_NAME, null, row);
        db.setTransactionSuccessful();
        db.endTransaction();

        // Cleanup connections.
        db.close();
    }

    /**
     * Obtains all past quiz results from the database.
     *
     * @return - An ArrayList of Strings each representing a past quiz result.
     *          The String is of the format "date, score".
     */
    public ArrayList<String> getAllQuizResults() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> quizResultsList = new ArrayList<>();

        // Obtain all Results from the database.
        String[] columns = {StatesQuizDBContract.QuizResults.DATE, StatesQuizDBContract.QuizResults.RESULTS};
        Cursor results = db.query(StatesQuizDBContract.QuizResults.TABLE_NAME,
                columns,
                null,
                null,null,null,null,null);

        while(results.moveToNext()) {

            // Generate the String for the item and add it to the ArrayList.
            String resultStr = results.getString(0 )+ "   -   " + results.getString(1)+"/6";
            quizResultsList.add(resultStr);
        }

        return quizResultsList;
    }
}
