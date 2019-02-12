package com.example.assignment_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.difficulty));

        final Intent i = new Intent(this, QuizActivity.class);

        alertDialog.setItems(new CharSequence[]{getString(R.string.easy),
                        getString(R.string.medium), getString(R.string.hard)},
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            i.putExtra("difficulty", getString(R.string.easy));
                            startActivity(i);
                            break;
                        case 1:
                            i.putExtra("difficulty", getString(R.string.medium));
                            startActivity(i);
                            break;
                        case 2:
                            i.putExtra("difficulty", getString(R.string.hard));
                            startActivity(i);
                            break;
                    }
                }
            });
    }

    public void quizPrompt(View view){
        alertDialog.show();
    }
}
