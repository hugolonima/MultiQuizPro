package edu.upc.eseiaat.pma.promultiquiz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //String question_content = "¿Capital de Francia?";
    private int id_answers [] = {
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4,
    };

    private String[] all_questions;

    private TextView question_text;
    private RadioGroup grup;
    private Button btn_next;
    private Button btn_prev;

    private int current_question;
    private int correct_answer;
    private boolean[] answer_is_correct;
    private int[] answers;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("Lifecycle", "onSaveInstancestate");
        super.onSaveInstanceState(outState);
        outState.putInt("correct_answer", correct_answer);
        outState.putInt("current_question", current_question);
        outState.putBooleanArray("answer_is_correct", answer_is_correct);
        outState.putIntArray("answer", answers);
    }


    @Override
    protected void onStop() {
        Log.i("Lifecycle", "onStop");
        super.onStop();

    }

    @Override
    protected void onStart() {
        Log.i("Lifecycle", "onStart");
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        Log.i("Lifecycle", "onDestroy");
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Lifecycle", "oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] answers=getResources().getStringArray(R.array.answers);

        btn_next = (Button) findViewById(R.id.btn_check);
        btn_prev = (Button) findViewById(R.id.btn_prev);

        all_questions = getResources().getStringArray(R.array.questions);
        if(savedInstanceState==null){
            startOver();
        }else{
            Bundle state = savedInstanceState;
            correct_answer =state.getInt("correct_answer");
            current_question = state.getInt("current_question");
            answer_is_correct = state.getBooleanArray("answer_is_correct");
             answers = state.getIntArray("answer");
            showQuestion();
        }



        question_text = (TextView) findViewById(R.id.text_question);

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                if(current_question>0){
                    current_question--;
                    showQuestion();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Hugo", "Pinchado"); //Con esto sale el mensaje en la consola
                //Ahora para que salga el message toast
                //final int correct_answer = getResources().getInteger(R.integer.correct_answer);
                grup = (RadioGroup) findViewById(R.id.radioGroup);
                checkAnswer();
                if(current_question<all_questions.length-1) {
                    current_question++;
                    showQuestion();}
                else{
                    checkResults();
                }
            }
        });
    }

    private void startOver() {
        current_question=0;
        answer_is_correct = new boolean[all_questions.length];
        answers = new int[all_questions.length];
        for(int i=0; i<answers.length;i++){
            answers[i]=-1;
        }
        showQuestion();
    }

    private void checkResults() {
        int correctas =0;
        int incorrectas =0;
        int nocontestadas =0;
        for(int i=0; i< all_questions.length; i++){
            if (answer_is_correct[i]) correctas++;
            else if (answers[i] ==-1) nocontestadas++;
            else incorrectas++;
        }
        String message =
                String.format("Correctas: &d \n Incorrectas: &d No contestadas: &d",
                        correctas, incorrectas, nocontestadas);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.results);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.startOver, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            startOver();
            }
        });
       builder.create().show();


    }

    private void checkAnswer() {
        int index = -1;
        int id = grup.getCheckedRadioButtonId();
        for(int i=0; i<id_answers.length;i++){
            if(id == id_answers[i]){
                index=i;
            }
        }
                /*if(index == correct_answer){
                    Toast.makeText(QuizActivity.this, R.string.correct, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuizActivity.this, R.string.wrong, Toast.LENGTH_SHORT).show();
                }*/
        answer_is_correct[current_question] = (index == correct_answer);
        answers[current_question]=index;
    }

    private void showQuestion() {
        String q = all_questions[current_question];
        String[] parts = q.split(";");
        grup.clearCheck();
        question_text.setText(parts[0]);

        //question_text.setText(R.string.question_content);

        for(int i =0; i<id_answers.length;i++){
            RadioButton rb = (RadioButton) findViewById(id_answers[i]);
            String ans = parts[i+1];
            if(ans.charAt(0) == '*'){
                correct_answer=i;
                ans = ans.substring(1);
            }
            rb.setText(ans);

            if(answers[current_question]==i){
                rb.setChecked(true);
            }

            if(current_question==0){
                btn_prev.setVisibility(View.GONE);
            }else{
                btn_prev.setVisibility(View.VISIBLE);
            }

            if(current_question==all_questions.length-1){
                btn_next.setText(R.string.finish);
            }else{
                btn_next.setText(R.string.next);
            }
        }
    }
}
