package student.inti.quizizz;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizQuestions implements Parcelable {
    public static final String CATEGORY_SCIENCE="Science";
    public static final String CATEGORY_MATHEMATICS="Mathematics";

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answer;
    private String category;

    public QuizQuestions(){

    }

    public QuizQuestions(String question, String option1, String option2, String option3, String option4, int answer, String category) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.category=category;
    }

    protected QuizQuestions(Parcel in) {
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        option4 = in.readString();
        answer = in.readInt();
        category=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeString(option4);
        dest.writeInt(answer);
        dest.writeString(category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizQuestions> CREATOR = new Creator<QuizQuestions>() {
        @Override
        public QuizQuestions createFromParcel(Parcel in) {
            return new QuizQuestions(in);
        }

        @Override
        public QuizQuestions[] newArray(int size) {
            return new QuizQuestions[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public static String[] getCategories(){
        return new String[]{
                CATEGORY_SCIENCE,
                CATEGORY_MATHEMATICS
        };
    }
}


