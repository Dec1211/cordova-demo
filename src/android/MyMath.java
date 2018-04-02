package SimpleMath;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;

/**
 * This class echoes a string called from JavaScript.
 */
public class MyMath extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("Plus")) {
            int A = args.getInt(0);
            int B = args.getInt(1);
            this.plus(A, B,callbackContext);
            return true;
        }else if(action.equals("Minus")){
            int A = args.getInt(0);
            int B = args.getInt(1);
            this.minus(A,B,callbackContext);
        }else if(action.equals("Address")){
            this.address(callbackContext);
        }
        return false;
    }

    private void plus(int A,int B,CallbackContext callbackContext) {
        int ret = A+B;
        if(ret<100){
            callbackContext.success(ret);
        }else {
            callbackContext.error("A+B must be smaller than 100");
        }
    }

    private void minus(int A,int B,CallbackContext callbackContext){
        int ret = A-B;
        if(ret>0){
            callbackContext.success(ret);
        }else {
            callbackContext.error("A-B must be bigger than B");
        }
    }

    private void address(CallbackContext callbackContext){
        try{
            String addressStr = getMac();
            callbackContext.success(addressStr);
        }catch (Exception ex){
            callbackContext.error("error");
        }
    }

    public String getMac(){
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        return macSerial;
    }

    public String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }
}
