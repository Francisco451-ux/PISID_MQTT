package CloudToMongo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class MQTT2MYSQL extends AbstractCloudToMongo implements MqttCallback {
    String broker = "tcp://192.168.1.91:1883";
    String clientId = "tecnico";
    String username = "tecnico";
    String password = "123456";
    String topic = "Move";
    WriteMysql connectionMysql = new WriteMysql(1);

    public MQTT2MYSQL() {

    }
    /*public void connect2MQTT() {
        MemoryPersistence persistence = new MemoryPersistence();

        try {
           MqttClient mqttClient = new MqttClient(MQTT_Broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(MQTT_Username_tecnico);
            connOpts.setPassword(MQTT_Password_tecnico.toCharArray());

            mqttClient.setCallback(this);
            mqttClient.connect(connOpts);
            System.out.println("Conectado ao broker: " + MQTT_Broker );

            mqttClient.subscribe(MQTT_Topico_Move);



        } catch (MqttException me) {
            System.out.println("Exceção ao se conectar ao broker: " + MQTT_Broker);
            me.printStackTrace();
        }
    }*/

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        connectionMysql.ReadData(mqttMessage,1);
       // connectionMysql.start();
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

        int i;

        try {
            i = new Random().nextInt(100000);
            MqttClient mqttClient = new MqttClient(MQTT_Broker, "MqttToMysql_"+String.valueOf(i)+"_"+MQTT_Topico_Move);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(MQTT_Username_tecnico);
            connOpts.setPassword(MQTT_Password_tecnico.toCharArray());

            mqttClient.setCallback(this);
            mqttClient.connect(connOpts);
            System.out.println("Conectado ao broker: " + MQTT_Broker );

            mqttClient.subscribe(MQTT_Topico_Move);



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
