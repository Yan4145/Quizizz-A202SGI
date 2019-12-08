package student.inti.quizizz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class QuizDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quizizz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase sqLiteDatabase;

    public QuizDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase=sqLiteDatabase;

        final String SQL_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizQuestionsTable.QuestionsTableColumns.TABLE_NAME_COLUMN + " ( " +
                QuizQuestionsTable.QuestionsTableColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizQuestionsTable.QuestionsTableColumns.QUESTION_COLUMN + " TEXT, " +
                QuizQuestionsTable.QuestionsTableColumns.OPTION1_COLUMN + " TEXT, " +
                QuizQuestionsTable.QuestionsTableColumns.OPTION2_COLUMN + " TEXT, " +
                QuizQuestionsTable.QuestionsTableColumns.OPTION3_COLUMN + " TEXT, " +
                QuizQuestionsTable.QuestionsTableColumns.OPTION4_COLUMN + " TEXT, " +
                QuizQuestionsTable.QuestionsTableColumns.ANSWER_COLUMN + " INTEGER, " +
                QuizQuestionsTable.QuestionsTableColumns.CATEGORY_COLUMN + " TEXT" +
                ")";

        sqLiteDatabase.execSQL(SQL_QUESTIONS_TABLE);
        fillQuestionsTableColumns();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QuizQuestionsTable.QuestionsTableColumns.TABLE_NAME_COLUMN);
        onCreate(sqLiteDatabase);

    }
    private void fillQuestionsTableColumns() {
        QuizQuestions q1 = new QuizQuestions("What element do organic compounds contain?", "Carbon",
                "Hydrogen", "Oxygen", "Nitrogen", 1, QuizQuestions.CATEGORY_SCIENCE);
        insertQuestion(q1);
        QuizQuestions q2 = new QuizQuestions("How old is the Earth approximately?", "60,000 years",
                "4.5 billion years", "600 million years", "2.1 billion years", 2, QuizQuestions.CATEGORY_SCIENCE);
        insertQuestion(q2);
        QuizQuestions q3 = new QuizQuestions("How many pairs of chromosomes are in the genome of a typical person?", "1",
                "87", "23", "7923", 3, QuizQuestions.CATEGORY_SCIENCE);
        insertQuestion(q3);
        QuizQuestions q4 = new QuizQuestions("What is the pH of pure water?", "7",
                "9", "0", "4", 1, QuizQuestions.CATEGORY_SCIENCE);
        insertQuestion(q4);
        QuizQuestions q5 = new QuizQuestions("Which of these has the longest wave length?", "Microwaves",
                "X-rays", "Visible light", "Radio waves", 4, QuizQuestions.CATEGORY_SCIENCE);
        insertQuestion(q5);
        QuizQuestions q6 = new QuizQuestions("What is the greatest two digit number?", "12", "99",
                "80", "44", 2, QuizQuestions.CATEGORY_MATHEMATICS);
        insertQuestion(q6);
        QuizQuestions q7 = new QuizQuestions("How much is 90 - 19?", "71", "109",
                "89", "41", 1, QuizQuestions.CATEGORY_MATHEMATICS);
        insertQuestion(q7);
        QuizQuestions q8 = new QuizQuestions("20 is divisible by ____.", "7", "3",
                "1", "None of above", 3, QuizQuestions.CATEGORY_MATHEMATICS);
        insertQuestion(q8);
        QuizQuestions q9 = new QuizQuestions("What is the smallest three digit number?", "999", "888",
                "800", "477", 4, QuizQuestions.CATEGORY_MATHEMATICS);
        insertQuestion(q9);
        QuizQuestions q10 = new QuizQuestions("10 + ____ = 20", "10", "0",
                "4", "42", 1, QuizQuestions.CATEGORY_MATHEMATICS);
        insertQuestion(q10);
    }

    private void insertQuestion(QuizQuestions question) {
        ContentValues cv = new ContentValues();
        cv.put(QuizQuestionsTable.QuestionsTableColumns.QUESTION_COLUMN, question.getQuestion());
        cv.put(QuizQuestionsTable.QuestionsTableColumns.OPTION1_COLUMN, question.getOption1());
        cv.put(QuizQuestionsTable.QuestionsTableColumns.OPTION2_COLUMN, question.getOption2());
        cv.put(QuizQuestionsTable.QuestionsTableColumns.OPTION3_COLUMN, question.getOption3());
        cv.put(QuizQuestionsTable.QuestionsTableColumns.OPTION4_COLUMN, question.getOption4());
        cv.put(QuizQuestionsTable.QuestionsTableColumns.ANSWER_COLUMN, question.getAnswer());
        cv.put(QuizQuestionsTable.QuestionsTableColumns.CATEGORY_COLUMN, question.getCategory());
        sqLiteDatabase.insert(QuizQuestionsTable.QuestionsTableColumns.TABLE_NAME_COLUMN, null, cv);
    }

    public ArrayList<QuizQuestions> retrieveQuestions() {
        ArrayList<QuizQuestions> questionList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + QuizQuestionsTable.QuestionsTableColumns.TABLE_NAME_COLUMN, null);

        if (c.moveToFirst()) {
            do {
                QuizQuestions question = new QuizQuestions();
                question.setQuestion(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.QUESTION_COLUMN)));
                question.setOption1(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION1_COLUMN)));
                question.setOption2(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION2_COLUMN)));
                question.setOption3(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION3_COLUMN)));
                question.setOption4(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION4_COLUMN)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.ANSWER_COLUMN)));
                question.setCategory(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.CATEGORY_COLUMN)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
    public ArrayList<QuizQuestions> retrieveAllQuestions(String category) {
        ArrayList<QuizQuestions> questionList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();

        String[] categorySelection=new String[]{category};
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + QuizQuestionsTable.QuestionsTableColumns.TABLE_NAME_COLUMN +
                " WHERE "+ QuizQuestionsTable.QuestionsTableColumns.CATEGORY_COLUMN + " = ?", categorySelection);

        if (c.moveToFirst()) {
            do {
                QuizQuestions question = new QuizQuestions();
                question.setQuestion(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.QUESTION_COLUMN)));
                question.setOption1(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION1_COLUMN)));
                question.setOption2(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION2_COLUMN)));
                question.setOption3(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION3_COLUMN)));
                question.setOption4(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.OPTION4_COLUMN)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.ANSWER_COLUMN)));
                question.setCategory(c.getString(c.getColumnIndex(QuizQuestionsTable.QuestionsTableColumns.CATEGORY_COLUMN)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
