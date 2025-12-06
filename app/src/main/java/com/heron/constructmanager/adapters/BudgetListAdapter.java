
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
import com.heron.constructmanager.activities.views.BudgetViewActivity;
import com.heron.constructmanager.models.Budget;

import java.util.List;

public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.ViewHolder> {

    private final List budgetList;
    private final String constructionUid;
    private final Context context;


    public BudgetListAdapter(List budgetList, Context context, String constructionUid) {
        this.budgetList = budgetList;
        this.context = context;
        this.constructionUid = constructionUid;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardBudgetTitleTextView, cardBudgetValueTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBudgetTitleTextView = itemView.findViewById(R.id.card_budget_title_text);
            cardBudgetValueTextView = itemView.findViewById(R.id.card_budget_value_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View budgetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(budgetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Budget budget = (Budget) budgetList.get(position);
        holder.cardBudgetTitleTextView.setText(budget.getTitle());
        holder.cardBudgetValueTextView.setText("UAH " + String.valueOf(budget.getValue()).replace(".", ","));

        holder.cardBudgetTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, BudgetViewActivity.class);
                intent = putExtrasBudget(intent, budget);
                context.startActivity(intent);
            }
        });
        holder.cardBudgetValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, BudgetViewActivity.class);
                intent = putExtrasBudget(intent, budget);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    private Intent putExtrasBudget(Intent intent, Budget budget) {
        intent.putExtra("constructionUid", constructionUid);
        intent.putExtra("title", budget.getTitle());
        intent.putExtra("desc", budget.getDesc());
        intent.putExtra("value", budget.getValue());
        return intent;
    }

}
