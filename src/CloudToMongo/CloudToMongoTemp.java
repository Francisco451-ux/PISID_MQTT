package CloudToMongo;

import com.mongodb.BasicDBObject;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.*;
import java.sql.Timestamp;

public class CloudToMongoTemp extends AbstractCloudToMongo implements MqttCallback {
    private static int tempId;
    private DBCollection collection;
    private static int id = 1; // ID inicial

    public CloudToMongoTemp(final int tempId) {
        CloudToMongoTemp.tempId = tempId;

    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage c)
            throws Exception {
        try {
            DBObject document_json;
           // DBObject doc = new BasicDBObject("ID_Mongo: ", id++).append("sensor: ", new String(c.getPayload()));
            document_json = (DBObject) JSON.parse(c.toString());
            mongo_collection_temp_db.insert(document_json);
            System.out.println(document_json.toString());
            documentLabel.append(c.toString()+"\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

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

    }


}
