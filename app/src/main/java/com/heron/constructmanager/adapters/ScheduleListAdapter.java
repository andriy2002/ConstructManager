
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
import com.heron.constructmanager.activities.views.ScheduleViewActivity;
import com.heron.constructmanager.models.Schedule;

import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private final List scheduleList;
    private final String constructionUid;
    private final Context context;


    public ScheduleListAdapter(List scheduleList, Context context, String constructionUid) {
        this.scheduleList = scheduleList;
        this.context = context;
        this.constructionUid = constructionUid;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardScheduleTitleTextView, cardScheduleDeadlineTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardScheduleTitleTextView = itemView.findViewById(R.id.card_schedule_title_text);
            cardScheduleDeadlineTextView = itemView.findViewById(R.id.card_schedule_deadline_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View scheduleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(scheduleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Schedule schedule = (Schedule) scheduleList.get(position);
        holder.cardScheduleTitleTextView.setText(schedule.getTitle());
        holder.cardScheduleDeadlineTextView.setText(schedule.getDeadline());

        holder.cardScheduleTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, ScheduleViewActivity.class);
                intent = putExtrasSchedule(intent, schedule);
                context.startActivity(intent);
            }
        });
        holder.cardScheduleDeadlineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, ScheduleViewActivity.class);
                intent = putExtrasSchedule(intent, schedule);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    private Intent putExtrasSchedule(Intent intent, Schedule schedule) {
        intent.putExtra("constructionUid", constructionUid);
        intent.putExtra("scheduleUid", schedule.getScheduleUid());
        intent.putExtra("title", schedule.getTitle());
        intent.putExtra("deadline", schedule.getDeadline());
        intent.putExtra("state", schedule.getState());
        intent.putExtra("finishDate", schedule.getFinishDate());
        return intent;
    }

}
