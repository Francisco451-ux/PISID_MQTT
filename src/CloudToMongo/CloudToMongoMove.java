package CloudToMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.sql.Timestamp;

public class CloudToMongoMove  extends AbstractCloudToMongo implements MqttCallback {
    private DBCollection collection;
    private DBObject lastObject;
    private static int id = 1; // ID inicial

    public CloudToMongoMove() {

    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage c)
            throws Exception {
        try {
            DBObject document_json;
            // Esta a perder a numeração do id a meio das connecçoes penso eu maybe
            DBObject doc = new BasicDBObject("ID_Mongo: ", id++).append("sensor: ", new String(c.getPayload()));
            document_json = (DBObject) JSON.parse(doc.toString());
            mongo_collection_Move_db.insert(document_json);
            System.out.println(document_json.toString());
            documentLabel.append(c.toString()+"\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        DBObject document_json;
        document_json = (DBObject) JSON.parse(mqttMessage.toString());
        System.out.println("Chegou uma messagem: " + document_json);
        // Tratar duplicados
        //Boolean isDupl = verifyDup(document_json);

        //if (!isDupl) {
            collection.insert(document_json);
            //lastObject = document_json;
            System.out.println(document_json.toString());
            documentLabel.append(mqttMessage.toString() + "\n");
        //} else if (isDupl) {
          //  System.out.println("O objeto " + document_json + " é duplicado!");
        //}
    }*/

   /* private Boolean verifyDup(final DBObject document_json) {
        Boolean isDup = Boolean.FALSE;
        if (lastObject != null) {
            // document_json.get("Sensor").toString().equals(String.valueOf(tempId))
            Timestamp lastTime = Timestamp.valueOf(lastObject.get("Hora").toString());
            Timestamp newTime = Timestamp.valueOf((String) document_json.get("Hora"));

            Integer salaEntradaLast = Integer.parseInt(lastObject.get("SalaEntrada").toString());
            Integer salaEntradaNew = Integer.parseInt(document_json.get("SalaEntrada").toString());

            Integer salaSaidaLast = Integer.parseInt(lastObject.get("SalaSaida").toString());
            Integer salaSaidaNew = Integer.parseInt(document_json.get("SalaSaida").toString());

            if (lastTime.equals(newTime) && salaEntradaLast.equals(salaEntradaNew) && salaSaidaLast.equals(salaSaidaNew)) {
                isDup = Boolean.TRUE;
            }
        }
        return isDup;
    }*/

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
        connecCloudMove();
        connectMongo(2);
        collection = mongo_collection_Move_db;
    }
}
