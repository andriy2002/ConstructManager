package com.heron.constructmanager;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.heron.constructmanager.activities.MainActivity;
import com.heron.constructmanager.activities.forms.BudgetFormActivity;
import com.heron.constructmanager.activities.forms.ConstructionPrepFormActivity;
import com.heron.constructmanager.activities.forms.DelayFormActivity;
import com.heron.constructmanager.activities.forms.PhotoFormActivity;
import com.heron.constructmanager.activities.forms.ReponsabilityFormActivity;
import com.heron.constructmanager.activities.forms.ScheduleFormActivity;
import com.heron.constructmanager.activities.forms.SignUpFormActivity;
import com.heron.constructmanager.activities.forms.UpdateEmailFormActivity;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class ValidateInput {

    Context context;
    EditText emailEditText, passwordEditText, repeatPasswordEditText, titleEditText, addressEditText, typeEditText, responsiblesEditText, descEditText, deadlineEditText, reasonEditText, valueEditText;
    MultiAutoCompleteTextView nachoTextView;;
    Spinner spinner;
    String emailStr, pwStr, repeatPwStr, titleStr, addressStr, typeStr, responsiblesStr, descStr, deadlinelineStr, reasonStr;
    float value;

    public ValidateInput(UpdateEmailFormActivity c, EditText e) {
        this.context = c;
        this.emailEditText = e;
    }

    public ValidateInput(MainActivity c, EditText e, EditText p) {
        this.context = c;
        this.emailEditText = e;
        this.passwordEditText = p;
    }

    public ValidateInput(PhotoFormActivity c, EditText title, EditText desc) {
        this.context = c;
        this.titleEditText = title;
        this.descEditText = desc;
    }

    public ValidateInput(SignUpFormActivity c, EditText e, EditText p, EditText rp){
        this.context = c;
        this.emailEditText = e;
        this.passwordEditText = p;
        this.repeatPasswordEditText = rp;
    }


    public ValidateInput(BudgetFormActivity c, EditText title, EditText desc, EditText value) {
        this.context = c;
        this.titleEditText = title;
        this.descEditText = desc;
        this.valueEditText = value;
    }

    public ValidateInput(ScheduleFormActivity c, EditText title, EditText deadline){
        this.context = c;
        this.titleEditText = title;
        this.deadlineEditText = deadline;
    }


    public ValidateInput(DelayFormActivity c, EditText title, EditText reason){
        this.context = c;
        this.titleEditText = title;
        this.reasonEditText = reason;
    }


    public ValidateInput(ReponsabilityFormActivity c, EditText t, EditText d, EditText dl, Spinner spinner){
        this.context = c;
        this.titleEditText = t;
        this.descEditText = d;
        this.deadlineEditText = dl;
        this.spinner = spinner;
    }

    public ValidateInput(ConstructionPrepFormActivity c, EditText titleEditText, EditText addressEditText, EditText typeEditText, MultiAutoCompleteTextView nachoTextView) {
        this.context = c;
        this.titleEditText = titleEditText;
        this.addressEditText = addressEditText;
        this.typeEditText = typeEditText;
        this.nachoTextView = nachoTextView;
    }


    public boolean validateNachoTextView(){
        String text = nachoTextView.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(context, "Введіть хоча б одного відповідального.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    public boolean validateSpinner(){
        Object obj = spinner.getSelectedItem();
        if (obj == null) {
            Toast.makeText(context, "Preencha o dropdown.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateDeadline() {
        deadlinelineStr = deadlineEditText.getText().toString().trim();
        if (deadlinelineStr.isEmpty()) {
            Toast.makeText(context, "Preencha o Prazo.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateValue() {
        try {
            value = Float.parseFloat(valueEditText.getText().toString().trim());
        } catch (Exception e) {
            Toast.makeText(context, "O valor deve ser composto apenas de números.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (valueEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Preencha o valor.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean validateDesc() {
        descStr = descEditText.getText().toString().trim();
        if (descStr.isEmpty()) {
            Toast.makeText(context, "Preencha a descrição.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    public boolean validateTitle() {
        titleStr = titleEditText.getText().toString().trim();
        if (titleStr.isEmpty()) {
            Toast.makeText(context, "Preencha o Título.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateReason() {
        reasonStr = reasonEditText.getText().toString().trim();
        if (reasonStr.isEmpty()) {
            Toast.makeText(context, "Preencha o Motivo.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateAddress() {
        addressStr = addressEditText.getText().toString().trim();
        if (addressStr.isEmpty()) {
            Toast.makeText(context, "Preencha o endereço.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }
    public boolean validateType() {
        typeStr = titleEditText.getText().toString().trim();
        if (typeStr.isEmpty()) {
            Toast.makeText(context, "Preencha o tipo de construção.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }
    public boolean validateResponsibles() {
        responsiblesStr = responsiblesEditText.getText().toString().trim();
        if (responsiblesStr.isEmpty()) {
            Toast.makeText(context, "Selecione algum responsável pela construction.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateEmail() {
        emailStr = emailEditText.getText().toString().trim();
        if (emailStr.isEmpty()) {
            Toast.makeText(context, "Preencha seu e-mail.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            Toast.makeText(context, "E-mail inválido.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePassword() {
        pwStr = passwordEditText.getText().toString();

        if (pwStr.isEmpty()) {
            Toast.makeText(context, "Preencha sua senha", Toast.LENGTH_LONG).show();
            return false;
        } else if (pwStr.length() < 8) {
            Toast.makeText(context, "Preencha uma senha com mais de 8 caracteres.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateRepeatPassword() {
        repeatPwStr = repeatPasswordEditText.getText().toString().trim();
        if (repeatPwStr.isEmpty()) {
            Toast.makeText(context, "Preencha sua senha novamente", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!repeatPwStr.equals(pwStr)) {
            Toast.makeText(context, "As senhas prenchidas são diferentes", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
