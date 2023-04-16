package MongoToMogo;

import CloudToMongo.*;

import static java.lang.Thread.sleep;

public class main {
    public static void main(String[] args)  {
        CloudToMongoMove cloudToMongoMove = new CloudToMongoMove();
        cloudToMongoMove.start();
        //cloudToMongoMove.init();
        CloudToMongoTemp cloudToMongoTemp = new CloudToMongoTemp();
        cloudToMongoTemp.start();
        try {
            sleep(1000);
            MQTT2MYSQL serverMqttsub = new MQTT2MYSQL();
            serverMqttsub.start();
            sleep(1000);
            MQTT2MYSQL2TEMP serverMqtt2TEMPsub = new MQTT2MYSQL2TEMP();
            serverMqtt2TEMPsub.start();

        } catch (InterruptedException e) {
            System.out.println("Erro Start the Thread mqtt a parte do subscribe");
            e.printStackTrace();
        }

        try {
            sleep(1000);
            Mongo2MQTT2TEMP serverMqtt2TEMPpub = new Mongo2MQTT2TEMP();
            serverMqtt2TEMPpub.start();
            sleep(1000);
            Mongo2MQTT serverMqttpub = new Mongo2MQTT();
            serverMqttpub.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // MQTT2MYSQL serverMqttsub = new MQTT2MYSQL();
        //serverMqttsub.start();

        //MQTT2MYSQL2TEMP serverMqtt2TEMPsub = new MQTT2MYSQL2TEMP();
        //serverMqtt2TEMPsub.start();
        //Mongo2MQTT2TEMP serverMqtt2TEMPpub = new Mongo2MQTT2TEMP();
        //serverMqtt2TEMPpub.start();

        //Mongo2MQTT serverMqttpub = new Mongo2MQTT();
        //serverMqttpub.start();
        //cloudToMongoTemp.init();

    }


}
