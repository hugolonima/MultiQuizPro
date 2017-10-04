package edu.upc.eseiaat.pma.promultiquiz;

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
    private int correct_answer;
    private String[] all_questions;
    private int current_question;
    private TextView question_text;
    private RadioGroup grup;
    private boolean[] answer_is_correct;
    private int[] answers;
    private Button btn_next;
    private Button btn_prev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] answers=getResources().getStringArray(R.array.answers);

        btn_next = (Button) findViewById(R.id.btn_check);
        btn_prev = (Button) findViewById(R.id.btn_prev);

        all_questions = getResources().getStringArray(R.array.questions);
        current_question=0;
        answer_is_correct = new boolean[all_questions.length];
        answers = new int[all_questions.length];
        for(int i=0; i<answers.length;i++){
            answers[i]=-1;
        }
        question_text = (TextView) findViewById(R.id.text_question);

        showQuestion();
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
                    int correctas =0;
                    int incorrectas =0;
                    for(boolean b : answer_is_correct){
                        if (b) correctas++;
                        else incorrectas++;
                    }
                    String resultado =
                            String.format("Correctas: &d -- Incorrectas &d", correctas, incorrectas);
                    Toast.makeText(MainActivity.this,resultado, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
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
