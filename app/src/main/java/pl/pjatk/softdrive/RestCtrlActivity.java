package pl.pjatk.softdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pl.pjatk.softdrive.rest.RestScan2dService;

public class RestCtrlActivity extends AppCompatActivity {

    Button btnHitDist;
    Button btnHitSpace;
    TextView txtJsonDist;
    TextView txtJsonSpace;
    ProgressDialog pd;

    boolean isDistanceActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl_rest);

        btnHitDist = (Button) findViewById(R.id.btnHitDist);
        btnHitSpace = (Button) findViewById(R.id.btnHitSpace);
        txtJsonDist = (TextView) findViewById(R.id.JsonDistTxt);
        txtJsonSpace = (TextView) findViewById(R.id.JsonSpaceTxt);


        // https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android/35916046
        btnHitDist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                isDistanceActive = true;



                try {
                    byte[] addresses = getGatewayIp().getAddress();
                    for (byte b :
                            addresses) {
                        System.out.println(Integer.toBinaryString(b));
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println(getGatewayIp().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                try {
                    Process process = Runtime.getRuntime().exec("ip addr show swlan0");
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String result = in.lines().collect(Collectors.joining());
                    System.out.println(result);
                    String subStr = "inet 192.168.";
                    int ipEnd = result.lastIndexOf(subStr);
                    System.out.println(result.substring(ipEnd + subStr.length()));

                    String cutResult = result.substring(ipEnd + subStr.length());

                    String regex = "^\\d{1,3}";
                    Pattern r = Pattern.compile(regex);
                    Matcher m = r.matcher(cutResult);

                    if(m.find())
                    System.out.println("regex " + m.group(0));

                    String ip3Byte = m.group(0);

                } catch (IOException e) {
                    System.out.println("ERROR");
                    e.printStackTrace();
                }

                Intent restService = new Intent(getApplicationContext(), RestScan2dService.class);
                startService(restService);

                //new JsonTask().execute("http://192.168.43.21:8080/api/distance/");
            }
        });

        btnHitSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDistanceActive = false;
                new JsonTask().execute("http://192.168.43.134:5000/api/rplidar");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //https://developer.android.com/training/system-ui/navigation
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public InetAddress getGatewayIp() throws UnknownHostException {
        WifiManager manager;
        DhcpInfo dhcp;
            manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            dhcp = manager.getDhcpInfo();
            final String address = Formatter.formatIpAddress(dhcp.gateway);
                InetAddress serverIP = null;
                try {
                    serverIP  = InetAddress.getByName(address);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }

                return serverIP ;

    }

    // https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android/35916046
    private class JsonTask extends AsyncTask<String, String, String> {



        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(RestCtrlActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            if (isDistanceActive)
                txtJsonDist.setText(result);
            else
                txtJsonSpace.setText(result);
        }
    }

}


