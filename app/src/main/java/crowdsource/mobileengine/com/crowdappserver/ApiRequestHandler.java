package crowdsource.mobileengine.com.crowdappserver;

/**
 * Created by praveen on 4/28/2016.
 */
public class ApiRequestHandler {

    public static void registerUser(String[] mssg)
    {

    }

    public static void getBusInfo(String[] mssg)
    {
        String[] message = mssg;
        String code = mssg[0];
        String phoneno = mssg[1];
        String name = mssg[2];
        String busNo = mssg[3];
        String lat = mssg[4];
        String lng = mssg[5];



        new AsyncBusDetailRequestManager(code,phoneno,name,busNo,lat,lng).execute();
    }

    public static void busRideTracker(String[] mssg)
    {
        String[] message = mssg;
        String code = mssg[0];
        String phoneno = mssg[1];
        String name = mssg[2];
        String busNo = mssg[3];
        String lat = mssg[4];
        String lng = mssg[5];
        String speed = mssg[6];


        /*HashMap<String, String> postPARAMS = new HashMap<String, String>();
        postPARAMS.put("busNo", busNo);
        postPARAMS.put("phoneNumber", phoneno);
        postPARAMS.put("latitude", lat);
        postPARAMS.put("longitude", lng);*/


        new AsyncTrackerManager(code,phoneno,name,busNo,lat,lng,speed).execute();

    }


}
