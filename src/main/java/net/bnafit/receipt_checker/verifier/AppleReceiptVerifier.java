package net.bnafit.receipt_checker.verifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import net.bnafit.receipt_checker.util.Base64Util;
import net.bnafit.receipt_checker.util.PaymentTransaction;

/**
 * Hello world!
 *
 */
public class AppleReceiptVerifier {

	/** Sandbox URL */
	private final static String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

	/** Production URL */
	private final static String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";

	/** True for sandbox mode. */
	private boolean sandbox;

	public AppleReceiptVerifier() {
		this(false);
	}

	public AppleReceiptVerifier(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public boolean isValid(PaymentTransaction transaction) {
		// the transaction data is our original == receipt!
		String receipt = transaction.getTransactionData();

		// encode the data
		final String receiptData = Base64Util.toBase64(receipt.getBytes());
		final String jsonData = "{\"receipt-data\" : \"" + receiptData + "\"}";
		try {
			// send the data to Apple
			final URL url = new URL(sandbox ? SANDBOX_URL : PRODUCTION_URL);
			final HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(jsonData);
			wr.flush();

			// obtain the response
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = rd.readLine();
			wr.close();
			rd.close();

			// verify the response: something like {"status":21004} etc...
			int status = Integer.parseInt(line.substring(line.indexOf(":") + 1, line.indexOf("}")));
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
				// unknown error code (nevertheless a problem)
				System.out.println("Unknown error: status code = " + status);
				return false;
			}
		} catch (IOException e) {
			// I/O-error: let's assume bad news...
			System.err.println("I/O error during verification: " + e);
			e.printStackTrace();
			return false;
		}
	}

}