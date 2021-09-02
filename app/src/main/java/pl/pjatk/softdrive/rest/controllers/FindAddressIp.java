package pl.pjatk.softdrive.rest.controllers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FindAddressIp {

    private String ip = "192.168.";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FindAddressIp() {
        ip += get3rdByteOfIp();
    }

    public String getNetworkIpWithoutHost() {
        return ip;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String get3rdByteOfIp() {

        String ip3Byte = "";

        try {
            //shell command for show ip
            String result = getShellReply();

            //cut shell command show result
            String cutResult = shellReplyFilterByIp(result);

            //get ip from cut shell command
            Matcher m = regexFilterResultByIp(cutResult);

            if(m.find()) {
                return extractIpByteFromRegex(m);
            }

        } catch (IOException e) {
            Log.d("FinAddressIp", "ERROR ip3byte");
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private String extractIpByteFromRegex(Matcher m) {
        String ip3Byte;
        ip3Byte = m.group(0);
        ip3Byte += ".";
        return ip3Byte;
    }

    @NonNull
    private Matcher regexFilterResultByIp(String cutResult) {
        String regex = "^\\d{1,3}";
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(cutResult);
        return m;
    }

    @NonNull
    private String shellReplyFilterByIp(String result) {
        String subStr = "inet 192.168.";
        int ipEnd = result.lastIndexOf(subStr);
        String cutResult = result.substring(ipEnd + subStr.length());
        return cutResult;
    }

    @NonNull
    private String getShellReply() throws IOException {
        Process process = Runtime.getRuntime().exec("ip addr show swlan0");
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = in.lines().collect(Collectors.joining());
        return result;
    }

}
