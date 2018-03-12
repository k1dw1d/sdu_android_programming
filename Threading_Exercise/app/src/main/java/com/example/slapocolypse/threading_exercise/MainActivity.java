package com.example.slapocolypse.threading_exercise;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    String url = "http://api.icndb.com/";

    JokeService jokeService;

    Thread workerThread;
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.joke_holder);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jokeService = retrofit.create(JokeService.class);

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running){
                    Call<Joke> joke = jokeService.randomJoke();
                    try {
                        final String joke_txt = joke.execute().body().getValue().getJoke();
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(joke_txt);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        workerThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
        try {
            workerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
