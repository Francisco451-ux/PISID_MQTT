package CloudToMongo;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import javax.swing.*;

import static com.mongodb.client.model.Indexes.descending;


public class Mongo2MQTT2TEMP extends AbstractCloudToMongo implements MqttCallback {


    String clientId = "mqtt_client";

    private DBCollection Move;

    public Mongo2MQTT2TEMP() {

    }


    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("PUB PUB : Nova mensagem recebida no tópico: " + MQTT_Topico_TEMP);
        System.out.println("PUB PUB : Conteúdo da mensagem: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    /*public static void main(String[] args) {
        Mongo2MQTT m = new Mongo2MQTT();
        m.connect2MQTT();

    }*/

    @Override
    public void connectToCloudTemp() {

    }

    @Override
    public void connectToMQTTMove() {
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(MQTT_Broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(MQTT_Username_Mqtt);
            connOpts.setPassword(MQTT_Password_Mqtt.toCharArray());

            mqttClient.connect(connOpts);
            System.out.println("Conectado ao broker: " + MQTT_Broker);
            int i = 0;
            //while(i<100000) {
            //String payload = String.format("{ \"ID_Mongo: \" : %d , \"sensor: \" : \"{Hora:\\\"2023-04-11 13:12:23.913836\\\", SalaEntrada:4, SalaSaida:5}\" , \"_id\" : { \"$oid\" : \"64354eae255a9e67243c584a\"}}", i, ++i);
            //String payload = "{ \"ID_Mongo: \" : i , \"sensor: \" : \"{Hora:\\\"2023-04-11 13:12:23.913836\\\", SalaEntrada:4, SalaSaida:5}\" , \"_id\" : { \"$oid\" : \"64354eae255a9e67243c584a\"}}";
            //String payload = connect2Mongo();
            //MqttMessage message = new MqttMessage(payload.getBytes());
            while (true) {
                sleep(1000);
                String payload = ultimoIDMongo();
                MqttMessage message = new MqttMessage(payload.getBytes());
                String mysqlString = message.toString();
                JSONObject json = new JSONObject(payload.substring(mysqlString.indexOf('{')));
                json.remove("_id");
                json.remove("Hora");
                json.remove("IDMongo");

                String newPayload = json.toString();
                MqttMessage newMessage = new MqttMessage(newPayload.getBytes());
                mqttClient.publish(MQTT_Topico_TEMP, newMessage);
                System.out.println("Publicado mensagem no tópico: " + MQTT_Topico_TEMP);
                System.out.println("Conteúdo da mensagem: " + payload);
            }
            //i++;
            //}
            //mqttClient.disconnect();
            //System.out.println("Desconectado do broker: " + broker);

        } catch (MqttException me) {
            System.out.println("Exceção ao se conectar ao broker: " + MQTT_Topico_Move);
            me.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void connectMQTT2MYSQLMove() {

    }

    @Override
    public void connectToCloudMove() {

    }

    @Override
    protected void initializeCollections() {
        Move = db.getCollection(mongo_collection_TEMP);

    }

    @Override
    protected void initializeIDMongo() {

    }

    public String ultimoIDMongo() {
        // Buscando a linha com o último ID mais alto
        //Corrigir esta parte nao esta a ir buscar o ultimo id do mongo
        BasicDBObject query = new BasicDBObject();
        query.put("IDMongo", -1);
        BasicDBObject doc = (BasicDBObject) Move.find().sort(query).limit(1).next();

        if (doc != null) {
            System.out.println("Linha com o último ID mais alto:");
            System.out.println(doc.toString());
        } else {
            System.out.println("Não foram encontrados documentos na coleção.");
        }


        return doc.toString();
    }
}
