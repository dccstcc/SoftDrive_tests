package pl.pjatk.softdrive.rest.controllers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FindAddressIp {

    String ip = "192.168.";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FindAddressIp() {
        ip += get3rdPartOfIp();
    }

    public String getIp() {
        return ip;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String get3rdPartOfIp() {

        String ip3Byte = "";

        try {
            //shell command for show ip
            Process process = Runtime.getRuntime().exec("ip addr show swlan0");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = in.lines().collect(Collectors.joining());

            //cut shell command show result
            String subStr = "inet 192.168.";
            int ipEnd = result.lastIndexOf(subStr);
            String cutResult = result.substring(ipEnd + subStr.length());

            //get ip from cut shell command
            String regex = "^\\d{1,3}";
            Pattern r = Pattern.compile(regex);
            Matcher m = r.matcher(cutResult);

            if(m.find()) {
                ip3Byte = m.group(0);
                ip3Byte += ".";
                return ip3Byte;
            }


        } catch (IOException e) {
            Log.d("FinAddressIp", "ERROR ip3byte");
            e.printStackTrace();
        }
        return null;
    }

}
