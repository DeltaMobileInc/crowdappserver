package crowdsource.mobileengine.com.crowdappserver;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
public class AsyncBusDetailRequestManager extends AsyncTask<Void, Void, HashMap<String, String>> {
    private Context mContext;

    private String mCode;
    private String mPhone;
    private String mName;
    private String mBusNo;
    private String mLat;
    private String mLng;

    public AsyncBusDetailRequestManager(String code, String phone, String name, String busNo, String lat, String lng) {
        mCode = code;
        mPhone = phone;
        mName = name;
        mBusNo = busNo;
        mLat = lat;
        mLng = lng;

    }


    @Override
    protected HashMap<String, String> doInBackground(Void... params) {
        HashMap<String, String> responseMap = new HashMap<>();
        String response = null;
        BufferedReader bufferedReader = null;
        InputStream inputStreamReader;
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        String STRING_URL = "http://192.168.2.137/EBusTrackingSystem/api/TrackingRequests";
              /*  POST http://localhost/EBusTrackingSystem/api/TrackingRequests HTTP/1.1
            User-Agent: Fiddler
            Content-Type: application/json
            Host: localhost
            Content-Length: 92

            {
                    "busNo":"RC-10",
                    "PhoneNumber":3104677032,
                    "latitude":"51.5034070",
                    "longitude":"-0.1275920"
            }
            */

        HashMap<String, String> postPARAMS = new HashMap<String, String>();
        postPARAMS.put("busNo", mBusNo);
        postPARAMS.put("phoneNumber", mPhone);
        postPARAMS.put("latitude", mLat);
        postPARAMS.put("longitude", mLng);


        try {

            URL url = new URL(STRING_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
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
            responseMap = parseJsonResponse(response);
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

        return responseMap;
    }


    @Override
    protected void onPostExecute(HashMap<String, String> hashMap) {
        SmsManager smsManager = SmsManager.getDefault();

        String message = isNotNull(mCode)+"/" + isNotNull(hashMap.get(Constants.JSON_STRING_PHONENO))  + "/" + isNotNull(hashMap.get(Constants.JSON_STRING_BUS_NO))
                + "/" +isNotNull(hashMap.get(Constants.JSON_STRING_EXPECTED_DATE)+"/"+isNotNull(hashMap.get(Constants.JSON_STRING_ADVERTISEMENT))) ;
        smsManager.sendTextMessage(hashMap.get(Constants.JSON_STRING_PHONENO), null, message, null, null);
    }

    private HashMap<String, String> parseJsonResponse(String response) {
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            JSONObject resultObject = new JSONObject(response);
            result.put(Constants.JSON_STRING_BUS_NO, resultObject.optString(Constants.JSON_STRING_BUS_NO));
            result.put(Constants.JSON_STRING_PHONENO, resultObject.optString(Constants.JSON_STRING_PHONENO));
            result.put(Constants.JSON_STRING_EXPECTED_DATE, resultObject.optString(Constants.JSON_STRING_EXPECTED_DATE));
            result.put(Constants.JSON_STRING_ADVERTISEMENT, resultObject.optString(Constants.JSON_STRING_ADVERTISEMENT));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }

    private String isNotNull(String string)
    {
        if (!TextUtils.isEmpty(string))
        {
            return string;
        }
            return "NA";
    }

/*    public String getRuntimeInHours(String time) {
        if (!TextUtils.isEmpty(time)) {
            Log.d("getRuntimeInHours", time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm");
            try {
                Date date = dateFormat.parse(time);

                String out = dateFormat2.f;
                Log.e("Time", out);
            } catch (ParseException e) {
            }

             return Integer.toString(Integer.parseInt(time)/60)+" hrs "+Integer.toString(Integer.parseInt(time)%60)+" mins";
        } else {
            return "NA";
        }

    }*/


    private static String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Set<String> keys = params.keySet();
        for (String key : keys) {
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
