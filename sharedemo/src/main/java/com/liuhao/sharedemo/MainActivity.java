package com.liuhao.sharedemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ShareDialog dialog;
    private ListView mlistview;
    private Button voiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voiceBtn = findViewById( R.id.voicebtn);
        mlistview = findViewById(R.id.listview);

        String packageName = this.getPackageName();
        Toast.makeText(this,"包名 = "+packageName,Toast.LENGTH_SHORT).show();
//        PackageManager pm = getPackageManager();
//        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
//        if(resolveInfos.size() == 0){
//            voiceBtn.getBackground().mutate().setAlpha(100);
//            voiceBtn.setEnabled(false);
//        }
    }

    public void share(View view) {
        shareTo(view);
    }

    private void shareTo(View view) {
        if(dialog == null) {
            dialog = new ShareDialog(this, R.style.loading_dialog);
            dialog.setContent("标题", "内容", "http://baidu.com");
        }
        dialog.show();
    }

    public void voice(View view) {

        try{
            Intent it = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            it.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            it.putExtra(RecognizerIntent.EXTRA_PROMPT,"开始语音");
            startActivityForResult(it,1);
        }catch (Exception e){
            Toast.makeText(this,"找不到语音设备",Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            ArrayList<String> matchs = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mlistview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , matchs));
        }
    }
}
