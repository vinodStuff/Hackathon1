package com.verizon.iot.bill;

import org.bson.Document;

import com.verizon.iot.mongo.MongoDBClient;;


public class BillingEngine {

	public static final double CalculateBill(int userId, String deviceCategory, double incomingDataVolume){
		String planId = MongoDBClient.fetchUserPlanId(userId);
		Document planDoc = MongoDBClient.fetchPlanDetails(planId);
		Document userUsageDoc = MongoDBClient.fetchCurrentUsageDetails(userId, deviceCategory);
		
		Double currentlyUsedDataVolume = userUsageDoc.getDouble("dataVolume");
		double dvtemp = currentlyUsedDataVolume + incomingDataVolume;
		Double dvToCharge = 0.00;
				
		Double rateForTheDevice = MongoDBClient.getRateMap().get(deviceCategory);
		
		double currCharges = 0.00;
		
		if ( currentlyUsedDataVolume >= plandoc.getDouble(deviceCategory)){
			dvToCharge = incomingDataVolume;
		}else if(dvtemp >= plandoc.getDouble(deviceCategory)){
			dvToCharge = dvtemp - plandoc.getDouble(deviceCategory);
		}
		
		if(rateForTheDevice != null){
			currCharges = dvToCharge * rateForTheDevice;
		}else {
			currCharges = dvToCharge * MongoDBClient.getRateMap().get("Others");
		}
		return currCharges;
	}
}
