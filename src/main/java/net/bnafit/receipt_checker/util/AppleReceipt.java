package net.bnafit.receipt_checker.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
		/** Nice to have... https://github.com/FasterXML/jackson-modules-java8 */
		mapper.registerModule(new Jdk8Module());
		mapper.registerModule(new JavaTimeModule());
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public AppleReceipt(String receiptData, String password, Boolean excludeOldTransactions) {
		this.receiptData = Base64Util.toBase64(receiptData.getBytes());
		this.password = password;
		this.excludeOldTransactions = excludeOldTransactions;
	}

	public JsonNode toJson() {
		return mapper.valueToTree(this);
	}
}
