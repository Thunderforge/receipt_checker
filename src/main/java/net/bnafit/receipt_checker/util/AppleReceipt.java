package net.bnafit.receipt_checker.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppleReceipt {

	private static final ObjectMapper mapper = makeMapper();

	@JsonProperty("receipt-data")
	public String receiptData;
	@JsonProperty("password")
	public String password;
	@JsonProperty("exclude-old-transactions")
	public Boolean excludeOldTransactions;

	public static ObjectMapper makeMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		return mapper;
	}

	public AppleReceipt(String receiptData, String password, Boolean excludeOldTransactions) {
		this.receiptData = Base64Util.toBase64(receiptData.getBytes());
		this.password = password;
		this.excludeOldTransactions = excludeOldTransactions;
	}

	public String toJsonString() {
		try {
			return mapper.writeValueAsString(mapper.valueToTree(this));
		} catch (Exception e) {
			return null;
		}
	}
}
