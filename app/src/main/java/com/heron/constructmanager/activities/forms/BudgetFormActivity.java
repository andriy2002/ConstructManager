package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.service.BudgetService;

public class BudgetFormActivity extends AppCompatActivity {

    ImageView backArrowImg;
    EditText titleEditText, descEditText, valueEditText;
    Button addButton;

    BudgetService budgetService;

    String constructionUidStr, titleStr, descStr, budgetUidStr;
    float value;

    ValidateInput validateInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_form);
        backArrowImg = findViewById(R.id.budget_form_back_arrow);
        titleEditText = findViewById(R.id.budget_form_title);
        descEditText = findViewById(R.id.budget_form_desc);
        valueEditText = findViewById(R.id.budget_form_value);
        addButton = findViewById(R.id.budget_form_add_button);
        budgetService = new BudgetService(this);
        validateInput = new ValidateInput(BudgetFormActivity.this, titleEditText, descEditText, valueEditText);

        if(getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
        }

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infosVerified()) {
                    getEditTextsContent();
                    budgetService.writeBudget(constructionUidStr, titleStr, descStr, value, budgetUidStr);
                    finish();
                }
            }
        });
    }

    public boolean infosVerified() {
        boolean titleVerified = validateInput.validateTitle();
        boolean descVerified = validateInput.validateDesc();
        boolean valueVerified = validateInput.validateValue();
        return titleVerified && descVerified && valueVerified;
    }

    public void getEditTextsContent() {
        titleStr = titleEditText.getText().toString().trim();
        descStr = descEditText.getText().toString().trim();
        value = Float.parseFloat(valueEditText.getText().toString().trim());
    }
}