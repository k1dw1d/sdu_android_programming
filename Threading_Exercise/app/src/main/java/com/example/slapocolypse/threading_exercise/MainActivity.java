package com.example.slapocolypse.threading_exercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextSwitcher;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.joke_holder)
    TextSwitcher textSwitcher;

    String url = "http://api.icndb.com/";

    JokeService jokeService;

    Thread workerThread;
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        textSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        textSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jokeService = retrofit.create(JokeService.class);

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    Call<Joke> joke = jokeService.randomJoke();
                    try {
                        final String joke_txt = Html.fromHtml(joke.execute().body().getValue().getJoke()).toString();
                        textSwitcher.post(new Runnable() {
                            @Override
                            public void run() {
                                textSwitcher.setText(joke_txt);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(8000);
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
        running = false;
        super.onDestroy();
        try {
            workerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
