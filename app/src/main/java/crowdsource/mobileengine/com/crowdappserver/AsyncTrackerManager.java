package crowdsource.mobileengine.com.crowdappserver;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by praveen on 4/28/2016.
 */
public class AsyncTrackerManager extends AsyncTask<Void,Void,Void> {
    private Context mContext;
    private String[] mMessage;
    private String mCode;
    private String mPhone;
    private String mName;
    private String mBusNo;
    private String mLat;
    private String mLng;
    private String mSpeed;


    public AsyncTrackerManager(String code, String phone, String name, String busNo, String lat, String lng, String speed) {
      // mMessage = message;
        mCode = code;
        mPhone = phone;
        mName = name;
        mBusNo = busNo;
        mLat = lat;
        mLng = lng;
        mSpeed = speed;
    }

    @Override
    protected Void doInBackground(Void... params) {

        String response = null;
        BufferedReader bufferedReader = null;
        InputStream inputStreamReader;
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        String STRING_URL = "http://192.168.2.137/EBusTrackingSystem/api/DailyBusActivities";
       /* POST http://localhost/EBusTrackingSystem/api/DailyBusActivities HTTP/1.1
        User-Agent: Fiddler
        Content-Type: application/json
        Host: localhost

        {
        "busNo":"RC10-RED",
        "phoneNumber":3104677032,
        "latitude":"51.5034070",
        "longitude":"-0.1276520"
        }
        */
        HashMap<String, String> postPARAMS = new HashMap<String, String>();
        postPARAMS.put("busNo", mBusNo);
        postPARAMS.put("phoneNumber", mPhone);
        postPARAMS.put("latitude", mLat);
        postPARAMS.put("longitude", mLng);
        postPARAMS.put("speed", mSpeed);

        try {

            URL url = new URL(STRING_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //con.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(1000000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(1500000 /* milliseconds */);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.addRequestProperty("Referer", "http://www.msk.com");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            // Post parm


            if (params != null && postPARAMS.size() > 0) {
                httpURLConnection.setDoOutput(true);

                String paramsQ = getQuery(postPARAMS);
                httpURLConnection.setRequestProperty("Content-Length", "" +
                        Integer.toString(paramsQ.getBytes().length));
                DataOutputStream wr = new DataOutputStream(
                        httpURLConnection.getOutputStream());
                wr.writeBytes(paramsQ);
                wr.flush();
                wr.close();
                //os.close();
            }
            // Start the query
            httpURLConnection.connect();
            // Check if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

            StringBuilder sb = new StringBuilder();
            inputStreamReader = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));

            if (bufferedReader == null) {
                return null;
            }
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            response = buffer.toString();
            Log.e("connectAndGetResponse", response);

        } catch (FileNotFoundException e) {
            Log.e("AsyncBusDetail", "FileNotFoundException" + e.getMessage());
        } catch (IOException ioException) {
            Log.e("AsyncBusDetail", "error from doInbackground Task " + ioException.getMessage());
            ioException.printStackTrace();

            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("AsyncBusDetail", "Error closing buferReader", e);
                }
            }
        }

     /*   try {

            URL url = new URL("http://192.168.2.137/EBusTrackingSystem/api/DailyBusActivities");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(1000000 *//* milliseconds *//*);
            con.setConnectTimeout(150000 *//* milliseconds *//*);

            con.setRequestMethod("POST");
            con.addRequestProperty("Referer", "http://www.msk.com");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoInput(true);


            // Post parm


            if (params != null && postPARAMS.size() > 0) {
                con.setDoOutput(true);

                String paramsQ = getQuery(postPARAMS);
                con.setRequestProperty("Content-Length", "" +
                        Integer.toString(paramsQ.getBytes().length));

                DataOutputStream wr = new DataOutputStream(
                        con.getOutputStream());
                wr.writeBytes(paramsQ);
                wr.flush();
                wr.close();
                //os.close();
            }
            // Start the query
            con.connect();
            // Check if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

            StringBuilder sb = new StringBuilder();

            // Read results from the query
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"));
            //	String payload = reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            response = sb.toString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("connectAndGetResponse", e.getMessage());
        }*/

        return null;
    }




    private static String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Set<String> keys = params.keySet();
        for (String key : keys)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get(key), "UTF-8"));
        }

        return result.toString();
    }


}
