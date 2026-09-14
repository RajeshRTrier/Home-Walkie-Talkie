package com.homewalkietalkie;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView status;
    private TextView activity;
    private Button talk;
    private boolean paired = false;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(18), dp(20), dp(18));
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Home Walkie-Talkie");
        title.setTextSize(28);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);

        root.addView(title,
                new LinearLayout.LayoutParams(-1, dp(55)));

        status = new TextView(this);
        status.setText("● Not paired");
        status.setTextSize(16);
        status.setTextColor(Color.DKGRAY);
        status.setGravity(Gravity.CENTER);

        root.addView(status,
                new LinearLayout.LayoutParams(-1, dp(42)));

        EditText code = new EditText(this);
        code.setHint("6-digit pairing code");
        code.setInputType(2);
        code.setSingleLine(true);

        root.addView(code,
                new LinearLayout.LayoutParams(-1, dp(55)));

        Button pair = new Button(this);
        pair.setText("PAIR PHONES");

        root.addView(pair,
                new LinearLayout.LayoutParams(-1, dp(52)));

        TextView spacer = new TextView(this);

        root.addView(spacer,
                new LinearLayout.LayoutParams(1, dp(14)));

        talk = new Button(this);
        talk.setText("🎙  HOLD TO TALK");
        talk.setTextSize(21);

        root.addView(talk,
                new LinearLayout.LayoutParams(-1, dp(150)));

        Button help = new Button(this);
        help.setText("🆘  HELP");
        help.setTextSize(21);

        LinearLayout.LayoutParams helpParams =
                new LinearLayout.LayoutParams(-1, dp(90));
        helpParams.topMargin = dp(12);

        root.addView(help, helpParams);

        activity = new TextView(this);
        activity.setText("Ready. Pair the phones to begin.");
        activity.setTextSize(15);
        activity.setTextColor(Color.DKGRAY);
        activity.setPadding(0, dp(18), 0, 0);

        root.addView(activity,
                new LinearLayout.LayoutParams(-1, dp(70)));

        setContentView(root);

        pair.setOnClickListener(v -> {
            String entered =
                    code.getText().toString().replaceAll("\\D", "");

            if (entered.length() != 6) {
                activity.setText(
                        "Enter a 6-digit pairing code.");
                return;
            }

            paired = true;
            status.setText("● Paired (prototype)");
            activity.setText(
                    "Paired successfully. Voice connection will be added next.");
        });

        talk.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (!paired) {
                    activity.setText(
                            "Pair the phones first.");
                    return true;
                }

                if (checkSelfPermission(
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{
                                    Manifest.permission.RECORD_AUDIO
                            }, 10);
                }

                talk.setText("🔴  RELEASE TO STOP");
                activity.setText(
                        "Transmitting… (network voice comes next)");

                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {

                talk.setText("🎙  HOLD TO TALK");
                activity.setText("Transmission ended.");

                return true;
            }

            return true;
        });

        help.setOnClickListener(v -> {

            if (!paired) {
                activity.setText(
                        "Pair the phones first.");
                return;
            }

            Toast.makeText(
                    this,
                    "HELP alert queued",
                    Toast.LENGTH_SHORT
            ).show();

            activity.setText(
                    "🆘 HELP alert queued.");
        });
    }
}
