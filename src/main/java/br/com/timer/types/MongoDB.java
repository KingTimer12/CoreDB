package br.com.timer.types;

import br.com.timer.interfaces.DBBackend;
import br.com.timer.objects.rows.Row;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MongoDB implements DBBackend {

    private final String URI, database, collectionName;
    private MongoDatabase mongoDatabase;
    private MongoClient client;

    @Override
    public void openConnection() {
        MongoClientURI clientURI = new MongoClientURI(System.getProperty(URI));
        try (MongoClient client = new MongoClient(clientURI)) {
            this.client = client;
            this.mongoDatabase = client.getDatabase(this.database);
        }
    }

    @Override
    public void closeConnection() {
        client.close();
    }

    public MongoDB insert(Row... insertValues) {
        return insert(Arrays.asList(insertValues));
    }

    public MongoDB update(Row found, Row... updateValues) {
        return update(found, Arrays.asList(updateValues));
    }

    public MongoDB insert(List<Row> insertValues) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document document = new Document();
        insertValues.forEach(value -> document.append(value.getField(), value.getValue()));
        collection.insertOne(document);
        return this;
    }

    public MongoDB update(Row found, List<Row> updateValues) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Optional.of(collection.find(new Document(found.getField(), found.getValue())).first())
                .ifPresent(consumer -> updateValues.forEach(where -> {
                    Bson valueUpdate = new Document(where.getField(), where.getValue());
                    Bson command = new Document("$set", valueUpdate);
                    collection.updateOne(valueUpdate, command);
                }));
        return this;
    }

}
