package CloudToMongo;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.mongodb.*;
import com.mongodb.util.JSON;

import java.util.*;
import java.util.Vector;
import java.io.File;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class AbstractCloudToMongo extends Thread implements MqttCallback {
    MqttClient mqttclient;
    static MongoClient mongoClient;
    static DB db;
    static DBCollection mongocol;
    static DBCollection mongo_collection_temp_db;
    static DBCollection mongo_collection_Move_db;
    static DBCollection mongo_collection_Anomalias_db;
    static String mongo_user = new String();
    static String mongo_password = new String();
    static String mongo_address = new String();
    static String cloud_server = new String();
    static String cloud_topic_mov = new String();
    static String cloud_topic_temp = new String();
    static String cloud_topic = new String();
    static String mongo_host = new String();
    static String mongo_replica = new String();
    static String mongo_database = new String();
    static String mongo_collection = new String();
    static String mongo_collection_TEMP = new String();
    static String mongo_collection_Move = new String();
    static String mongo_collection_Anomalias = new String();
    static String mongo_authentication = new String();
    static JTextArea documentLabel = new JTextArea("\n");
    static String MQTT_Broker =  new String();
    static String MQTT_Topico_TEMP =  new String();
    static String MQTT_Topico_Move=  new String();
    static String MQTT_Username_Mqtt=  new String();
    static String MQTT_Username_tecnico=  new String();
    static String MQTT_Password_Mqtt=  new String();
    static String MQTT_Password_tecnico=  new String();

    static String MQTT_Username_temp_sub_tecnico=new String();
    static String MQTT_Username_temp_tecnico=new String();
    static String MQTT_Password_temp_sub_tecnico=new String();
    static String MQTT_Password_tecnico_temp_tecnico=new String();


    static String sql_database_connection_to=  new String();
    static String sql_database_user_to=  new String();
    static String sql_database_password_to=  new String();
    static String sql_table_to=  new String();
    static String  sql_table_to_temp =  new String();
    protected Integer IDMongoTemp = -1;
    protected Integer IDMongoMov = -1;

    protected static void createWindow() {
        JFrame frame = new JFrame("Cloud to Mongo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel textLabel = new JLabel("Data from broker: ",SwingConstants.CENTER);
        textLabel.setPreferredSize(new Dimension(600, 30));
        JScrollPane scroll = new JScrollPane (documentLabel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(600, 200));
        JButton b1 = new JButton("Stop the program");
        frame.getContentPane().add(textLabel, BorderLayout.PAGE_START);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        frame.getContentPane().add(b1, BorderLayout.PAGE_END);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
    }

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
        mongo_collection_TEMP = p.getProperty("mongo_collection_TEMP");
        mongo_collection_Move = p.getProperty("mongo_collection_Move");
        mongo_collection_Anomalias = p.getProperty("mongo_collection_Anomalias");
        MQTT_Broker =  p.getProperty("MQTT_Broker");
        MQTT_Topico_TEMP =  p.getProperty("MQTT_Topico_TEMP");
        MQTT_Topico_Move =  p.getProperty("MQTT_Topico_Move");
        MQTT_Username_Mqtt=  p.getProperty("MQTT_Username_Mqtt");
        MQTT_Username_tecnico=  p.getProperty("MQTT_Username_tecnico");
        MQTT_Password_Mqtt=  p.getProperty("MQTT_Password_Mqtt");
        MQTT_Password_tecnico=  p.getProperty("MQTT_Password_tecnico");

        MQTT_Username_temp_sub_tecnico= p.getProperty("MQTT_Username_temp_sub_tecnico");
        MQTT_Username_temp_tecnico= p.getProperty("MQTT_Username_temp_tecnico");
        MQTT_Password_temp_sub_tecnico= p.getProperty("MQTT_Password_temp_sub_tecnico");
        MQTT_Password_tecnico_temp_tecnico= p.getProperty("MQTT_Password_tecnico_temp_tecnico");

        sql_database_connection_to=   p.getProperty("sql_database_connection_to");
        sql_database_user_to=   p.getProperty("sql_database_user_to");
        sql_database_password_to=   p.getProperty("sql_database_password_to");
        sql_table_to=  p.getProperty("sql_table_to");
        sql_table_to_temp = p.getProperty("sql_table_to_temp");
    }
    /*public void connecCloudTemp() {
        int i;
        try {
            i = new Random().nextInt(100000);
            mqttclient = new MqttClient(cloud_server, "CloudToMongo_"+String.valueOf(i)+"_"+cloud_topic_temp);
            mqttclient.connect();
            mqttclient.setCallback(this);
            mqttclient.subscribe(cloud_topic_temp);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connecCloudMove() {
        int i;
        try {
            i = new Random().nextInt(100000);
            mqttclient = new MqttClient(cloud_server, "CloudToMongo_"+String.valueOf(i)+"_"+cloud_topic_mov);
            mqttclient.connect();
            mqttclient.setCallback(this);
            mqttclient.subscribe(cloud_topic_mov);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }*/

    public abstract void connectToCloudTemp();

    public abstract void connectToMQTTMove();

    public abstract void connectMQTT2MYSQLMove();

    public abstract void connectToCloudMove();

    protected abstract void initializeCollections();

    protected abstract void initializeIDMongo();


    public void connectMongo() {
        String mongoURI = new String();
        mongoURI = "mongodb://";
        if (mongo_authentication.equals("true")) mongoURI = mongoURI + mongo_user + ":" + mongo_password + "@";
        mongoURI = mongoURI + mongo_address;
        if (!mongo_replica.equals("false"))
            if (mongo_authentication.equals("true")) mongoURI = mongoURI + "/?replicaSet=" + mongo_replica+"&authSource=admin";
            else mongoURI = mongoURI + "/?replicaSet=" + mongo_replica;
        else
        if (mongo_authentication.equals("true")) mongoURI = mongoURI  + "/?authSource=admin";
        MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURI));
        db = mongoClient.getDB(mongo_database);
        /* mongocol = db.getCollection(mongo_collection);*/
        //mongo_collection_temp_db = db.getCollection(mongo_collection_TEMP);
        //mongo_collection_Move_db = db.getCollection(mongo_collection_Move);
       /* if (tempId == 1) {
            mongo_collection_temp_db = db.getCollection(mongo_collection_TEMP);
        } else if (tempId == 2) {
            mongo_collection_Move_db = db.getCollection(mongo_collection_Move);
        } else if (tempId == 0){
            mongo_collection_Anomalias_db = db.getCollection(mongo_collection_Anomalias);
        }*/
        initializeCollections();

    }

    @Override
    public void run() {
        createWindow();
        try {
            loadProperties();
        } catch (Exception e) {
            System.out.println("Error reading CloudToMongo.CloudToMongo.ini file " + e);
            JOptionPane.showMessageDialog(null, "The CloudToMongo.CloudToMongo.inifile wasn't found.", "CloudToMongo", JOptionPane.ERROR_MESSAGE);
        }
        connectToCloudTemp();
        connectToCloudMove();
        connectMongo();
        connectToMQTTMove();
        initializeIDMongo();
        connectMQTT2MYSQLMove();
    }

    protected synchronized void addIDMongoTemp(Integer increment) {
        IDMongoTemp += increment;
    }


    protected synchronized void setIDMongoTemp(Integer newValue) {
        IDMongoTemp = newValue;
    }

    protected synchronized Integer getIDMongoTemp() {
        return IDMongoTemp;
    }

    protected synchronized void addIDMongoMov() {
        IDMongoMov = IDMongoMov != null ? IDMongoMov++ : 1;
    }

    protected synchronized Integer getIDMongoMov() {
        return IDMongoMov;
    }


}

