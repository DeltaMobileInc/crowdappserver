package crowdsource.mobileengine.com.crowdappserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageButton start , stop ;

    private SmsMessage[] message;
    private TextView statustext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statustext = (TextView)findViewById(R.id.statustext);
        start = (ImageButton)findViewById(R.id.start_button);
        stop = (ImageButton)findViewById(R.id.stop_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                statustext.setText("Server is running ... ");
                Constants.SERVER_START_STATUS = true;
                //SmsParser.parserSMS(getApplicationContext(),"1/1310467032/praveen");
               /* AsyncBusDetailRequestManager runinbackground = new AsyncBusDetailRequestManager(getApplicationContext());
                runinbackground.execute();*/
        }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                statustext.setText("Please click on start server ");
                Constants.SERVER_START_STATUS = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logs:
                startActivity(new Intent(this,LogActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
