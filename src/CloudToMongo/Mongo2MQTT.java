package CloudToMongo;
import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.swing.*;

import static com.mongodb.client.model.Indexes.descending;


public class Mongo2MQTT extends AbstractCloudToMongo implements MqttCallback {

    String broker = "tcp://192.168.1.91:1883";
    String clientId = "mqtt_client";
    String username = "mqtt_client";
    String password = "123456";
    String topic = "Move";

    public String connect2Mongo(){
        try {
            loadProperties();
        } catch (Exception e) {
            System.out.println("Error reading CloudToMongo.CloudToMongo.ini file " + e);
            JOptionPane.showMessageDialog(null, "The CloudToMongo.CloudToMongo.inifile wasn't found.", "CloudToMongo", JOptionPane.ERROR_MESSAGE);
        }
        connectMongo(2);
        // Selecionando a coleção
        DBCollection collection = db.getCollection("Move");

        // Buscando a linha com o último ID mais alto
        //Corrigir esta parte nao esta a ir buscar o ultimo id do mongo
        BasicDBObject query = new BasicDBObject();
        query.put("ID_Mongo", -1);
        BasicDBObject doc = (BasicDBObject) collection.find().sort(query).limit(1).next();

        if (doc != null) {
            System.out.println("Linha com o último ID mais alto:");
            System.out.println(doc.toString());
        } else {
            System.out.println("Não foram encontrados documentos na coleção.");
        }

        return doc.toString();
    }

    public void connect2MQTT() {

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());

            mqttClient.connect(connOpts);
            System.out.println("Conectado ao broker: " + broker);
            int i=0;
            while(i<100000) {
                String payload = String.format("{ \"ID_Mongo: \" : %d , \"sensor: \" : \"{Hora:\\\"2023-04-11 13:12:23.913836\\\", SalaEntrada:4, SalaSaida:5}\" , \"_id\" : { \"$oid\" : \"64354eae255a9e67243c584a\"}}", i, ++i);
                //String payload = "{ \"ID_Mongo: \" : i , \"sensor: \" : \"{Hora:\\\"2023-04-11 13:12:23.913836\\\", SalaEntrada:4, SalaSaida:5}\" , \"_id\" : { \"$oid\" : \"64354eae255a9e67243c584a\"}}";
                //String payload = connect2Mongo();
                MqttMessage message = new MqttMessage(payload.getBytes());
                mqttClient.publish(topic, message);
                System.out.println("Publicado mensagem no tópico: " + topic);
                System.out.println("Conteúdo da mensagem: " + payload);
                //i++;
            }
            //mqttClient.disconnect();
            //System.out.println("Desconectado do broker: " + broker);

        } catch (MqttException me) {
            System.out.println("Exceção ao se conectar ao broker: " + broker);
            me.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("Nova mensagem recebida no tópico: " + topic);
        System.out.println("Conteúdo da mensagem: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public static void main(String[] args) {
        Mongo2MQTT m = new Mongo2MQTT();
        m.connect2MQTT();

    }
}
