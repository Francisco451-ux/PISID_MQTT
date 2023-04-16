package CloudToMongo;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class MQTT2MYSQL2TEMP extends AbstractCloudToMongo implements MqttCallback {

    String clientId = "tecnico";

    WriteMysql connectionMysql = new WriteMysql(2);

    public MQTT2MYSQL2TEMP() {

    }


    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        connectionMysql.ReadData(mqttMessage,2);
        //connectionMysql.start();
        System.out.println("SUB SUB : Nova mensagem recebida no tópico: " + s);
        System.out.println("SUB SUB : Conteúdo da mensagem: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
   /* public static void main(String[] args) {
        MQTT2MYSQL m = new MQTT2MYSQL();
        m.connect2MQTT();
    }*/

    @Override
    public void connectToCloudTemp() {

    }

    @Override
    public void connectToMQTTMove() {
        MemoryPersistence persistence = new MemoryPersistence();
        int i;
        try {
            i = new Random().nextInt(100000);
            MqttClient mqttClient = new MqttClient(MQTT_Broker, "MqttToMysql_"+String.valueOf(i)+"_"+MQTT_Topico_Move, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(MQTT_Username_temp_tecnico);
            connOpts.setPassword(MQTT_Password_tecnico_temp_tecnico.toCharArray());

            mqttClient.setCallback(this);
            mqttClient.connect(connOpts);
            System.out.println("Conectado ao broker: " + MQTT_Broker );

            mqttClient.subscribe(MQTT_Topico_TEMP);



        } catch (MqttException me) {
            System.out.println("Exceção ao se conectar ao broker: " + MQTT_Broker);
            me.printStackTrace();
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

    }

    @Override
    protected void initializeIDMongo() {

    }
}
