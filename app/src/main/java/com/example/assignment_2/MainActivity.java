package com.example.assignment_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder startDialog;
    AlertDialog.Builder finishDialog;
    AlertDialog finish;
    public static final int TEXT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDialog = new AlertDialog.Builder(this);
        startDialog.setTitle(getString(R.string.difficulty));

        final Intent i = new Intent(this, QuizActivity.class);

        //Allow user to select difficulty and pass the difficulty to the QuizActivity
        startDialog.setItems(new CharSequence[]{getString(R.string.displayEasy),
                        getString(R.string.displayMedium), getString(R.string.displayHard)},
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            i.putExtra("difficulty", getString(R.string.easy));
                            startActivityForResult(i, TEXT_REQUEST);
                            break;
                        case 1:
                            i.putExtra("difficulty", getString(R.string.medium));
                            startActivityForResult(i, TEXT_REQUEST);
                            break;
                        case 2:
                            i.putExtra("difficulty", getString(R.string.hard));
                            startActivityForResult(i, TEXT_REQUEST);
                            break;
                    }
                }
            });
    }

    //Display the difficulty selector
    public void quizPrompt(View view){
        startDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Test for the right intent reply.
        if (requestCode == TEXT_REQUEST){
            //Test to make sure the intent reply result was good.
            if (resultCode == RESULT_OK){
                //Read in the score and the total number of questions
                int score = data.getIntExtra("score", 0);
                int numQuestions = data.getIntExtra("numQuestions", 5);

                //Display the user's score out of the total number of questions
                finishDialog = new AlertDialog.Builder(this);
                finish = finishDialog.create();
                finishDialog.setTitle("You got " + score + " out of " + numQuestions + " questions correct.\nThank you for playing!");
                finishDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                finish.cancel();
                        }
                    }
                });
                finishDialog.show();
            }
        }
    }
}