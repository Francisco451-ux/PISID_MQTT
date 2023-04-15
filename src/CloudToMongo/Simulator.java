package CloudToMongo;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class Simulator extends Thread {

    private static boolean isConnectedTemp = false;
    private static boolean isConnectedMov = false;
    private boolean entrou = false;

    public Simulator() {
        try {
            loadProperties();

        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    static String mongo_user = new String();
    static String mongo_password = new String();
    static String mongo_address = new String();
    static String cloud_server = new String();
    static String cloud_topic_mov = new String();
    static String cloud_topic_temp = new String();
    static String mongo_host = new String();
    static String mongo_replica = new String();
    static String mongo_database = new String();
    static String mongo_collection_temp = new String();
    static String mongo_collection_mov = new String();
    static String mongo_collection_anom = new String();
    static String mongo_authentication = new String();
    static JTextArea documentLabel = new JTextArea("\n");

    protected static void loadProperties() throws IOException {
        Properties p = new Properties();
        p.load(new FileInputStream("C:\\Users\\Franc\\Downloads\\3ºProjecto\\PISID_MQTT\\src\\CloudToMongo.ini"));
        mongo_address = p.getProperty("mongo_address");
        mongo_user = p.getProperty("mongo_user");
        mongo_password = p.getProperty("mongo_password");
        mongo_replica = p.getProperty("mongo_replica");
        cloud_server = p.getProperty("cloud_server");
        cloud_topic_mov = p.getProperty("cloud_topic_mov");
        cloud_topic_temp = p.getProperty("cloud_topic_temp");
        mongo_host = p.getProperty("mongo_host");
        mongo_database = p.getProperty("mongo_database");
        mongo_authentication = p.getProperty("mongo_authentication");
        mongo_collection_temp = p.getProperty("mongo_collection_temp");
        mongo_collection_mov = p.getProperty("mongo_collection_mov");
        mongo_collection_anom = p.getProperty("mongo_collection_anom");
    }

    public static void publishSensorTemp(final MqttClient mqttclientTEMP) throws MqttException {

        try {

            MqttMessage mqtt_message = new MqttMessage();

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Sensor", (int) (Math.random() * 2) == 0 ? 1 : 2);
            jsonObject.put("Hora", getCurrentDate());
            jsonObject.put("Temperatura", (Math.random() * 2) + 10);

            System.out.println("Gerei o JSON: " + jsonObject.toString());
            mqtt_message.setPayload(jsonObject.toString().getBytes());
            if (!isConnectedTemp) {
                mqttclientTEMP.connect();
                isConnectedTemp = Boolean.TRUE;
            }
            mqttclientTEMP.publish(cloud_topic_temp, mqtt_message);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishSensorMov(MqttClient mqttclientMOVE) throws MqttException {

        try {

            MqttMessage mqtt_message = new MqttMessage();

            JSONObject jsonObject = new JSONObject();

            if (entrou) {
                jsonObject.put("Sala Entrada", 1);
                jsonObject.put("Sala Saida", 2);
                entrou = false;
            } else {
                jsonObject.put("Sala Entrada", 2);
                jsonObject.put("Sala Saida", 1);
                entrou = true;
            }

            jsonObject.put("Hora", this.getCurrentDate());

            System.out.println("Gerei o JSON: " + jsonObject.toString());
            mqtt_message.setPayload(jsonObject.toString().getBytes());
            if (!isConnectedMov) {
                mqttclientMOVE.connect();
                isConnectedMov = Boolean.TRUE;
            }
            mqttclientMOVE.publish(cloud_topic_mov, mqtt_message);

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    @Override
    public void run() {
        try {
            loadProperties();
            int i = new Random().nextInt(100000);
            MqttClient mqttclientTEMP = new MqttClient(cloud_server, "CloudToMongo_" + String.valueOf(i) + "_" + cloud_topic_temp);

            int t = new Random().nextInt(100000);
            MqttClient mqttclientMOVE = new MqttClient(cloud_server, "CloudToMongo_" + String.valueOf(t) + "_" + cloud_topic_mov);

            while (true) {
                publishSensorTemp(mqttclientTEMP);
                publishSensorMov(mqttclientMOVE);
                Thread.sleep(1000);
            }
        } catch (IOException | MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Simulator s = new Simulator();
        s.start();
    }
}
