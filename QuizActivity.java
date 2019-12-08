package student.inti.quizizz;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    Button ok;
    Dialog dialog;
    TextView dialog_box_text;

    public static final String SAVE_HIGH_SCORE="saveHighScore";
    private static final long COUNTDOWN_TIMER=15000;

    private static final String ROTATE_SCORE="rotateScore";
    private static final String ROTATE_QUESTION_NUM="rotateQuestionNum";
    private static final String ROTATE_TIME_REMAINING="rotateTimeRemaining";
    private static final String ROTATE_ANSWER="rotateAnswer";
    private static final String ROTATE_QUESTION_LIST="rotateQuestionList";

    private TextView QuestionTextView;
    private TextView ScoreTextView;
    private TextView QuestionNumTextView;
    private TextView TimerTextView;
    private TextView FeedbackTextView;

    private TextView CategoryTextView;

    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button NextButton;

    private ProgressBar ProgressBar;

    private ColorStateList DefaultColorRb;

    private ColorStateList DefaultColorTimer;

    private CountDownTimer timer;
    private long TimeRemaining;


    private ArrayList<QuizQuestions> questionList;
    private int questionNum;
    private int questionTotal;
    private QuizQuestions currentQuestion;

    private int score;
    private boolean answered;

    private long BackButtonTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        QuestionTextView = findViewById(R.id.text_view_question);
        ScoreTextView = findViewById(R.id.text_view_score);
        QuestionNumTextView = findViewById(R.id.text_view_question_count);
        TimerTextView = findViewById(R.id.text_view_countdown);
        FeedbackTextView = findViewById(R.id.text_view_feedback);

        CategoryTextView=findViewById(R.id.text_view_category);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        NextButton = findViewById(R.id.button_confirm_next);

        ProgressBar = findViewById(R.id.progress_bar);

        DefaultColorRb = rb1.getTextColors();

        DefaultColorTimer = TimerTextView.getTextColors();

        Intent intent=getIntent();
        String category=intent.getStringExtra(MainActivity.SELECT_CATEGORY);

        CategoryTextView.setText("Category: "+category);

        if (savedInstanceState == null){
            QuizDatabase db = new QuizDatabase(this);
        questionList = db.retrieveAllQuestions(category);
        questionTotal = questionList.size();
        Collections.shuffle(questionList);
        displayNextQuestion();
        }else{
            questionList=savedInstanceState.getParcelableArrayList(ROTATE_QUESTION_LIST);
            questionTotal=questionList.size();
            questionNum=savedInstanceState.getInt(ROTATE_QUESTION_NUM);
            currentQuestion=questionList.get(questionNum-1);
            score=savedInstanceState.getInt(ROTATE_SCORE);
            TimeRemaining=savedInstanceState.getLong(ROTATE_TIME_REMAINING);
            answered=savedInstanceState.getBoolean(ROTATE_ANSWER);
            if(!answered){
                startTimer();
            }else{
                setCountDownTimer();
                showAnswer();
            }
        }

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkMyAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    displayNextQuestion();
                }
            }
        });

    }

    private void displayNextQuestion() {
        rb1.setTextColor(DefaultColorRb);
        rb2.setTextColor(DefaultColorRb);
        rb3.setTextColor(DefaultColorRb);
        rb4.setTextColor(DefaultColorRb);
        rbGroup.clearCheck();

        if (questionNum < questionTotal) {
            currentQuestion = questionList.get(questionNum);
            QuestionTextView.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());
            FeedbackTextView.setText("");

            questionNum++;
            ScoreTextView.setText("Score: "+score +"/"+questionTotal);
            QuestionNumTextView.setText("Question: " + questionNum + "/" + questionTotal);
            answered = false;
            NextButton.setText("Check My Answer");

            TimeRemaining=COUNTDOWN_TIMER;
            startTimer();
        } else {
            endQuiz();
        }
    }

    private void startTimer(){
        timer=new CountDownTimer(TimeRemaining,1000) {
            @Override
            public void onTick(long l) {
                int secondsRemaining=(int)(l/1000)%60;
                secondsRemaining--;
                ProgressBar.setProgress(15-secondsRemaining);
                TimeRemaining=l;
                setCountDownTimer();

            }

            @Override
            public void onFinish() {
                TimeRemaining=0;
                setCountDownTimer();
                checkMyAnswer();

            }
        }.start();
    }

    private void setCountDownTimer(){
        int seconds=(int)(TimeRemaining/1000)%60;

        String timerInString=String.format(Locale.getDefault(),"%02dsec",seconds);
        TimerTextView.setText(timerInString);

        if (TimeRemaining<11000 && TimeRemaining>=6000){
            TimerTextView.setTextColor(Color.YELLOW);
        }
        else if (TimeRemaining<6000){
            TimerTextView.setTextColor(Color.RED);
        }
        else{
            TimerTextView.setTextColor(DefaultColorTimer);
        }
    }

    private void checkMyAnswer() {
        answered = true;
        timer.cancel();

        RadioButton rbChecked = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerIndex = rbGroup.indexOfChild(rbChecked) + 1;

        if (answerIndex == currentQuestion.getAnswer()) {
            score++;
            ScoreTextView.setText("Score: " + score+"/5");
            FeedbackTextView.setText("You are correct! +1 point");
            FeedbackTextView.setTextColor(Color.GREEN);
        }else{
            score--;
            FeedbackTextView.setText("You are wrong! -1 point");
            ScoreTextView.setText("Score: " + score+"/5");
            FeedbackTextView.setTextColor(Color.RED);
        }
        showAnswer();
    }

    private void showAnswer() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswer()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                break;
        }

        if (questionNum < questionTotal) {
            NextButton.setText("Next Question");
        } else {
            NextButton.setText("End My Quiz");
        }
    }

    private void endQuiz() {
        Intent scoreIntent=new Intent();
        scoreIntent.putExtra(SAVE_HIGH_SCORE,score);
        setResult(RESULT_OK,scoreIntent);
        if(score>=3) {
            dialog = new Dialog(QuizActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog);
            dialog_box_text = (TextView) dialog.findViewById(R.id.dialog_box_text_view);
            dialog_box_text.setText("Well Done You've Passed The Quiz!\nYour Score: " + score + "/5\nKeep Up The Good Work!");
            ok = (Button) dialog.findViewById(R.id.ok);
            ok.setEnabled(true);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }

            });

            dialog.show();
        }
        else{
            dialog = new Dialog(QuizActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog2);
            dialog_box_text = (TextView) dialog.findViewById(R.id.dialog_box_text_view);
            dialog_box_text.setText("You Failed The Quiz\nYour Score: " + score + "/5\nBetter Luck Next Time!");
            ok = (Button) dialog.findViewById(R.id.ok);
            ok.setEnabled(true);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }

            });

            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (BackButtonTime+2500>System.currentTimeMillis()){
            finish();
        }else{
            Toast.makeText(this,"Press back again to exit quiz",Toast.LENGTH_SHORT).show();
        }
        BackButtonTime=System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ROTATE_SCORE,score);
        outState.putInt(ROTATE_QUESTION_NUM,questionNum);
        outState.putLong(ROTATE_TIME_REMAINING,TimeRemaining);
        outState.putBoolean(ROTATE_ANSWER,answered);
        outState.putParcelableArrayList(ROTATE_QUESTION_LIST,questionList);
    }
}
