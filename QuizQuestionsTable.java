package student.inti.quizizz;

import android.provider.BaseColumns;

public final class QuizQuestionsTable {

    private QuizQuestionsTable(){
    }

    public static class QuestionsTableColumns implements BaseColumns {
        public static final String TABLE_NAME_COLUMN = "quiz_questions";
        public static final String QUESTION_COLUMN = "question";
        public static final String OPTION1_COLUMN = "option1";
        public static final String OPTION2_COLUMN = "option2";
        public static final String OPTION3_COLUMN = "option3";
        public static final String OPTION4_COLUMN = "option4";
        public static final String ANSWER_COLUMN = "answer";
        public static final String CATEGORY_COLUMN = "category";
    }
}
