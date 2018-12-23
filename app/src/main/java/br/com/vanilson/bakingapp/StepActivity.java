

package br.com.vanilson.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import br.com.vanilson.bakingapp.model.StepModel;

public class StepActivity extends AppCompatActivity {
    public static String STEP_DETAIL_KEY = "stepsDetail";
    public static String STEP_POSITION_KEY = "stepPositing";

    private ArrayList<StepModel> mSteps;
    private Integer position;

    private Button nextBt;
    private Button prevBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        nextBt = findViewById(R.id.bt_next);
        prevBt = findViewById(R.id.bt_prev);

        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep();
            }
        });

        prevBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevStep();
            }
        });

        if (savedInstanceState == null) {

            Intent self = getIntent();
            mSteps = self.getParcelableArrayListExtra(STEP_DETAIL_KEY);
            position = self.getIntExtra(STEP_POSITION_KEY, 0);

            getSupportActionBar().setTitle("How to...");

            showStep();

        }


    }

    private void nextStep(){
        position = position < mSteps.size() - 1 ? position + 1 : position;
        showStep();
    }

    private void prevStep(){
        position = position > 0 ? position - 1 : position;
        showStep();
    }

    private void showStep() {
        if(position == 0){
            prevBt.setEnabled(false);
        }else{
            prevBt.setEnabled(true);
        }
        if(position == (mSteps.size() - 1)){
            nextBt.setEnabled(false);
        } else {
            nextBt.setEnabled(true);
        }
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_DETAIL_KEY, mSteps.get(position));
        stepFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.step_fragment_fl, stepFragment)
                .commit();
    }

}