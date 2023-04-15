package MongoToMogo;

import CloudToMongo.CloudToMongoMove;
import CloudToMongo.CloudToMongoTemp;
import CloudToMongo.MQTT2MYSQL;
import CloudToMongo.Mongo2MQTT;

public class main {
    public static void main(String[] args) {
        CloudToMongoMove cloudToMongoMove = new CloudToMongoMove();
        cloudToMongoMove.start();
        //cloudToMongoMove.init();
        CloudToMongoTemp cloudToMongoTemp = new CloudToMongoTemp();
        cloudToMongoTemp.start();
        MQTT2MYSQL serverMqttsub = new MQTT2MYSQL();
        serverMqttsub.start();
        Mongo2MQTT serverMqttpub = new Mongo2MQTT();
        serverMqttpub.start();
        //cloudToMongoTemp.init();

    }


}
