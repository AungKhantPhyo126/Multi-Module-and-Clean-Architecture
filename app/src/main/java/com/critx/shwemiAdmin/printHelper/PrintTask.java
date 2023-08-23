package com.critx.shwemiAdmin.printHelper;

/**
 * Created by gbs.damian.zydczak on 5/24/2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class PrintTask extends AsyncTask<String, Void, Map<String,String>>
{
    private Exception exception;
    AlertDialog alertDialog;
    public static HashMap<String,String> variableMap=null;
    public static Map<String,String> resultFinal=null;
    public Activity act =null;
    public static boolean isPortSet=false;

     public PrintTask(Activity activity)
     {
       this.act=activity;
     }

    protected Map<String,String> doInBackground(String... urls)
    {
        try
        {
            //Map<String,String> mResult = HttpClient.get("http://localhost:8080/Printer/SendRawData", variableMap);  // http://localhost:8080/Printer/Port
            Map<String,String> mResult = null;

            if(variableMap==null)
            {
                mResult = HttpClient.get("http://localhost:8080/Printer/Port", variableMap);
            }
            else
            {
                if(!isPortSet)
                    mResult = HttpClient.get("http://localhost:8080/Printer/SendCommand", variableMap);
                else
                    mResult = HttpClient.get("http://localhost:8080/Printer/SetPort", variableMap);
            }

            //Map<String,String> mResult = HttpClient.get("http://localhost:8080/Printer/SendRawData?__send_data=05&__encoding=hex", "");
            //Map<String,String> mResult = HttpClient.get("http://localhost:8080/Format/Print", variableMap);

            return mResult;
        } catch (HttpClient.HttpClientException e) {
            //TODO Auto-generated catch block
            Log.e("Sending of The data...",e.getMessage());
        } catch (Exception e) {
            this.exception = e;
            Log.e("Sending of The Data...",e.getMessage());
            return null;
        }
        return null;
    }


    protected void onPostExecute( Map<String,String> result) {

        try {
            // TODO: check this.exception
            // TODO: do something with the feed
            for (Map.Entry<String, String> entry : result.entrySet()) {

                Log.i("SmaPriReply", "[" + entry.getKey() + "] "
                        + entry.getValue());
            }
            if (result.containsKey("result") && result.get("result").equals("NG")) {
                alertDialog.setTitle("Print Failed!");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setMessage("Error Message : " + result.get("message"));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Write your code here to execute after dialog closed
                    }
                });
                alertDialog.show();
            }
            else if ((result.containsKey("result") && result.get("result").equals("OK"))&&(result.containsKey("function") && result.get("function").equals("/Printer/Port")))
            {
                resultFinal=result;
            }
            else if ((result.containsKey("result") && result.get("result").equals("OK"))&&(result.containsKey("function") && result.get("function").equals("/Printer/SetPort")))
            {
                Toast toast = Toast.makeText(act, "Data Set Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(act, "Data Sent Successfully.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(act, "Please Check if The SmartPri/Printer are connected and running", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}