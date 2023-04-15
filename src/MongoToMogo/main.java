package MongoToMogo;

import CloudToMongo.*;

public class main {
    public static void main(String[] args) {
        CloudToMongoMove cloudToMongoMove = new CloudToMongoMove();
        cloudToMongoMove.start();
        //cloudToMongoMove.init();
        CloudToMongoTemp cloudToMongoTemp = new CloudToMongoTemp();
        cloudToMongoTemp.start();
        //MQTT2MYSQL serverMqttsub = new MQTT2MYSQL();
        //serverMqttsub.start();
        MQTT2MYSQL2TEMP serverMqtt2TEMPsub = new MQTT2MYSQL2TEMP();
        serverMqtt2TEMPsub.start();
        Mongo2MQTT2TEMP serverMqtt2TEMPpub = new Mongo2MQTT2TEMP();
        serverMqtt2TEMPpub.start();
        //Mongo2MQTT serverMqttpub = new Mongo2MQTT();
        //serverMqttpub.start();
        //cloudToMongoTemp.init();

    }


}
