package com.example.muhammadfauzanrazandi.persona;

import com.example.muhammadfauzanrazandi.persona.data.model.Dialogflow;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.muhammadfauzanrazandi.persona.data.remote.ApiUtils;
import com.example.muhammadfauzanrazandi.persona.data.remote.DFService;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private TextView txtDoctorQuestion, txtPatientAnswer;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private DFService dfService;

    /** Speech to Text */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);
        dfService = ApiUtils.getDFService();

        txtDoctorQuestion = findViewById(R.id.txtDoctorQuestion);
        txtPatientAnswer = findViewById(R.id.txtPatientAnswer);
        btnSpeak = findViewById(R.id.btnSpeak);

        // hide the action bar
        getSupportActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String input = result.get(0);
                    txtDoctorQuestion.setText(input.substring(0, 1).toUpperCase() + input.substring(1) + "?");
                    getPatientAnswer(input);
                }
                break;
            }
        }
    }

    /** Text to Speech Feature */
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String output) {
        tts.speak(output, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    /** APICalls to Dialogflow API */
    public void getPatientAnswer(String doctorQuestion) {
        dfService.getSpeech(doctorQuestion).enqueue(new Callback<Dialogflow>() {

            @Override
            public void onResponse(Call<Dialogflow> call, Response<Dialogflow> response) {
                if(response.isSuccessful()) {
                    String patientAnswer = response.body().getResult().getFulfillment().getSpeech();

                    Log.d("Response", patientAnswer);

                    txtPatientAnswer.setText(patientAnswer);
                    speakOut(patientAnswer);
                }else {
                    Log.d("Error Response", response.body().getResult().getFulfillment().getSpeech());
                }
            }

            @Override
            public void onFailure(Call<Dialogflow> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
}
