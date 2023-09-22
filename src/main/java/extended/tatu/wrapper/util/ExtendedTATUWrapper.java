package extended.tatu.wrapper.util;

import extended.tatu.wrapper.model.Device;
import org.json.JSONObject;

/**
 *
 * @author Uellington Damasceno
 */
public final class ExtendedTATUWrapper {

    public static final String TOPIC_BASE = "dev/";
    public static final String TOPIC_RESPONSE = "/RES";

    //"dev/CONNECTIONS"
    public static String getConnectionTopic() {
        return new StringBuilder()
                .append(TOPIC_BASE)
                .append("CONNECTIONS")
                .toString();
    }

    //"dev/CONNECTIONS/RES"
    public static String getConnectionTopicResponse() {
        return getConnectionTopic().concat(TOPIC_RESPONSE);
    }
    
    public static String buildConnectMessage(Device device, String ip, Double timeout, JSONObject ariesConnection){
        JSONObject requestBody = new JSONObject();
        JSONObject header = new JSONObject();

        requestBody.put("TIME_OUT", timeout);
        header.put("NAME", device.getId());
        header.put("SOURCE_IP", ip);
        header.put("TIMESTAMP", System.currentTimeMillis());
        requestBody.put("HEADER", header);
        requestBody.put("DEVICE", DeviceWrapper.toJSONObject(device));
        requestBody.put("ARIES_CONNECTION", ariesConnection);
        return new StringBuilder()
                .append("CONNECT VALUE BROKER ")
                .append(requestBody.toString())
                .toString();
    }
    
    public static String buildConnectMessage(Device device, String ip, Double timeout){
        JSONObject requestBody = new JSONObject();
        JSONObject header = new JSONObject();

        requestBody.put("TIME_OUT", timeout);
        header.put("NAME", device.getId());
        header.put("SOURCE_IP", ip);
        header.put("TIMESTAMP", System.currentTimeMillis());
        requestBody.put("HEADER", header);
        requestBody.put("DEVICE", DeviceWrapper.toJSONObject(device));
        return new StringBuilder()
                .append("CONNECT VALUE BROKER ")
                .append(requestBody.toString())
                .toString();
    }

    //CONNECT VALUE BROKER {"HEADER":{"NAME":String}, DEVICE:{id:String, sensors[]:Sensor, latitude:long, longitude:long} "TIME_OUT":Double}
    public static String buildConnectMessage(Device device, Double timeOut) {
        JSONObject requestBody = new JSONObject();
        JSONObject header = new JSONObject();

        requestBody.put("TIME_OUT", timeOut);
        header.put("NAME", device.getId());
        header.put("TIMESTAMP", System.currentTimeMillis());
        requestBody.put("HEADER", header);
        requestBody.put("DEVICE", DeviceWrapper.toJSONObject(device));
        return new StringBuilder()
                .append("CONNECT VALUE BROKER ")
                .append(requestBody.toString())
                .toString();
    }

    //{"CODE":"POST", "METHOD":"CONNACK", "HEADER":{"NAME":String}, "BODY":{"NEW_NAME":String, "CAN_CONNECT":Boolean}}
    public static String buildConnackMessage(String deviceName, String newDeviceName, boolean sucess) {
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONObject response = new JSONObject();

        header.put("NAME", deviceName);
        body.put("NEW_NAME", newDeviceName);
        body.put("CAN_CONNECT", sucess);

        response.put("METHOD", "CONNACK");
        response.put("CODE", "POST");
        response.put("HEADER", header);
        response.put("BODY", body);

        return response.toString();
    }

}
