package es.unileon.pruebas.Gridfs;


import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.*;
import com.mongodb.client.gridfs.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import static com.mongodb.client.model.Filters.eq;



public class SaveFileGridFS {
	
	
	//REFERENCIAS:
	//mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/gridfs/
	//https://docs.mongodb.com/manual/core/gridfs/
	
	public static void main(String[] args) {


		//conexión a la base de datos
		MongoClient mongoClient = new MongoClient();
		MongoDatabase myDatabase = mongoClient.getDatabase("filesGridFS");
		
		//creamos el contenedor bucket que contendra el fs.chunks y fs.files
		GridFSBucket gridFSBucket = GridFSBuckets.create(myDatabase);
		
		ObjectId fileId = uploadToGridFS(gridFSBucket);
		
		openFile(gridFSBucket, fileId);
		
		mongoClient.close();
		
		
	}
	

	public static ObjectId uploadToGridFS(GridFSBucket gridFSBucket){
		
		//creamos las opciones para el GridFS (existen más opciones)
		GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(358400) //tamaño de cada chunk en bytes
                .metadata(new Document("tipo", "dni")); //datos opcionales para añadir más información

		//le damos un nombre al archivo que vamos a subir, filename será escaneadosDNI2 que estará en fs.files
		GridFSUploadStream uploadStream = gridFSBucket.openUploadStream("escaneadosDNI2", options);
		
		byte[] data = null;
		
		try {//leemos los bytes del pdf
			data = Files.readAllBytes(new File("C:/dni.pdf").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//almacenamos los datos
		uploadStream.write(data);
		uploadStream.close();
		
		System.out.println("Archivo subido con id: " + uploadStream.getObjectId());
				
		return uploadStream.getObjectId();
		
		
	}
	
	public static void openFile(GridFSBucket gridFSBucket, ObjectId fileId){
    	
    	
		FileOutputStream streamToDownloadTo;
		try {
			streamToDownloadTo = new FileOutputStream("C:/Users/Windows/Desktop/dniOut.pdf");
			gridFSBucket.downloadToStream(fileId, streamToDownloadTo);
		    streamToDownloadTo.close();
		    System.out.println(streamToDownloadTo.toString());
		} catch (IOException  e1) {
			e1.printStackTrace();
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
	
	
}