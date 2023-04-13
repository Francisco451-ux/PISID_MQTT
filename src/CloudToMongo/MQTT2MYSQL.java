package CloudToMongo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT2MYSQL implements MqttCallback {
    String broker = "tcp://192.168.1.91:1883";
    String clientId = "tecnico";
    String username = "tecnico";
    String password = "123456";
    String topic = "Move";

    public void connect2MQTT() {
        MemoryPersistence persistence = new MemoryPersistence();

        try {
           MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());

            mqttClient.setCallback(this);
            mqttClient.connect(connOpts);
            System.out.println("Conectado ao broker: " + broker);

            mqttClient.subscribe(topic);



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
        System.out.println("Nova mensagem recebida no tópico: " + s);
        System.out.println("Conteúdo da mensagem: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
    public static void main(String[] args) {
        MQTT2MYSQL m = new MQTT2MYSQL();
        m.connect2MQTT();
    }
}
