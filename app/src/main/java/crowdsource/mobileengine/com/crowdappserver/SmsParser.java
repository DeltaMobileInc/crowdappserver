package crowdsource.mobileengine.com.crowdappserver;

import android.content.Context;
import android.text.format.Time;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by praveen on 4/28/2016.
 */
public class SmsParser {

    public static void parserSMS(Context context, String smsMssg){
        //HashMap<String,String> messageMap = new HashMap<String, String>();
        String[] decryptedStringMssg = smsMssg.split("/");
        int caseCode = Integer.parseInt(decryptedStringMssg[0]);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        switch(caseCode){
            //to register user
            case 1: ApiRequestHandler.registerUser(decryptedStringMssg);
                storeSmslogFiles(context,today.format("%k:%M:%S")+" SmsRecived : From - "+decryptedStringMssg[2]+": no - "+decryptedStringMssg[1]);
                Toast.makeText(context,"registerUser", Toast.LENGTH_SHORT).show();
                break;
            //to get information about the current bus
            case 2: ApiRequestHandler.getBusInfo(decryptedStringMssg);
                storeSmslogFiles(context,today.format("%k:%M:%S")+"SmsRecived : From - "+decryptedStringMssg[2]+" no - "+decryptedStringMssg[1]+" Bus - "+decryptedStringMssg[3]);
                Toast.makeText(context,"getBusInfo", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                break;
            //to help server track the location of the bus
            case 4:ApiRequestHandler.busRideTracker(decryptedStringMssg);
                storeSmslogFiles(context,today.format("%k:%M:%S")+"SmsRecived : From - "+decryptedStringMssg[2]+" no - "+decryptedStringMssg[1]+" Bus - "+decryptedStringMssg[3]);
                Toast.makeText(context,"context,busRideTracker", Toast.LENGTH_SHORT).show();
                break;
        }



    }

    public static void storeSmslogFiles(Context context,String text)
    {
        String filename = "crowdsourcelog";
       // File logFile = new File("sdcard/crowdsourcelog.file");
        File logFile = new File(context.getFilesDir(), filename);

        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String readLogFile(Context context)  {
        String filename = "crowdsourcelog";
        StringBuilder sb=new StringBuilder();
        File file = new File(context.getFilesDir(), filename);
        String read;
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while((read=br.readLine()) != null) {
                //System.out.println(read);
                sb.append(System.getProperty("line.separator"));
                sb.append(read);
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





        return sb.toString();
    }

}
