package de.jformchecker.utils;

import java.util.Map;

import de.jformchecker.FormChecker;
import de.jformchecker.request.Request;
import de.jformchecker.request.SampleRequest;

public class RequestBuilders {

	public static final String FC_ID = "id44";

	public static Request buildExampleHttpRequest() {
		SampleRequest request = new SampleRequest();
		request.setParameter("firstname", "Jochen Pier<bold>");
		return request;
	}

	public static Request buildExampleHttpRequest(Map<String, String> reqVals) {
		SampleRequest request = new SampleRequest();

		for (String key : reqVals.keySet()) {
			request.setParameter(key, reqVals.get(key));
		}
		return request;
	}

	public static Request buildEmptyHttpRequest() {
		SampleRequest request = new SampleRequest();
		return request;
	}

	public static FormChecker buildFcWithEmptyRequest() {
		return new FormChecker(RequestBuilders.buildEmptyHttpRequest());
	}

}
