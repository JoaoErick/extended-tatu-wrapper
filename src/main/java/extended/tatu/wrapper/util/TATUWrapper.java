package extended.tatu.wrapper.util;

import extended.tatu.wrapper.enums.ExtendedTATUMethods;
import java.util.Iterator;
import org.json.JSONObject;

import org.json.JSONException;

public final class TATUWrapper {

    public static final String TOPIC_BASE = "dev/";
    public static final String TOPIC_RESPONSE = "/RES";

    public static String buildTATUFlowInfoMessage(String sensorId, int collectSeconds, int publishSeconds) {
        return buildTATUFlow(sensorId, collectSeconds, publishSeconds, "INFO ");
    }

    public static String buildTATUFlowValueMessage(String sensorId, int collectSeconds, int publishSeconds) {
        return buildTATUFlow(sensorId, collectSeconds, publishSeconds, "VALUE ");
    }

    private static String buildTATUFlow(String sensorId, int collectSeconds, int publishSeconds, String command) {
        return new StringBuilder()
                .append("FLOW ")
                .append(command)
                .append(sensorId)
                .append(" {\"collect\":")
                .append(collectSeconds)
                .append(",\"publish\":")
                .append(publishSeconds)
                .append("}").toString();
    }

    public static String buildTATUTopic(String deviceName) {
        return new StringBuilder()
                .append(TOPIC_BASE)
                .append(deviceName)
                .toString();
    }

    public static String buildTATUResponseTopic(String deviceName) {
        return new StringBuilder()
                .append(TOPIC_BASE)
                .append(deviceName)
                .append(TOPIC_RESPONSE)
                .toString();
    }

    public static String buildGetMessageResponse(String deviceName, String key, Object value) {
        JSONObject response = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();

        header.put("NAME", deviceName);
        header.put("TIMESTAMP", System.currentTimeMillis());
        body.put(key, value);
        response.put("METHOD", "GET");
        response.put("CODE", "POST");
        response.put("HEADER", header);
        response.put("BODY", body);

        return response.toString();
    }

    public static String buildFlowMessageResponse(String deviceName, String sensorName, int publish, int collect, Object[] values) {
        JSONObject response = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONObject flow = new JSONObject();

        flow.put("collect", collect);
        flow.put("publish", publish);

        header.put("NAME", deviceName);
        header.put("TIMESTAMP", System.currentTimeMillis());
        body.put(sensorName, values);
        body.put("FLOW", flow);

        response.put("METHOD", "FLOW");
        response.put("CODE", "POST");
        response.put("HEADER", header);
        response.put("BODY", body);

        return response.toString();
    }

    public static boolean isTATUResponse(String message) {
        return isValidMessage(message)
                && isValidTATUAnswer(message);
    }

    public static boolean isValidMessage(String message) {
        return !(message == null || message.isEmpty() || message.length() <= 10);
    }

    //{"CODE":"POST","METHOD":"FLOW","HEADER":{"NAME":"ufbaino04"},"BODY":{"temperatureSensor":["36","26"],"FLOW":{"publish":10000,"collect":5000}}}
    public static boolean isValidTATUAnswer(String answer) {
        try {
            JSONObject json = new JSONObject(answer);
            return ((json.get("CODE").toString().contentEquals("POST"))
                    && json.getJSONObject("BODY") != null);
        } catch (JSONException e) {
            return false;
        }
    }

    public static String getMethod(String message) {
        if (isValidMessage(message)) {
            if (isValidTATUAnswer(message)) {
                return new JSONObject(message).getString("METHOD");
            } else {
                return message.split(" ")[0];
            }
        }
        return ExtendedTATUMethods.INVALID.name();
    }

    public static String getDeviceIdByTATUAnswer(String answer) {
        JSONObject json = new JSONObject(answer);
        return json.getJSONObject("HEADER").getString("NAME");
    }

    public static String getSensorIdByTATUAnswer(String answer) {
        JSONObject json = new JSONObject(answer);
        Iterator<?> keys = json.getJSONObject("BODY").keys();
        String sensorId = keys.next().toString();
        while (sensorId.contentEquals("FLOW")) {
            sensorId = keys.next().toString();
        }
        return sensorId;
    }

    public static String getSensorIdByTATURequest(String request) {
        return request.split(" ")[2];
    }

    public static String getAttributeByTATURequest(String request) {
        return request.split(" ")[1];
    }

    public static Long getMessageTimestamp(String message) {
        if(message == null || message.isEmpty()|| !message.contains("TIMESTAMP")){
            return 0L;
        }
        return new JSONObject(message).getJSONObject("HEADER").getLong("TIMESTAMP");
    }

}
