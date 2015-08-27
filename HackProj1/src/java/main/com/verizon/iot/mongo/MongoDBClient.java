package com.verizon.iot.mongo;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class MongoDBClient {

	private static final String VZIOT_DATA_DUMP_DB = "vziotdatadump";
	private static final String VZIOT_BILLING_DB = "vziotbillingdb";
	private static final String VZIOT_PLAN_MASTER_DB = "vziotplandb";
	private static final String VZIOT_USER_PROFILE_DB = "vziotprofiledb";

	private static final Map<String, Double> RATE_MAP = new HashMap<String, Double>();

	private static MongoClient mongoClient;
	private static MongoDatabase mongoDatabase;

	static {
		try {
			initDBConnection();
		} catch (Exception e) {
			throw new ExceptionInInitializerError("DB connection not created");
		}
	}

	public static Map<String, Double> getRateMap() {
		return RATE_MAP;
	}

	public static void main(String[] args) {
		// buildPlans();
		// buildRateMao();
		// provisionUser(1234,"SMALL");
		// insertIntoDataDumpTable(1234, "ny", "health", 12.00);
		// fetchUserPlan(1234);
		// fetchPlanDetails("SMALL");
		// updateBillData(1234, "health", 3.00, 3.24);
		// fetchCurrentBillData(1234);
		// fetchCurrentUsageDetails(1234, "health");

	}

	private static void buildRateMap() {
		RATE_MAP.put("HealthDevices", 0.32);
		RATE_MAP.put("Gadgets", 0.22);
		RATE_MAP.put("Appliances", 0.12);
		RATE_MAP.put("Others", 0.18);
	}

	private static void initDBConnection() throws Exception {
		try {
			mongoClient = new MongoClient("localhost", 27017);
			mongoDatabase = mongoClient.getDatabase("test");
			System.out.println("Connect to database successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void buildPlans(){
		try{
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_PLAN_MASTER_DB);
			
			Document doc = new Document();
			doc.put("planId","SMALL");
			
			Document docg = new Document();
			docg.put("Gadgets", 5.00);
			docg.put("health", 10.00);
			docg.put("appliance", 20.00);
			docg.put("other", 30.00);
			
			doc.put("details", docg);
			collection.insertOne(doc);
			
			Document doc1 = new Document();
			doc1.put("planId","MEDIUM");
			
			Document docg1 = new Document();
			docg1.put("Gadgets", 10.00);
			docg1.put("health", 20.00);
			docg1.put("appliance", 30.00);
			docg1.put("other", 40.00);
			
			doc1.put("details", docg1);
			collection.insertOne(doc1);

			Document doc2 = new Document();
			doc2.put("planId","LARGE");
			
			Document docg2 = new Document();
			docg2.put("Gadgets", 20.00);
			docg2.put("health", 30.00);
			docg2.put("appliance", 40.00);
			docg2.put("other", 50.00);
			
			doc2.put("details", docg2);
			collection.insertOne(doc2);
			
			System.out.println("Inserted JSON string to database successfully");
		} catch (Exception e){
			e.printStackTrace();
		}
			
	}



	public static Document fetchCurrentUsageDetails(int userId, String deviceCategory){
		MongoCursor<Document> cursor = null;
		Document userUsageDoc = null;
		try{
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_BILLING_DB);
			BasicDBObject dbo = new BasicDBObject("userId", userId).append("deviceCategory", deviceCategory);
			FindIterable<Document> iter = collection.find(dbo);
			cursor = iter.iterator();			
			System.out.println("Fetching document :"+userId);
			
			while(cursor.hasNext()){
				userUsageDoc = cursor.next();
				System.out.println("Row Data = "+ userUsageDoc);
				break;
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return userUsageDoc;
	}



	public static Document fetchPlanDetails(String planId){
		MongoCursor<Document> cursor = null;
		Document planDoc = null;
		try{
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_PLAN_MASTER_DB);
			BasicDBObject dbo = new BasicDBObject("planId", planId);
			FindIterable<Document> iter = collection.find(dbo);
			cursor = iter.iterator();			
			System.out.println("Fetching document for plan id :"+planId);
			
			while(cursor.hasNext()){
				planDoc = cursor.next();
				System.out.println("Row Data = "+ planDoc);
				break;
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return planDoc;
	}

	
	public static String fetchUserPlanId(int userId){
		MongoCursor<Document> cursor = null;
		Document planDoc = null;
		try{
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_USER_PROFILE_DB);
			BasicDBObject dbo = new BasicDBObject("userId", userId);
			FindIterable<Document> iter = collection.find(dbo);
			cursor = iter.iterator();			
			System.out.println("Fetching document for userId :"+userId);
			
			while(cursor.hasNext()){
				planDoc = cursor.next();
				System.out.println("Plan Row Data = "+ planDoc);
				break;
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return planDoc.getString("planId");
	}
	
	public static void provisionUser(int userId, String planId){
		try{
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_USER_PROFILE_DB);
			System.out.println("User provisioned in DB successfully");
			UpdateOptions uo = new UpdateOptions();
			uo.upsert(true);
			
			collection.updateOne(
					new Document("userId", userId),
					new Document("$set", new Document("planId", planId)),
					uo
					);			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void insertIntoDataDumpTable(int userId, String location, String deviceCategory, double dataVolume){
		try{
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_DATA_DUMP_DB);
			
			Document doc = new Document();
			doc.put("userId",userId);
			doc.put("location",location);
			doc.put("deviceCategory",deviceCategory);
			doc.put("dataVolume",dataVolume);
			collection.insertOne(doc);
			System.out.println("Inserted JSON string into DB successfully");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	/**
	 * Returns Json String; contains current bill amount of the given customer.
	 * 
	 * @param paramKey
	 * @param paramValue
	 * @return
	 */
	public static String fetchCurrentBillData(int userId) {
		System.out.println("Fetching Document :" + userId);
		MongoCursor<Document> cursor = null;
		JsonArray jarry = Json.createArrayBuilder().build();

		JsonArrayBuilder jab = Json.createArrayBuilder();

		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_BILLING_DB);
			BasicDBObject dbo = new BasicDBObject("userId", userId);
			FindIterable<Document> iter = collection.find(dbo);
			cursor = iter.iterator();

			String deviceCategory = null;
			double dataVolume = 0.00;
			double currentBillAmt = 0.00;

			while (cursor.hasNext()) {
				Document docx = cursor.next();
				deviceCategory = (String) docx.get("deviceCategory");
				dataVolume = (Double) docx.get("dataVolume");
				currentBillAmt = (Double) docx.get("currentBillAmount");

				JsonObjectBuilder job = Json.createObjectBuilder();
				job.add("userId", userId);
				job.add("deviceCategory", deviceCategory);
				job.add("dataVolume", dataVolume);
				job.add("currentBillAmt", currentBillAmt);

				jab.add(job);

			}

			jarry = jab.build();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}

		System.out.println("Returning Document :" + jarry.toString());
		return jarry.toString();
	}
	
	
	public static void updateBillData(int userId, String deviceCategory, double dataVolume, double currentBillAmount){
		MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_BILLING_DB);
		BasicDBObject dbo = new BasicDBObject("userId", userId).append("deviceCategory",deviceCategory);
		FindIterable<Document> iter = collection.find(dbo);
		MongoCursor<Document> cursor = iter.iterator();
		
		System.out.println("Fetching document userId = " + userId + " deviceCategory = " + deviceCategory);
		
		Document doc = null;
		while(cursor.hasNext()){
			doc = cursor.next();
			System.out.println("row data doc =" + doc);
			break;
		}
		
		double totAmount = 0;
		double totData = 0;
		
		if (doc != null){
			double a = doc.getDouble("currentBillAmt");
			double b = doc.getDouble("dataVolume");
			
			System.out.println("a = " + a);
			System.out.println("b = " + b);
			
			totAmount = currentBillAmount + a ;
			totData = dataVolume + b;
		} else {
			totAmount = currentBillAmount;
			totData = dataVolume;
		}
		
		System.out.println(totAmount);
		System.out.println(totData);
		
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		
		collection.updateOne(
				new Document("userId", userId).append("deviceCategory",deviceCategory),
				new Document("$set", new Document("currentBillAmt", totAmount).append("dataVolume",totData)),
				uo
				);	
		System.out.println("Updated to database successfully");
			
	}
	
}