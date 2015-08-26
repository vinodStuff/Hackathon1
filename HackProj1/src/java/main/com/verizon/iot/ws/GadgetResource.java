package com.verizon.iot.ws;

import javax.json.Json;

@Path("gadget")
public class GadgetResource {
	@GET
	// @Consumes("text/plain")
	@Produces(MediaType.TEXT_HTML)
	public String persistGadgetRequestG(@QueryParam("userId") int userID, @QueryParam("location") String location,
			@QueryParam("deviceCategory") String deviceCategory, @QueryParam("dataVolume") double dataVolume) {

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("userId", userId);
		job.add("location", location);
		job.add("deviceCategory", deviceCategory);
		job.add("dataVolume", dataVolume);

		JsonObject jo = job.build();

		MongoDBClient dbc = new MongoDBClient();
		String currentBill = dbc.insertIntoDataDumpTable(userId, location, deviceCategory, dataVolume);
		return currentBill;
	}
}