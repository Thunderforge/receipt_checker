package net.bnafit.receipt_checker;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.bnafit.receipt_checker.util.AppleReceipt;

public class AppleReceiptTest {

	@Test
	public void testJsonConversion() {
		AppleReceipt receipt = new AppleReceipt("foo", "bar", true);
		assertTrue("{\"receipt-data\":\"Zm9v\",\"password\":\"bar\",\"exclude-old-transactions\":true}"
				.equals(receipt.toJsonString()));
		AppleReceipt receipt2 = new AppleReceipt("foo", "bar", null);
		assertTrue("{\"receipt-data\":\"Zm9v\",\"password\":\"bar\"}".equals(receipt2.toJsonString()));
	}
}
