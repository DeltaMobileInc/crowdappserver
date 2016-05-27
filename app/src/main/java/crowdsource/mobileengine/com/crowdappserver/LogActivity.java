package crowdsource.mobileengine.com.crowdappserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

    private TextView logText;
    private String logTrace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        logText = (TextView)findViewById(R.id.logtextView);
        logTrace = SmsParser.readLogFile(getApplicationContext());
        logText.setText(logTrace);

    }
}
