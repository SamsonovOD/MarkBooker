package com.amonimus.markbooker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class EditActivity extends AppCompatActivity {
    Memory memory = Memory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        newField();
    }

    String GetLabelText(EditText et){
        return String.valueOf(et.getText());
    }

    public void clickSaveButton(View view) throws JSONException {
        EditText etRecordName = findViewById(R.id.etRecordName);
        String recordName = GetLabelText(etRecordName);
        JSONObject record = memory.addRecord(recordName);

        LinearLayout layoutLinks = findViewById(R.id.layoutLinks);
        for (int index = 0; index < layoutLinks.getChildCount(); index++) {
            LinearLayout layoutLink = (LinearLayout) layoutLinks.getChildAt(index);

            ConstraintLayout cl0 = (ConstraintLayout) layoutLink.getChildAt(0);
            ConstraintLayout cl1 = (ConstraintLayout) layoutLink.getChildAt(1);
            EditText etLinkName = (EditText) cl0.getChildAt(0);
            EditText erLinkUrl = (EditText) cl1.getChildAt(0);
            String linkName = GetLabelText(etLinkName);
            String url = GetLabelText(erLinkUrl);
            memory.addLink(record, url, linkName);
        }
        finish();
    }

    public void newField() {
        LinearLayout layoutLinks = findViewById(R.id.layoutLinks);
        @SuppressLint("InflateParams")
        View linkContainer = LayoutInflater.from(this).inflate(R.layout.link_contrainer, null);
        layoutLinks.addView(linkContainer);
    }

    public void clickAddField(View view) {
        newField();
    }
}