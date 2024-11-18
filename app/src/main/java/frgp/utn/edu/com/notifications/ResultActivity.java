package frgp.utn.edu.com.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import frgp.utn.edu.com.R;

public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String message = getIntent().getStringExtra(CommonConstants.EXTRA_MESSAGE);
        TextView text = (TextView) findViewById(R.id.result_message);
        text.setText(message);
    }

    public void onSnoozeClick(View v) {
        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        intent.setAction(CommonConstants.ACTION_SNOOZE);
        startService(intent);
        finish();
    }

    public void onDismissClick(View v) {
        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        intent.setAction(CommonConstants.ACTION_DISMISS);
        startService(intent);
        finish();
    }
}
