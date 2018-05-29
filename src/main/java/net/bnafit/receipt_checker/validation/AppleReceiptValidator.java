package net.bnafit.receipt_checker.validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bnafit.receipt_checker.util.AppleReceipt;

public class AppleReceiptValidator {

	/** Initial capacity of the StringBuilder */
	private static final int INIT_CAPACITY = 2048;

	/** Sandbox URL */
	private final static String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

	/** Production URL */
	private final static String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";

	/** Maps Strings to JSON */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** True for sandbox mode. */
	private Boolean sandbox;

	/** Your app's shared secret */
	private String secret;

	public AppleReceiptValidator() {
		this(false, null);
	}

	public AppleReceiptValidator(Boolean sandbox, String secret) {
		this.sandbox = sandbox;
		this.secret = secret;
	}

	/**
	 * Checks if a receipt is valid or not.
	 * @param receiptData The text of the receipt (not base64 encoded).
	 * @param excludeOldTransactions 
	 * @return If the receipt is valid.
	 */
	public boolean isValid(String receiptData, Boolean excludeOldTransactions) {
		final AppleReceipt receipt = new AppleReceipt(receiptData, secret, excludeOldTransactions);
		final String jsonData = receipt.toJson().toString();
		System.out.println(jsonData);
		try {
			final URL url = new URL(sandbox ? SANDBOX_URL : PRODUCTION_URL);
			final HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(jsonData);
			wr.flush();

			/** obtain the response */
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder builder = new StringBuilder(INIT_CAPACITY);
			String line;
			while ((line = rd.readLine()) != null) {
				builder.append(line);
			}
			wr.close();
			rd.close();
			String response = builder.toString();
			System.out.println(builder.toString());
			JsonNode actualObj = mapper.readTree(response);
			int status = actualObj.get("status").asInt();

			/** verify the response: something like {"status":21004} etc... */
			return mapStatus(status);

		} catch (Exception e) {
			/** I/O-error: let's assume bad news... */
			e.printStackTrace();
			return false;
		}
	}

	private boolean mapStatus(int status) {
		switch (status) {
		case 0:
			return true;
		case 21000:
			System.out.println(status + ": App store could not read");
			return false;
		case 21002:
			System.out.println(status + ": Data was malformed");
			return false;
		case 21003:
			System.out.println(status + ": Receipt not authenticated");
			return false;
		case 21004:
			System.out.println(status + ": Shared secret does not match");
			return false;
		case 21005:
			System.out.println(status + ": Receipt server unavailable");
			return false;
		case 21006:
			System.out.println(status + ": Receipt valid but sub expired");
			return false;
		case 21007:
			System.out.println(status + ": Sandbox receipt sent to Production environment");
			return false;
		case 21008:
			System.out.println(status + ": Production receipt sent to Sandbox environment");
			return false;
		default:
			/** unknown error code (nevertheless a problem) */
			System.out.println("Unknown error: status code = " + status);
			return false;
		}
	}
}