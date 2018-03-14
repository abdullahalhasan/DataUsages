package com.appeteria.introsliderexample;

import android.app.Activity;
import android.content.SharedPreferences;
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


    //Strings for Shared Preference;

    public static long latest_stx;
    long latest_srx;
    public static long latest_smtx;
    long latest_smrx;
    public static long latest_swtx;
    long latest_swrx;
    long previous_stx;
    long previous_srx;
    long previous_smtx;
    long previous_smrx;
    long previous_swtx;
    long previous_swrx;
    long delta_stx;
    long delta_srx;
    long delta_smtx;
    long delta_smrx;
    long delta_swtx;
    long delta_swrx;

    //Created Class TrafficSnapshot
    TrafficSnapshot latest;
    TrafficSnapshot previous;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

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

        editor = getSharedPreferences("DataUsageInfo",MODE_PRIVATE).edit();
        sp = getSharedPreferences("DataUsagesInfo",MODE_PRIVATE);

    }

    public void takeSnapshot(View view) {
        previous = latest;
        latest = new TrafficSnapshot(this);

        latest_srx = latest.device.rx;
        latest_stx = latest.device.tx;
        latest_smtx = latest.device.mtx;
        latest_smrx = latest.device.mrx;
        latest_swtx = latest.device.wtx;
        latest_swrx = latest.device.wrx;

        //new
        latest_stx = latest_stx + latest_srx;
        latest_smtx = latest_smtx + latest_smrx;
        latest_swtx = latest_swtx + latest_swrx;

        //Save in Shared Preferences
        editor.putLong("latest_srx",latest_srx);
        editor.putLong("latest_stx",latest_stx);
        editor.putLong("latest_smrx",latest_smrx);
        editor.putLong("latest_smtx",latest_smtx);
        editor.putLong("latest_swrx",latest_swrx);
        editor.putLong("latest_swtx",latest_swtx);
        editor.apply();
        editor.commit();
        //Total Data Usages
        if (latest_srx != 0) {
            latest_rx.setText(String.valueOf(sp.getFloat("latext_srx", latest_srx)));
            latest_tx.setText(String.valueOf(sp.getFloat("latext_stx", latest_stx)));
        }
        //Mobile Data Usages
        latest_mrx.setText(String.valueOf(sp.getFloat("latext_smrx", latest_smrx)));
        latest_mtx.setText(String.valueOf(sp.getFloat("latext_smtx", latest_smtx)));

        //Wi-Fi Data Usages
        latest_wrx.setText(String.valueOf(latest_swrx));
        latest_wtx.setText(String.valueOf(latest_swtx));

        if (previous != null) {

            previous_stx = previous.device.tx;
            previous_srx = previous.device.rx;
            previous_smtx = previous.device.mtx;
            previous_smrx = previous.device.mrx;
            previous_swtx = previous_smtx;
            previous_swrx = previous_smrx;
            delta_stx = latest.device.tx - previous.device.tx;
            delta_srx = latest.device.rx - previous.device.rx;
            delta_smtx = latest.device.mtx - previous.device.mtx;
            delta_smrx = latest.device.mrx - previous.device.mrx;
            delta_swtx = latest.device.wtx - previous.device.wtx;
            delta_swrx = latest.device.wrx - previous.device.wrx;

            //Total Data Usages
            previous_rx.setText(String.valueOf(previous_srx));
            previous_tx.setText(String.valueOf(previous_stx));

            delta_rx.setText(String.valueOf(delta_srx));
            delta_tx.setText(String.valueOf(delta_stx));

            //Mobile Data Usages
            previous_mrx.setText(String.valueOf(previous_smrx));
            previous_mtx.setText(String.valueOf(previous_smtx));

            delta_mrx.setText(String.valueOf(delta_smrx));
            delta_mtx.setText(String.valueOf(delta_smtx));

            //Wi-Fi Data Usages
            previous_wrx.setText(String.valueOf(previous_swrx));
            previous_wtx.setText(String.valueOf(previous_swtx));

            delta_wrx.setText(String.valueOf(delta_swrx));
            delta_wtx.setText(String.valueOf(delta_swtx));
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

    private void saveInSP() {

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
