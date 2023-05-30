package com.adbc.qtracker_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.adbc.tracker.ADBCTracker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.evt1);
        btn2 = findViewById(R.id.evt2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        // 앱 실행 시 1회만 호출 ( MainActivity or Application )
        ADBCTracker.init(this, new ADBCTracker.InitListener() {
            @Override
            public void onResult(boolean result, @NonNull String errMsg) {

                if(!result) {
                    Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.evt1:

                // 트래킹 하고자 하는 시점에 이벤트명으로 구분하여 호출
                ADBCTracker.sendEvent(MainActivity.this, "custom_evt");
                break;

            case R.id.evt2:
                ADBCTracker.sendPurchase(MainActivity.this, 1, 100L, "prd_1");
                break;

            default:
                break;
        }
    }
}
