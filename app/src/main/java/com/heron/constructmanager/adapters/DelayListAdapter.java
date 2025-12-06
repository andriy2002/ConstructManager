
package com.heron.constructmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.views.DelayViewActivity;
import com.heron.constructmanager.models.Delay;

import java.util.List;

public class DelayListAdapter extends RecyclerView.Adapter<DelayListAdapter.ViewHolder> {

    private final List<Delay> delaysList;
    private final String constructionUid;
    private final String scheduleUid;
    private final Context context;


    public DelayListAdapter(List<Delay> delaysList, Context context, String constructionUid, String scheduleUid) {
        this.delaysList = delaysList;
        this.context = context;
        this.constructionUid = constructionUid;
        this.scheduleUid = scheduleUid;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardDelayTitleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDelayTitleTextView = itemView.findViewById(R.id.card_delay_title_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View delayView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(delayView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Delay delay = (Delay) delaysList.get(position);
        holder.cardDelayTitleTextView.setText(delay.getTitle());
        holder.cardDelayTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DelayViewActivity.class);
                intent = putExtrasDelay(intent, delay);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return delaysList.size();
    }

    private Intent putExtrasDelay(Intent intent, Delay delay) {
        intent.putExtra("constructionUid", constructionUid);
        intent.putExtra("scheduleUid", scheduleUid);
        intent.putExtra("delayUid", delay.getDelayUid());
        intent.putExtra("title", delay.getTitle());
        intent.putExtra("reason", delay.getReason());
        intent.putExtra("isExcusable", delay.isExcusable());
        intent.putExtra("isCompensable", delay.isCompensable());
        intent.putExtra("isConcurrent", delay.isConcurrent());
        intent.putExtra("isCritical", delay.isCritical());
        intent.putExtra("days", delay.getDays());
        intent.putExtra("aditionalInfo", delay.getAditionalInfo());
        return intent;
    }

}
