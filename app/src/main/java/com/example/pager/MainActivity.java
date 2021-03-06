package com.example.pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Picasso.with(this).setIndicatorsEnabled(true);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ListViewAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startDetailsWithSharedElementTransition((ViewGroup) view, position);
            }
        });
    }

    private void startDetailsWithSharedElementTransition(ViewGroup itemView, int position) {
        Intent intent = new Intent(this, DetailsActivity.class).putExtra(DetailsActivity.EXTRA_URL_INDEX, position);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                new Pair<>(itemView.getChildAt(0), getString(R.string.transition_picture)),
                new Pair<>(itemView.getChildAt(1), getString(R.string.transition_label))
        );
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}
