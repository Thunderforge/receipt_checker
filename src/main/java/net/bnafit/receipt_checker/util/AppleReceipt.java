package net.bnafit.receipt_checker.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a receipt to be sent to Apple's servers to be validated.
 * 
 * @author kdbeall
 *
 */
public class AppleReceipt {

	private static final ObjectMapper mapper = makeMapper();

	@JsonProperty("receipt-data")
	public String receiptData;
	@JsonProperty("password")
	public String password;
	@JsonProperty("exclude-old-transactions")
	public Boolean excludeOldTransactions;

	public AppleReceipt(String receiptData, String password, Boolean excludeOldTransactions) {
		this.receiptData = Base64Util.toBase64(receiptData.getBytes());
		this.password = password;
		this.excludeOldTransactions = excludeOldTransactions;
	}

	/**
	 * Converts this object to a valid JSON string.
	 * 
	 * @return A JSON-formatted string.
	 */
	public String toJsonString() {
		try {
			return mapper.writeValueAsString(mapper.valueToTree(this));
		} catch (Exception e) {
			return null;
		}
	}

	private static ObjectMapper makeMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		return mapper;
	}
}
