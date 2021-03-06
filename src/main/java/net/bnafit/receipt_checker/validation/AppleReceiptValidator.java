package net.bnafit.receipt_checker.validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bnafit.receipt_checker.util.AppleReceipt;

/*
 * Adapted from com.badlogicgames.gdxpay: gdx-pay-server by kdbeall
 * 
 * Copyright 2011 AUTHORS
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * AUTHORS Noble Master Games, just4phil (Heavily Loaded Games), Kees van Dieren
 * (Squins IT Solutions), Migeran
 * 
 */

/**
 * Validates Apple IAP receipts with the App Store.
 * 
 * @author kdbeall
 *
 */
public class AppleReceiptValidator {

	/** Initial capacity of the StringBuilder */
	private static final int INIT_CAPACITY = 2048;

	/** Sandbox URL */
	private final static String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

	/** Production URL */
	private final static String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";

	/** Maps Strings to JSON */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** Slf4j Logger */
	final Logger logger = LoggerFactory.getLogger(AppleReceiptValidator.class);

	/** True for sandbox mode. */
	private Boolean sandbox;

	/** Your app's shared secret */
	private String password;

	private boolean logging;

	public AppleReceiptValidator() {
		this(false, null, true);
	}

	public AppleReceiptValidator(Boolean sandbox) {
		this(sandbox, null, true);
	}

	/**
	 *
	 * @param sandbox
	 *            If true, SANDBOX, else production.
	 * @param password
	 *            Your application specific secret
	 * @param logging
	 *            If true, logs upon errors.
	 */
	public AppleReceiptValidator(Boolean sandbox, String password, boolean logging) {
		this.sandbox = sandbox;
		this.password = password;
		this.logging = logging;
	}

	/**
	 * Checks if a receipt is valid or not.
	 * 
	 * @param receiptData
	 *            The text of the receipt (not base64 encoded).
	 * @param excludeOldTransactions
	 *            Only used for iOS7 style app receipts that contain auto-renewable
	 *            or non-renewing subscriptions; set to null otherwise.
	 * @return If the receipt is valid.
	 */
	public boolean isValid(String receiptData, Boolean excludeOldTransactions) {
		final AppleReceipt receipt = new AppleReceipt(receiptData, password, excludeOldTransactions);
		final String requestJson = receipt.toJsonString();
		if (requestJson == null) {
			return false;
		}
		try {
			final URL url = new URL(sandbox ? SANDBOX_URL : PRODUCTION_URL);
			final HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(requestJson);
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
			JsonNode responseJson = mapper.readTree(response);
			int status = responseJson.get("status").asInt();

			/** verify the response: something like {"status":21004} etc... */
			return mapStatus(status);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Maps a status returned from Apple to success or failure. Logs an error using
	 * slf4j if logging is enabled.
	 * 
	 * @param status
	 * @return Whether the status is successful or not.
	 */
	private boolean mapStatus(int status) {
		String message = status + ": ";
		switch (status) {
		case 0:
			return true;
		case 21000:
			message += "App store could not read";
			break;
		case 21002:
			message += "Data was malformed";
			break;
		case 21003:
			message += "Receipt not authenticated";
			break;
		case 21004:
			message += "Shared secret does not match";
			break;
		case 21005:
			message += "Receipt server unavailable";
			break;
		case 21006:
			message += "Receipt valid but sub expired";
			break;
		case 21007:
			message += "Sandbox receipt sent to Production environment";
			break;
		case 21008:
			message += "Production receipt sent to Sandbox environment";
			break;
		default:
			/** unknown error code (nevertheless a problem) */
			message = "Unknown error: status code = " + status;
		}
		if (logging) {
			logger.info(message);
		}
		return false;
	}
}