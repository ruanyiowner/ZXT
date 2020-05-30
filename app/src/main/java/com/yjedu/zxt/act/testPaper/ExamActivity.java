package com.yjedu.zxt.act.testPaper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

import com.yjedu.zxt.R;

public class ExamActivity extends Activity {
    Intent intent = null;
    Bundle bundle= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exam);

        intent = getIntent();
        bundle = intent.getExtras();
        String PaperID = bundle.getString("PaperID");
        //this.setTitle(PaperID);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exam, menu);
        return true;
    }

}
