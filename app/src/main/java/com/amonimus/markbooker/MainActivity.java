package com.amonimus.markbooker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Memory memory = Memory.getInstance();
    Timer t = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        try {
            memory.init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    checkRecords();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    public void clickNewButton(View view) {
        startActivity(new Intent(MainActivity.this, EditActivity.class));
    }

    public void clickSave(View view) {
        try {
            jsonSave();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickLoad(View view) {
        try {
            jsonLoad();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    void listRecordInfo(JSONObject record) throws JSONException {
        LinearLayout layoutMain = findViewById(R.id.scrollLayoutMain);

        LinearLayout layoutTitle = new LinearLayout(this);

        TextView tvDisplayName = new TextView(this);
        String displayName = record.getString("DisplayName");
        tvDisplayName.setText(displayName);
        layoutTitle.addView(tvDisplayName);

        Button buttonEdit = new Button(this);
        buttonEdit.setTextSize(6);
        buttonEdit.setMaxHeight(6);
        buttonEdit.setText(R.string.stringEdit);
        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
//            intent.putExtras(record);
            startActivity(intent);
        });
        layoutTitle.addView(buttonEdit);

        layoutMain.addView(layoutTitle);

        JSONArray links = record.getJSONArray("Links");
        for (int i = 0; i < links.length(); i++) {
            JSONObject link = links.getJSONObject(i);

            Button buttonLink = new Button(this);
            String linkName = (String) link.get("LinkName");
            String LinkUrl = (String) link.get("LinkUrl");
            buttonLink.setText(linkName);
            buttonLink.setTag(LinkUrl);
            buttonLink.setAllCaps(false);
            buttonLink.setOnClickListener(v -> {
                String tag = (String) buttonLink.getTag();
                if ((!tag.startsWith("http://")) && (!tag.startsWith("https://"))) {
                    tag = "http://" + tag;
                }
                Uri uri = Uri.parse(tag);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });
            layoutMain.addView(buttonLink);
        }
    }

    void checkRecords() throws JSONException {
        runOnUiThread(() -> {
            try {
                LinearLayout layout = findViewById(R.id.scrollLayoutMain);
                int countRecords = memory.getRecords().length();
                int countChildren = layout.getChildCount();
                if (countRecords != countChildren) {
                    layout.removeAllViews();
                    JSONArray records = memory.getRecords();
                    for (int i = 0; i < records.length(); i++) {
                        listRecordInfo(records.getJSONObject(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    void jsonSave() throws IOException, JSONException {
        String jsonString = memory.toString();
        File file = new File(this.getFilesDir(), "data.json");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        System.out.println(jsonString);
        bufferedWriter.write(jsonString);
        bufferedWriter.close();
    }

    void jsonLoad() throws IOException, JSONException {
        File file = new File(this.getFilesDir(), "data.json");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String jsonString = stringBuilder.toString();
        System.out.println(jsonString);
        memory.overrideJson(jsonString);
    }
}