package com.example.assignment_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    public ArrayList<Question> questionList = new ArrayList<>();
    public ArrayList<Question> orderedQuestionList = new ArrayList<>();
    String[] line;
    Intent intent;
    String difficulty;
    int orderedQuestionListIndex = 0;

    TextView questionNumber;
    TextView questionTextView;
    RadioButton answerA;
    RadioButton answerB;
    RadioButton answerC;
    RadioButton answerD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        intent = getIntent();
        difficulty = intent.getStringExtra("difficulty");

        questionNumber = findViewById(R.id.questionNumber);
        questionTextView = findViewById(R.id.question);
        answerA = findViewById(R.id.answerA);
        answerB = findViewById(R.id.answerB);
        answerC = findViewById(R.id.answerC);
        answerD = findViewById(R.id.answerD);

        readQuestions();
        loadFirstQuestion();
    }

    private void readQuestions(){
        Question question = new Question();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("questions.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                line = mLine.split(":");

                switch (line[0]){
                    case "difficulty":
                        question.setDifficulty(line[1]);
                        break;
                    case "question":
                        question.setQuestion(line[1]);
                        break;
                    case "answerA":
                        question.setAnswerA(line[1]);
                        break;
                    case "answerB":
                        question.setAnswerB(line[1]);
                        break;
                    case "answerC":
                        question.setAnswerC(line[1]);
                        break;
                    case "answerD":
                        question.setAnswerD(line[1]);
                        break;
                    case "correctAnswer":
                        question.setCorrectAnswer(line[1]);
                        break;
                    default:
                        questionList.add(question);
                        question = new Question();
                        break;
                }
            }

            reader.close();
        } catch (IOException e) {
            Log.d(TAG, "IOException when reading trying to read file");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, "IOException when reading trying to close file");
                }
            }
        }
    }

    private void loadFirstQuestion(){
        Question currentQuestion;
        int i = 0;

        while (i < questionList.size()){
            currentQuestion = questionList.get(i);

            if ((currentQuestion.getDifficulty()).equals(intent.getStringExtra("difficulty"))){
                questionTextView.setText(currentQuestion.getQuestion());
                answerA.setText(currentQuestion.getAnswerA());
                answerB.setText(currentQuestion.getAnswerB());
                answerC.setText(currentQuestion.getAnswerC());
                answerD.setText(currentQuestion.getAnswerD());

                orderedQuestionList.add(currentQuestion);
                questionList.remove(i);
                i = questionList.size();
                questionNumber.setText(String.format(Locale.US, "Question #%d", orderedQuestionListIndex + 1));
            }
            else{
                i++;
            }
        }
    }

    public void loadPrevQuestion(View view){
        orderedQuestionListIndex--;
        Question prevQuestion = orderedQuestionList.get(orderedQuestionListIndex);

        questionTextView.setText(prevQuestion.getQuestion());
        answerA.setText(prevQuestion.getAnswerA());
        answerB.setText(prevQuestion.getAnswerB());
        answerC.setText(prevQuestion.getAnswerC());
        answerD.setText(prevQuestion.getAnswerD());
        questionNumber.setText(String.format(Locale.US, "Question #%d", orderedQuestionListIndex + 1));
    }

    public void loadNextQuestion(View view){
        Question currentQuestion;
        int i = 0;

        if (orderedQuestionListIndex + 1 > orderedQuestionList.size() - 1){
            while (i < questionList.size()){
                currentQuestion = questionList.get(i);

                if ((currentQuestion.getDifficulty()).equals(intent.getStringExtra("difficulty"))){
                    questionTextView.setText(currentQuestion.getQuestion());
                    answerA.setText(currentQuestion.getAnswerA());
                    answerB.setText(currentQuestion.getAnswerB());
                    answerC.setText(currentQuestion.getAnswerC());
                    answerD.setText(currentQuestion.getAnswerD());

                    orderedQuestionList.add(currentQuestion);
                    orderedQuestionListIndex++;
                    questionList.remove(i);
                    i = questionList.size();
                }
                else{
                    i++;
                }
            }
        }
        else{
            orderedQuestionListIndex++;
            Question nextQuestion = orderedQuestionList.get(orderedQuestionListIndex);

            questionTextView.setText(nextQuestion.getQuestion());
            answerA.setText(nextQuestion.getAnswerA());
            answerB.setText(nextQuestion.getAnswerB());
            answerC.setText(nextQuestion.getAnswerC());
            answerD.setText(nextQuestion.getAnswerD());
        }

        questionNumber.setText(String.format(Locale.US, "Question #%d", orderedQuestionListIndex + 1));
    }
}
