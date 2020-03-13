package edu.uga.cs.statesquiz.database;

import android.provider.BaseColumns;

/**
 * A class that defines the Schema of the
 * database for the app.
 *
 * @author Robert Stone
 * @version 1.0
 * @since 2018-10-12
 */
public final class StatesQuizDBContract {
    private StatesQuizDBContract() {}

    /**
     * Defines the values used to create the table that holds
     * the "Questions" and possible Answers for the Quizzes.
     *
     * In this case, "Question" simply refers to the name of the State.
     * It's expected that the UI will provide the form of the Question and
     * insert the State name as appropriate. For example, the UI might provide
     * two Strings, "What is the capital of " and "?". Then the question will be
     * formed by concatenating the name of the State with those two Strings.
     */
    public static class StatesQA implements BaseColumns {
        public static final String TABLE_NAME = "statesqa";
        public static final String STATE = "state";
        public static final String CAPITAL = "capital";
        public static final String SECOND_CITY = "secondcity";
        public static final String THIRD_CITY = "thirdcity";
    }

    /**
     * Defines the values used to create the table that holds
     * the results of Tests.
     */
    public static class QuizResults implements BaseColumns {
        public static final String TABLE_NAME = "quizresults";
        public static final String DATE = "date";
        public static final String RESULTS = "results";
    }
}
