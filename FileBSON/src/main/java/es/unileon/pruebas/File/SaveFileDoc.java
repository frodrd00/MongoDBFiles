package es.unileon.pruebas.File;

import org.apache.commons.codec.binary.Base64;

import static com.mongodb.client.model.Filters.eq;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class SaveFileDoc 
{
  
	public static void main( String[] args )
    {
      
    	//conexión a la base de datos
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("filesBSON");
		MongoCollection<Document> collection = database.getCollection("test");		
		
		byte[] byteFile= readBytes();

		String s = base64Encode(byteFile);
		Document doc = new Document("_id",001).append("bytes", s);
		
		collection.insertOne(doc);
		
		openFile(collection);
		
		mongoClient.close();
		
    }
    
    public static  byte[]  readBytes(){  
    	
    	
	    FileInputStream crunchifyInputStream = null;
		 
		File crunchifyFile = new File("C:/dni.pdf");
	
		byte[] crunchifyByteStream = new byte[(int) crunchifyFile.length()];
	
	
		try {
			
			crunchifyInputStream = new FileInputStream(crunchifyFile);
			crunchifyInputStream.read(crunchifyByteStream);
			crunchifyInputStream.close();
			
		} catch (IOException e) {
	
			e.printStackTrace();
		}
		
		
		return crunchifyByteStream;
	}
    
    public static void openFile(MongoCollection<Document> collection){
    	
    	
		Document doc2 = collection.find(eq("_id",001)).first();
    	byte[] bytes = base64Decode(doc2.getString("bytes"));
		
		
    	try {
    		FileOutputStream fos = new FileOutputStream("C:/Users/Windows/Desktop/dniOut.pdf");
			fos.write(bytes);
	    	fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    	File file = new File("C:/Users/Windows/Desktop/dniOut.pdf");
    	
    	Desktop desktop = Desktop.getDesktop();
        if(file.exists())
			try {
				desktop.open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

    }

    

    public static byte[] base64Decode(String stringToDecode) {
        byte[] decodedBytes = Base64.decodeBase64(stringToDecode);
        return decodedBytes;
    }
    
    public static String base64Encode(byte[] bytes) {
       String decodedBytes = Base64.encodeBase64String(bytes);
       return decodedBytes;
    }
    
}
