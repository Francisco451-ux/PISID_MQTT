package CloudToMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.eclipse.paho.client.mqttv3.*;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.eclipse.paho.client.mqttv3.MqttCallback;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.Random;

public class CloudToMongoTemp extends AbstractCloudToMongo implements MqttCallback {
    private static int tempId;
    private DBCollection temp;
    //private static int id = 1; // ID inicial

    public CloudToMongoTemp() {


    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage c)
            throws Exception {
        try {
            DBObject document_json;
            document_json = (DBObject) JSON.parse(c.toString());
           // DBObject doc = new BasicDBObject("ID_Mongo: ", id++).append("sensor: ", new String(c.getPayload()));
            addIDMongoTemp(1);
            document_json.put("IDMongo", getIDMongoTemp());

            temp.insert(document_json);
            System.out.println(document_json.toString());
            documentLabel.append(c.toString()+"\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    /*
    public void init() {
        createWindow();
        try {
            loadProperties();
        } catch (Exception e) {
            System.out.println("Error reading CloudToMongo.CloudToMongo.ini file " + e);
            JOptionPane.showMessageDialog(null, "The CloudToMongo.CloudToMongo.inifile wasn't found.", "CloudToMongo", JOptionPane.ERROR_MESSAGE);
        }
        connecCloudTemp();
        connectMongo(1);
        collection = mongo_collection_temp_db;

    }*/


    @Override
    public void connectToCloudTemp() {
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

    @Override
    public void connectToMQTTMove() {

    }

    @Override
    public void connectMQTT2MYSQLMove() {

    }

    @Override
    public void connectToCloudMove() {

    }

    @Override
    protected void initializeCollections() {
        temp = db.getCollection(mongo_collection_TEMP);
    }

    @Override
    protected void initializeIDMongo() {
        if (temp != null) {
            DBCursor cursor = temp.find()
                    .sort(new BasicDBObject("IDMongo", -1))
                    .limit(1);

            DBObject doc = cursor.next();
            if (doc.get("IDMongo") == null) {
                addIDMongoTemp(2);
            } else if (doc.get("IDMongo") != null) {
                int s = (int) doc.get("IDMongo");
                setIDMongoTemp(s);
            } else if (getIDMongoTemp() != -1) {
                addIDMongoTemp(1);
            }
        }

    }
}
