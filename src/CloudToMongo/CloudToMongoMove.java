package CloudToMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Random;

public class CloudToMongoMove  extends AbstractCloudToMongo implements MqttCallback {
    private DBCollection Move;
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
            document_json = (DBObject) JSON.parse(c.toString());
            // Esta a perder a numeração do id a meio das connecçoes penso eu maybe
            //DBObject doc = new BasicDBObject("ID_Mongo: ", id++).append("sensor: ", new String(c.getPayload()));
            addIDMongoTemp(1);
            document_json.put("IDMongo", getIDMongoTemp());
           // document_json = (DBObject) JSON.parse(c.toString());
            Move.insert(document_json);
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

    @Override
    public void connectToCloudTemp() {

    }

    @Override
    public void connectToMQTTMove() {

    }

    @Override
    public void connectMQTT2MYSQLMove() {

    }

    @Override
    public void connectToCloudMove() {
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
    }

    @Override
    protected void initializeCollections() {
        Move = db.getCollection(mongo_collection_Move);
    }

    @Override
    protected void initializeIDMongo() {
        try {

        if (Move != null) {
            DBCursor cursor = Move.find()
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
        } catch (NoSuchElementException e) {
            System.out.println("ERRO A PROCURA DO ID MAXIMO DO MONGODB DA COLLECTION MOVE");

        }
    }

    /*public void init() {
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
    }*/
}
