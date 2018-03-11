package com.appeteria.introsliderexample;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class TrafficMonitorActivity extends Activity {

    //Declared  TextViews For Total Data Usages
    TextView latest_rx;
    TextView latest_tx;
    TextView previous_rx;
    TextView previous_tx;
    TextView delta_rx;
    TextView delta_tx;

    //Declared  TextViews For Mobile Data Usages
    TextView latest_mrx;
    TextView latest_mtx;
    TextView previous_mrx;
    TextView previous_mtx;
    TextView delta_mrx;
    TextView delta_mtx;

    //Declared  TextViews For Wi-Fi Data Usages
    TextView latest_wrx;
    TextView latest_wtx;
    TextView previous_wrx;
    TextView previous_wtx;
    TextView delta_wrx;
    TextView delta_wtx;

    //Created Class TrafficSnapshot
    TrafficSnapshot latest;
    TrafficSnapshot previous;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_monitor);

        //Initialized TextViews For Total Data Usages
        latest_rx = (TextView) findViewById(R.id.latest_rx);
        latest_tx = (TextView) findViewById(R.id.latest_tx);
        previous_rx = (TextView) findViewById(R.id.previous_rx);
        previous_tx = (TextView) findViewById(R.id.previous_tx);
        delta_rx = (TextView) findViewById(R.id.delta_rx);
        delta_tx = (TextView) findViewById(R.id.delta_tx);

        //Initialized TextViews For Mobile Data Usages
        latest_mrx = (TextView) findViewById(R.id.latest_mrx);
        latest_mtx = (TextView) findViewById(R.id.latest_mtx);
        previous_mrx = (TextView) findViewById(R.id.previous_mrx);
        previous_mtx = (TextView) findViewById(R.id.previous_mtx);
        delta_mrx = (TextView) findViewById(R.id.delta_mrx);
        delta_mtx = (TextView) findViewById(R.id.delta_mtx);

        //Initialized TextViews For Wi-Fi Data Usages
        latest_wrx = (TextView) findViewById(R.id.latest_wrx);
        latest_wtx = (TextView) findViewById(R.id.latest_wtx);
        previous_wrx = (TextView) findViewById(R.id.previous_wrx);
        previous_wtx = (TextView) findViewById(R.id.previous_wtx);
        delta_wrx = (TextView) findViewById(R.id.delta_wrx);
        delta_wtx = (TextView) findViewById(R.id.delta_wtx);
        //takeSnapshot(null);

    }

    public void takeSnapshot(View view) {
        previous = latest;
        latest = new TrafficSnapshot(this);

        //Total Data Usages
        latest_rx.setText(String.valueOf(latest.device.rx));
        latest_tx.setText(String.valueOf(latest.device.tx));

        //Mobile Data Usages
        latest_mrx.setText(String.valueOf(latest.device.mrx));
        latest_mtx.setText(String.valueOf(latest.device.mtx));

        //Wi-Fi Data Usages
        latest_wrx.setText(String.valueOf(latest.device.wrx));
        latest_wtx.setText(String.valueOf(latest.device.wtx));

        if (previous != null) {

            //Total Data Usages
            previous_rx.setText(String.valueOf(previous.device.rx));
            previous_tx.setText(String.valueOf(previous.device.tx));

            delta_rx.setText(String.valueOf(latest.device.rx - previous.device.rx));
            delta_tx.setText(String.valueOf(latest.device.tx - previous.device.tx));

            //Mobile Data Usages
            previous_mrx.setText(String.valueOf(previous.device.mrx));
            previous_mtx.setText(String.valueOf(previous.device.mtx));

            delta_mrx.setText(String.valueOf(latest.device.mrx - previous.device.mrx));
            delta_mtx.setText(String.valueOf(latest.device.mtx - previous.device.mtx));

            //Wi-Fi Data Usages
            previous_wrx.setText(String.valueOf(previous.device.wrx));
            previous_wtx.setText(String.valueOf(previous.device.wtx));

            delta_wrx.setText(String.valueOf(latest.device.wrx - previous.device.wrx));
            delta_wtx.setText(String.valueOf(latest.device.wtx - previous.device.wtx));


        }

        ArrayList<String> log = new ArrayList<>();
        HashSet<Integer> intersection = new HashSet<Integer>(latest.apps.keySet());

        if (previous != null) {
            intersection.retainAll(previous.apps.keySet());
        }

        for (Integer uid : intersection) {
            TrafficRecord latest_rec = latest.apps.get(uid);
            TrafficRecord previous_rec = (previous == null ? null : previous.apps.get(uid));
            emitLog(latest_rec.tag, latest_rec,previous_rec,log);
        }

        Collections.sort(log);

        for (String row : log) {
            Log.d("Traffic Monitor", row);
        }



    }

    private void emitLog(CharSequence name, TrafficRecord latest_rec,
                         TrafficRecord previous_rec, ArrayList<String> rows) {

        if (latest_rec.rx >- 1 || latest_rec.tx >- 1) {
            StringBuilder buf = new StringBuilder(name);
            buf.append(" = ").append(String.valueOf(latest_rec.rx)).append(" Received");

            if (previous_rec != null) {
                buf.append("Delta = ").append(String.valueOf(latest_rec.rx - previous_rec.rx)).append(")");
            }

            buf.append(", ").append(String.valueOf(latest_rec.tx)).append(" Sent");

            if (previous_rec != null) {
                buf.append("Delta = ").append(String.valueOf(latest_rec.tx - previous_rec.tx)).append(")");
            }

            rows.add(buf.toString());
        }
    }
}
