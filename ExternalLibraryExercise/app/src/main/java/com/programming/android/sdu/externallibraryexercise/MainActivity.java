package com.programming.android.sdu.externallibraryexercise;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public class RandomPicturesAdapter extends RecyclerView.Adapter<RandomPicturesAdapter.ViewHolder> {
        private Context context;

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public ViewHolder(View v) {
                super(v);
                imageView = v.findViewById(R.id.ivCell);
            }
        }

        public RandomPicturesAdapter(Context context){
            this.context = context;
        }

        @Override
        public RandomPicturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            View v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_layout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String urlToCall = String.format("https://picsum.photos/500/500?image=%d", position);
            Glide.with(context).clear(holder.imageView);
            Glide.with(context).load(urlToCall).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RandomPicturesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
