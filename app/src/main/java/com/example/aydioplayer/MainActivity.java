package com.example.aydioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public final String DATA_STREAM = "http://ep128.hostingradio.ru:8030/ep128";
    public final String DATA_SD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/music.mp3";

    public String nameAudio = "";
    //public MediaPlayer mediaPlayer = new MediaPlayer();
    public MediaPlayer mediaPlayer = null;

    public AudioManager audioManager;
    public TextView textOut;
    public Toast toast;
    public Switch switchLoop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mediaPlayer = new MediaPlayer(); //Создание медия плеера

        textOut = findViewById(R.id.textOut);
        switchLoop = findViewById(R.id.switchLoop);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int r = (int) 11.4;


        switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked) {
                if (mediaPlayer != null)
                    mediaPlayer.setLooping(isCheked);
            }
        });


    }


    public void onClickSource(View view) throws IOException {

        releaseMediaPlayer();

        mediaPlayer = new MediaPlayer(); //Создание медия плеера

        try {
            switch (view.getId()) {
                case R.id.btnStream:
                    toast = Toast.makeText(this, "Запущен поток аудио", Toast.LENGTH_SHORT);
                    toast.show();

                    //mediaPlayer = new MediaPlayer();


                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(DATA_STREAM);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setOnPreparedListener(this);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        Exception error = e;
                    }

                    nameAudio = "Какая то там мелодия";
                    break;

                case R.id.btnRAW:
                    toast = Toast.makeText(this, "файл с памяти телефона", Toast.LENGTH_SHORT);
                    toast.show();
                    mediaPlayer = MediaPlayer.create(this, R.raw.app_src_main_res_raw_flight_of_the_bumblebee);
                    mediaPlayer.start();
                    nameAudio = "Полёт Шмеля";
                    break;

                case R.id.btnSD:

                    toast = Toast.makeText(this, "файл с SD-карты", Toast.LENGTH_SHORT);
                    toast.show();

                    //mediaPlayer = new MediaPlayer();

                    mediaPlayer.setDataSource(DATA_SD);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
            toast = Toast.makeText(this, "Источник информации не найден", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (mediaPlayer == null) return;
        mediaPlayer.setLooping(switchLoop.isChecked());
        mediaPlayer.setOnCompletionListener(this);

    }


    public void onClick(View view) {
        if (mediaPlayer == null) return;
        textOut = findViewById(R.id.textOut);

        switch (view.getId()) {
            case R.id.btnResume:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.btnPause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.btnStop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                break;
            case R.id.btnForward:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                    mediaPlayer.getCurrentPosition();
                }
                break;
            case R.id.btnBack:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                }
                break;
        }

//        textOut.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
//                + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
        textOut.setText(nameAudio + "\n(проигрывание " + ")" + "время" + mediaPlayer.getCurrentPosition() + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        toast = Toast.makeText(this, "Музыки больше нет", Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}