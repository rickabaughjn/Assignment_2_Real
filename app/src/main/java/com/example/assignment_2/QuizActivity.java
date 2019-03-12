package com.example.assignment_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    public ArrayList<Question> questionList = new ArrayList<>();
    List<Integer> answerNumber = new ArrayList<>();
    String[] line;
    Intent intent;
    String difficulty;
    int questionListIndex = 0;

    TextView questionNumber;
    TextView questionTextView;
    RadioGroup radioGroup;
    RadioButton answerA;
    RadioButton answerB;
    RadioButton answerC;
    RadioButton answerD;
    RadioButton selectedAnswer;
    Button prevButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        intent = getIntent();
        difficulty = intent.getStringExtra("difficulty");

        questionNumber = findViewById(R.id.questionNumber);
        questionTextView = findViewById(R.id.question);
        radioGroup = findViewById(R.id.radioGroup);
        answerA = findViewById(R.id.answerA);
        answerB = findViewById(R.id.answerB);
        answerC = findViewById(R.id.answerC);
        answerD = findViewById(R.id.answerD);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        readQuestions();
        filterQuestions();

        //Shuffle the questionList so question order is randomized
        Collections.shuffle(questionList);

        //Display the first question of the chosen difficulty
        displayQuestion(questionList.get(0));
    }

    //Read in all of the questions from the file
    private void readQuestions(){
        Question question = new Question();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("questions.txt")));

            //Read each line of the file
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
                    case "answer":
                        question.addAnswer(line[1]);
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

    //Pare the questionList down to only questions of the chosen difficulty
    private void filterQuestions(){
        Question currentQuestion;
        int i = 0;

        //Iterate through the list of questions
        while (i < questionList.size()){
            currentQuestion = questionList.get(i);

            //Remove any question that is not of the appropriate difficulty
            if (!(currentQuestion.getDifficulty()).equals(intent.getStringExtra("difficulty"))){
                questionList.remove(i);
            }
            else{
                i++;
            }
        }
    }

    //Load the previous question in the questionList
    public void loadPrevQuestion(View view){
        saveAnswer();

        //Clear the radioGroup selection in preparation for the previous question's answer
        radioGroup.clearCheck();

        //Display the previous question
        questionListIndex--;
        Question prevQuestion = questionList.get(questionListIndex);
        displayQuestion(prevQuestion);

        //Change "Submit" back to "Next ->" when navigating away from the final question
        nextButton.setText(getString(R.string.nextButton));

        //Hide the prevButton on the first question
        if(questionListIndex == 0){
            prevButton.setVisibility(View.INVISIBLE);
        }

        //Select the user's answer if they answered the question before
        selectAnswer();
    }

    //Load the next question in the questionList
    public void loadNextQuestion(View view){
        saveAnswer();

        //Display the next question if not on the final question
        if(nextButton.getText().equals(getString(R.string.nextButton))){
            //Clear the radioGroup selection in preparation for the next question's answer
            radioGroup.clearCheck();

            //Display the next question
            questionListIndex++;
            Question nextQuestion = questionList.get(questionListIndex);
            displayQuestion(nextQuestion);

            //Show the prevButton when navigating away from the first question
            prevButton.setVisibility(View.VISIBLE);

            //Change "Next ->" to "Submit" when on the final question
            if(questionListIndex + 2 > questionList.size()){
                nextButton.setText(getString(R.string.submitButton));
            }

            //Select the user's answer if they answered the question before
            selectAnswer();
        }
        //Finish the quiz if on the final question
        else{
            int score = 0;

            //Add 1 to the user's score for each question they got right
            for(int i = 0; i < questionList.size(); i++){
                if(questionList.get(i).getUserAnswer().equals(questionList.get(i).getCorrectAnswer())){
                    score++;
                }
            }

            //Return the user to the MainActivity along with their score and the # of questions
            Intent i = new Intent();
            i.putExtra("score", score);
            i.putExtra("numQuestions", questionList.size());
            setResult(RESULT_OK, i);
            finish();
        }
    }

    //Display a question on the screen
    public void displayQuestion(Question question){
        ArrayList<String> questionAnswers = question.getAnswers();

        //Clear the answerNumber list
        answerNumber.clear();

        //Add a number to the answerNumber list for each answer
        for(int i = 1; i < question.getAnswers().size() + 1; i++){
            answerNumber.add(i);
        }

        //Shuffle the answerNumber list so the order of the answers is randomized
        Collections.shuffle(answerNumber);

        //Set the question number and question text
        questionNumber.setText(String.format(Locale.US, "Question #%d", questionListIndex + 1));
        questionTextView.setText(question.getQuestion());

        //Set the radio button text to the answers
        for(int i = 0; i < answerNumber.size(); i++){
            switch (answerNumber.get(i)){
                case 1:
                    answerA.setText(questionAnswers.get(i));
                    break;
                case 2:
                    answerB.setText(questionAnswers.get(i));
                    break;
                case 3:
                    answerC.setText(questionAnswers.get(i));
                    break;
                case 4:
                    answerD.setText(questionAnswers.get(i));
                    break;
                default:
                    break;
            }
        }
    }

    //Save the user's answer when navigating away from a question
    public void saveAnswer(){
        //Only save the answer if an answer is present
        if(radioGroup.getCheckedRadioButtonId() != -1) {
            selectedAnswer = findViewById(radioGroup.getCheckedRadioButtonId());
            questionList.get(questionListIndex).setUserAnswer((String) selectedAnswer.getText());
        }
    }

    //Select the user's answer if they answered a question before
    public void selectAnswer(){
        if(!questionList.get(questionListIndex).getUserAnswer().equals("Sample Answer B")){
            RadioButton radioButton;

            //Iterate through each radio button and select the one with the chosen answer
            for(int i = 0; i < radioGroup.getChildCount(); i++){
                radioButton = (RadioButton) radioGroup.getChildAt(i);
                if(radioButton.getText().equals(questionList.get(questionListIndex).getUserAnswer())){
                    radioGroup.check(radioButton.getId());
                }
            }
        }
    }
}