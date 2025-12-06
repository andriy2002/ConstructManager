
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
import com.heron.constructmanager.activities.views.ResponsabilityOpenViewActivity;
import com.heron.constructmanager.activities.views.ResponsabilitySolvedViewActivity;
import com.heron.constructmanager.models.Responsability;

import java.util.List;

public class ResponsabilityListAdapter extends RecyclerView.Adapter<ResponsabilityListAdapter.ViewHolder> {

    private final String SOLVED = "Вирішено";
    private final String OPEN = "Відкрити";

    private final List responsabilitiesList;
    private final String constructionUid;
    private final Context context;


    public ResponsabilityListAdapter(List responsabilitiesList, Context context, String constructionUid) {
        this.responsabilitiesList = responsabilitiesList;
        this.context = context;
        this.constructionUid = constructionUid;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardResponsabilityTitleTextView, cardResponsabilityResponsibleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardResponsabilityTitleTextView = itemView.findViewById(R.id.card_responsability_title_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View responsabilityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.responsability_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(responsabilityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Responsability responsability = (Responsability) responsabilitiesList.get(position);
        holder.cardResponsabilityTitleTextView.setText(responsability.getTitle());
        holder.cardResponsabilityTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (responsability.getState().equals(OPEN)) {
                    intent = new Intent(context, ResponsabilityOpenViewActivity.class);
                } else {
                    intent = new Intent(context, ResponsabilitySolvedViewActivity.class);
                }

                intent = putExtrasResponsability(intent, responsability);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return responsabilitiesList.size();
    }

    private Intent putExtrasResponsability(Intent intent, Responsability responsability) {
        intent.putExtra("constructionUid", constructionUid);
        intent.putExtra("responsabilityUid", responsability.getResponsabilityUid());
        intent.putExtra("responsibleEmail", responsability.getResponsibleEmail());
        intent.putExtra("title", responsability.getTitle());
        intent.putExtra("desc", responsability.getDesc());
        intent.putExtra("deadline", responsability.getDeadline());
        intent.putExtra("state", responsability.getState());
        return intent;
    }

}
