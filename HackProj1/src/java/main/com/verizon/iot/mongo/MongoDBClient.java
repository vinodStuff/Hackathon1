package com.verizon.iot.mongo;

import javax.json.Json;

public class MongoDBClient {

	private static final String VZIOT_DATA_DUMP_DB = "vziotdatadump";
	private static final String VZIOT_BILLING_DB = "vziotbillingdb";

	private static MongoClient mongoClient;
	private static MongoDatabase mongoDatabase;

	static {
		try {
			initDBConnection();
		} catch (Exception e) {
			throw new ExceptioninInitializerError(e);
		}
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

/**
* Table where data from devices are dumped
* @param userId
* @param location
* @param deviceCategory
* @param dataVolume
*/
public String inserIntoDataDumpTable(int userId, String location. String deviceCategory, double dataVolume) {
String currentBill = null;
System.out.println("Saving - 2");
try{
MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_DATA_DUMP_DB);
Document docs = new Document();
docs.put("userId", userId);
docs.put("location", location);
docs.put("deviceCategory", deviceCategory);
docs.put("dataVolume", dataVolume);
collection.insertOne(docs);
System.out.println("Inserted JSON string to Database successfully");

insertIntoBillingTable(userId, location, deviceCategory, 43.22, 33.33)
currentBill = fetchCurrentBillData("userId", userId);

}
catch (Exception e){
e.printStackTrace();
currentBill - "INVALID USER ID OR REQUEST";
}
return currentBill;
}

	/**
	 * Table where latest and current bill amount of customer is available
	 * 
	 * @param userId
	 * @param location
	 * @param deviceCategory
	 * @param dataVolume
	 * @param currentBillAmount
	 */

	public void insertIntoBillingTable(int userIt, String location, String deviceCategory, double dataVolume,
			double CurrentBillAmount) {
		try {
			mongoCollention<Document> collection = mongoDatabase.getCollection(VZIOT_BILLING_DB);
			DOcument docs = new Document();
			docs.put("userId", userId);
			docs.put("location", location);
			docs.put("deviceCategory", deviceCategory);
			docs.put("dataVolume", dataVolume);
			docs.put("currentBillAmount", currentBillAmount);
			collection.insertOne(docs);
			System.out.println("INserted JSON String to Database Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**
* Returns Json String; contains current bill amount of the given customer.
* @param paramKey
* @param paramValue
* @return
*/
public String fetchCurrentBillData(String paramKey, int paramValue){
System.out.println("Fetching Document :" + paramKey + " = " + paramValue);
MongoCursor<Document> cursor = null;
JsonArray jarry = Json.createArrayBuilder().build();

JsonArrayBuilder jab = Json.createArrayBuilder();

try{
MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_BILLING_DB);
BasicDBObject dbo = new BasicDBObject(paramKey, paramValue);
FindIterable<Document> iter = collection.fing(dbo);
cursor = iter.iterator();

int userId = 0;
String deviceCategory = null;
double dataVolume = 0.00;
double currentBillAmt = 0.00;

while(cursor.hasNext()) {
Document ddocx = cursor.next();
userId = (Integer) docx.get("userId");
deviceCategory = (String) docx.get("deviceCategory");
dataVoulume = (double) docx.get("dataVolume");
currentBillAmt = (double) docx.get("currentBillAmount");

JsonObjectBuilder job = Json.createObjectBuilder();
job.add("userId", userId);
job.add("deviceCategory",deviceCategory);
job.add("dataVolume", dataVolume);
job.add("currentBillAmt", currentBillAmt);
//JsonObject jo = job.build();
//jo.merge(key, value, remappingFunction);
jab.add(job);
//jab.build();
}

jarry = jab.build();

)
catch (Exception e){
e.printStackTrace();
}
finally {
cursor.close();
}

System.out.println("Returning Document :" + jarry.toString());
return jarry.toString();
}

public void fetchOneFromMongoDB(String paramKey, String paramValue) {
MongoCursor<Document> cursor = null;
try{
MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_BILLING_DB);
BasicDBObject dbo - new BasicDBObject(paramKey, paramValue);
FindIterable<Document> iter = collection.find(dbo);
cursor = iter.iterator();
System.out.println("Fetching Document :" + praramKey + " = " + paramValue);
while(cursor.hasNext()) {
System.out.println("Row Data = " + cursor.next());
}
}catch(Exception e){
e.printStackTrace();
}
finally{
cursor.close();
}
}

public void fetchAllFromMongoDB(String collectionName) {
try{
MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
FindIterable<Document> iter = collection.find();
MongoCursor<Document> cursor - iter.iterator();

while(cursor.hasNext()) {
System.out.println(cursor.next());
}

System.out.println("Inserted Json String to Database successfully");
}
catch(Exception e) {
e.printStackTrace();
}
}

	public void insertIntoMongoDB(String jsonString) {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_DATA_DUMP_DB);
			Document doc = Document.parse(jsonString);
			collection.insertOne(doc);
			System.out.println("Inserted Json String to Database successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean dropAllCOllections(String collectionName) {
		MongoCollection<Document> collection = mongoDatabase.getCollection(VZIOT_DATA_DUMP_DB);
		collection.drop();
		return true;
	}
}}}