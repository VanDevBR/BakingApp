package br.com.vanilson.bakingapp.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.vanilson.bakingapp.MasterDetailListFragment;
import br.com.vanilson.bakingapp.R;
import br.com.vanilson.bakingapp.StepActivity;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepsAdapterViewHolder> {

    ArrayList<StepModel> steps;
    Context mContext;

    MasterDetailListFragment.OnStepClickListener mCallback;

    public void setCallback(MasterDetailListFragment.OnStepClickListener mCallback) {
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public StepAdapter.StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.step_item, parent, false);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepsAdapterViewHolder holder, final int position) {
        StepModel step = steps.get(position);
        holder.mStepTextView.setText(step.getShortDescription());

        if(step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()){
            Picasso.with(mContext)
                    .load(step.getThumbnailURL())
                    .error(R.drawable.no_picture)
                    .into(holder.mStepImageView);
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.no_picture)
                    .into(holder.mStepImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onStepClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == steps) return 0;
        return steps.size();
    }

    public void setSteps(ArrayList<StepModel> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public List<StepModel> getSteps() {
        return steps;
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mStepTextView;
        public final ImageView mStepImageView;

        public StepsAdapterViewHolder(View view) {
            super(view);
            mStepTextView = view.findViewById(R.id.step_item_tv);
            mStepImageView = view.findViewById(R.id.step_item_iv);
        }

    }


}
