package com.github.dsipaint.AMGN.io;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.io.FileNotFoundException;

public class MongoConnector
{
	private static MongoClient client;
	
	public static MongoClient client()
    {
		if(client == null)
        {
			try
            {
				String database_address = (String)IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "database_address");
				if(database_address == null || database_address.isEmpty())
					client = MongoClients.create("mongodb://localhost");
                else
					client = MongoClients.create("mongodb://" + database_address); 
			}
            catch (FileNotFoundException e)
            {	
				e.printStackTrace();
			} 
		}
		
		return client;
	}
}
