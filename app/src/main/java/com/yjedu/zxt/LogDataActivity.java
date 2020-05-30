package com.yjedu.zxt;

import java.util.ArrayList;
import java.util.List;

import com.yjedu.zxt.R;
import com.yjedu.zxt.log.logdata;
import com.yjedu.zxt.model.TextValueItem;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class LogDataActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_data);
        ImageButton imgBtnBack = (ImageButton)this.findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(OnBtnClickListener);

        // 绑定日志类型下拉列表
        SpinlogtypeBind();

    }

    // 日志类型绑定
    private void SpinlogtypeBind(){

        //ArrayAdapter<TextValueItem> myaAdapter = new ArrayAdapter<TextValueItem>(this, android.R.layout.simple_spinner_item, logdata.GetLogTypeList());
        //Spinner mySpinner = (Spinner) this.findViewById(R.id.spinlogtype);
        //mySpinner.setAdapter(myaAdapter);


        final Spinner mySpinner = (Spinner) findViewById(R.id.spinlogtype);

        List<TextValueItem> lst   = logdata.GetLogTypeList();

        //ArrayAdapter<TextValueItem> myaAdapter = new ArrayAdapter<TextValueItem>(this, android.R.layout.simple_spinner_item, lst);
        ArrayAdapter<TextValueItem> myaAdapter = new ArrayAdapter<TextValueItem>(this, R.layout.spinner_list, lst);
        mySpinner.setAdapter(myaAdapter);

        mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*
                 * ids是刚刚新建的list里面的ID
                 */
                String ids = ((TextValueItem) mySpinner.getSelectedItem()).GetValue();
                System.out.println(ids);
                Toast.makeText(getApplicationContext(), String.valueOf(ids), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_data, menu);
        return true;
    }

    private OnClickListener OnBtnClickListener = new OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgBtnBack:

                    finish();
                    break;
                default:
                    break;
            }
        }
    };

}
