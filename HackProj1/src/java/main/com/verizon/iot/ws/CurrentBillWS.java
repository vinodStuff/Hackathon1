package com.verizon.iot.ws;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParem;
import javax.ws.rs.core.MediaType;

import com.verixon.iot.mongo.MongoDBClient;

@Path("currentBill")
public class CurrentBillWS {
	@GET
	// @Consumes("text/plain")
	@Produces(MediaType.TEXT_HTML)
	public String fetchCurrentBillAmount(@QueryParam("userId") int userID) {
		System.out.println("Fetch Current Bill for UserId = " + userId);
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("userId", userId);
		MongoDBClient dbc = new MongoDBClient();
		String currentBill = dbc.fetchCurrentBillData("userId", userId);
		System.out.println(currentBill);
		return currentBill;
	}
}