package student.inti.quizizz;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ=1;
    public static final String SELECT_CATEGORY="selectCategory";
    public static final String SHARED_PREFERENCES="sharedPreferences";
    public static final String KEY_HIGHSCORE="keyHighscore";

    private TextView HighScoreTextView;
    private Spinner CategorySpinner;

    private int highscore;


    RelativeLayout quiz_layout;
    AnimationDrawable animation_drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quiz_layout = (RelativeLayout) findViewById(R.id.quiz_layout);

        animation_drawable = (AnimationDrawable) quiz_layout.getBackground();
        animation_drawable.setEnterFadeDuration(1000);
        animation_drawable.setExitFadeDuration(1000);
        animation_drawable.start();

        HighScoreTextView=findViewById(R.id.text_view_highscore);
        CategorySpinner=findViewById(R.id.spinner_category);

        String[] categories=QuizQuestions.getCategories();

        ArrayAdapter<String> categoryAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(categoryAdapter);

        getHighScore();


        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });


    }
    private void startQuiz() {
        String category=CategorySpinner.getSelectedItem().toString();
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra(SELECT_CATEGORY,category);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_QUIZ){
            if (resultCode==RESULT_OK){
                int currentScore=data.getIntExtra(QuizActivity.SAVE_HIGH_SCORE,-5);
                if (currentScore>highscore){
                    setLatestHighscore(currentScore);
                }
            }
        }
    }
    private void getHighScore(){
        SharedPreferences preferences=getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        highscore=preferences.getInt(KEY_HIGHSCORE,-5);
        HighScoreTextView.setText("My Highscore: "+highscore);
    }

    private void setLatestHighscore(int latestHighScore){
        highscore=latestHighScore;
        HighScoreTextView.setText("My Highscore: "+highscore);

        SharedPreferences preferences=getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(KEY_HIGHSCORE,highscore);
        editor.apply();
    }
}
