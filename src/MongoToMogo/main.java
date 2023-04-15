package MongoToMogo;

import CloudToMongo.CloudToMongoMove;
import CloudToMongo.CloudToMongoTemp;

public class main {
    public static void main(String[] args) {
        CloudToMongoMove cloudToMongoMove = new CloudToMongoMove();
        cloudToMongoMove.init();
        CloudToMongoTemp cloudToMongoTemp = new CloudToMongoTemp(1);
        cloudToMongoTemp.init();

    }


}
