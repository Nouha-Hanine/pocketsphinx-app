package edu.cmu.pocketsphinx.demo;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class ChatActivity extends Activity implements RecognitionListener {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech textToSpeech;
    private SpeechRecognizer recognizer;
   // private HashMap<String, Integer> captions;
    private Button startListeningButton;
    private boolean isRecording = false;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_chat);

      /*  captions = new HashMap<>();
        String joke = dbHelper.getJoke();
        String studyInfo = dbHelper.getCurrentStudySubject();
        String currentTime = dbHelper.getCurrentTime();

/*
        captions.put("joke", R.string.joke_caption);
        captions.put("study", R.string.study_caption);
        captions.put("time", R.string.time_caption);
        captions.put("music", R.string.music_caption);

*/

        startListeningButton = findViewById(R.id.startRecordingButton);
        startListeningButton.setOnClickListener(v -> {
            if (isRecording==true) {
                stopRecognition();
            } else {
                startRecognition();
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            setupRecognizer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupRecognizer();
            } else {
                finish();
            }
        }
    }

    private void setupRecognizer() {
        try {
            File assetsDir = getExternalFilesDir(null); // ou getAssetsDir() selon votre structure de répertoire

            recognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                    .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                    .getRecognizer();
            recognizer.addListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRecognition() {
        recognizer.startListening("voice_command");
        isRecording = true;
        startListeningButton.setText("Stop Recording");
    }

    private void stopRecognition() {
        recognizer.stop();
        isRecording = false;
        startListeningButton.setText("Start Recording");
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        processVoiceCommand(text);

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        // Handle final result if needed
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(Exception error) {
        // Handle error if needed
    }

    @Override
    public void onTimeout() {
        stopRecognition();
    }
    private void processVoiceCommand(String command) {

        if (command.contains("joke")) {
            String joke = dbHelper.getJoke();

            if (joke != null) {
                speakText(joke);
            } else {
                speakText("I'm sorry, I couldn't find a joke.");
            }
        } else if (command.contains("study")) {
            String studyInfo = dbHelper.getCurrentStudySubject();

            if (studyInfo != null) {
                speakText("You have " + studyInfo);
            } else {
                speakText("I'm sorry, I couldn't find your current study information.");
            }
        } else if (command.contains("time")) {
            String currentTime = dbHelper.getCurrentTime();
            speakText("The current time is " + currentTime);
        } else if (command.contains("music")) {
            playMusic();
        }
    }

    private void speakText(String text) {
        if (text != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    private void playMusic() {

        String pathToYourMusicFile = "path_to_music";


        final MediaPlayer[] mediaPlayer = {new MediaPlayer()};

        try {

            mediaPlayer[0].setDataSource(this, Uri.parse(pathToYourMusicFile));


            mediaPlayer[0].prepare();


            mediaPlayer[0].start();


            mediaPlayer[0].setOnCompletionListener(mp -> {
                if (mediaPlayer[0] != null && mediaPlayer[0].isPlaying()) {
                    mediaPlayer[0].stop();
                    mediaPlayer[0].release();
                    mediaPlayer[0] = null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
}}
