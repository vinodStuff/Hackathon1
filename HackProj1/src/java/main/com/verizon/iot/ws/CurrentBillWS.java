package com.verizon.iot.ws;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.verizon.iot.mongo.MongoDBClient;

@Path("currentBill")
public class CurrentBillWS {
	@GET
	// @Consumes("text/plain")
	@Produces(MediaType.TEXT_HTML)
	public String fetchCurrentBillAmount(@QueryParam("userId") int userId) {
		System.out.println("Fetch Current Bill for UserId = " + userId);
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("userId", userId);
		String currentBill = MongoDBClient.fetchCurrentBillData(userId);
		System.out.println(currentBill);
		return currentBill;
	}
}