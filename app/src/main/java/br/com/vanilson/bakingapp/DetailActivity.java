package br.com.vanilson.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            MasterDetailListFragment detailListFragment = new MasterDetailListFragment();

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.master_list_fragment, detailListFragment)
                    .commit();
        }


    }
}
