package es.unileon.pruebas.GuardarUbicacion;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class SaveFilePath 
{
    public static void main( String[] args )
    {
    	MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("filesLink");
		MongoCollection<Document> collection = database.getCollection("test");		
		
		Document doc = new Document("ubicacion","c:/dni.pdf");
		collection.insertOne(doc);

		
		Document doc2 = collection.find(eq("ubicacion","c:/dni.pdf")).first();
		System.out.println(doc2.getString("ubicacion"));
		
		mongoClient.close();
		
		File file = new File(doc2.getString("ubicacion"));
		
		Desktop desktop = Desktop.getDesktop();
        if(file.exists())
			try {
				desktop.open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

    }
}
