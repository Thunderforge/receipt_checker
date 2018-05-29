package net.bnafit.receipt_checker.util;

/*******************************************************************************
 * Copyright 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * AUTHORS
 * This is the official list of the AUTHORS of gdx-pay
 * Names should be added to this file as
 * Name or Organization <email address>
 * The email address is not required for organizations.
 * Noble Master Games
 * just4phil (Heavily Loaded Games)
 * Kees van Dieren (Squins IT Solutions)
 * Migeran
 * 
 ******************************************************************************/

import java.util.Date;

public final class PaymentTransaction {

	public static final String REVERSAL_TEXT_CANCELLED = "Cancelled";
	public static final String REVERSAL_TEXT_REFUNDED = "Refunded";

	/** Item identifier/SKU number. */
	private String identifier;

	/** The store name. */
	private String storeName;
	/** A unique order ID. */
	private String orderId; // unique identifier of a purchase.
	private String requestId = null; // Represents the unique id of any in-app purchasing request.
	private String userId = null; // Represents the unique user id of any in-app purchase

	/** The original purchase time in milliseconds since the epoch (Jan 1, 1970). */
	private Date purchaseTime;
	/**
	 * The title/info for the purchase (or null for unknown). E.g. "Purchased: 100
	 * Coins".
	 */
	private String purchaseText;
	/**
	 * How much was originally charged in the lowest denomination (or -1 for
	 * unknown). E.g. if the cost was USD 4.99, then this field contains 499.
	 */
	private int purchaseCost;
	/**
	 * The ISO 4217 currency code for price (or null for unknown). For example, if
	 * price is specified in British pounds sterling then this field is "GBP".
	 */
	private String purchaseCostCurrency;

	/**
	 * The original refund/cancellation time or null for non-refunded. Might not be
	 * accurate if it cannot be determined.
	 */
	private Date reversalTime;
	/**
	 * The title/info for the refund (or null for unknown). E.g. "Refunded" or
	 * "Cancelled".
	 */
	private String reversalText;

	/** The original data string from the purchase (or null for unknown). */
	private String transactionData;
	/**
	 * A signature for the purchase data string for validation of the data (or null
	 * for unknown).
	 */
	private String transactionDataSignature;

	/** The item identifier/SKU that matches our item id in the IAP service. */
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/** Returns one of the store names as defined in PurchaseManagerConfig. */
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
	 * The original transaction identifier which is unique for each purchase
	 * (doesn't change). It represents an unique ID for the purchase on the
	 * corresponding store.
	 */
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * The original request identifier which is unique for each request (doesn't
	 * change). It represents an unique ID for the request on the corresponding
	 * store.
	 */
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * The user identifier which is unique for each request / purchase (doesn't
	 * change).
	 */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns true if the order is considered valid, i.e. in purchased state
	 * (non-refunded/cancelled).
	 */
	public boolean isPurchased() {
		return reversalTime == null;
	}

	/** The original purchase time in milliseconds since the epoch (Jan 1, 1970). */
	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	/**
	 * The title/info for the purchase (or null for unknown). E.g. "Purchased: 100
	 * Coins".
	 */
	public String getPurchaseText() {
		return purchaseText;
	}

	public void setPurchaseText(String purchaseText) {
		this.purchaseText = purchaseText;
	}

	/**
	 * How much was originally charged in the lowest denomination (or 0 for
	 * unknown). E.g. if the cost was USD 4.99, then this field contains 499.
	 * 
	 * @return price in cents, or zero if unknown
	 */
	public int getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(int purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	/**
	 * The ISO 4217 currency code for price (or null for unknown). For example, if
	 * price is specified in British pounds sterling then this field is "GBP".
	 */
	public String getPurchaseCostCurrency() {
		return purchaseCostCurrency;
	}

	public void setPurchaseCostCurrency(String purchaseCostCurrency) {
		this.purchaseCostCurrency = purchaseCostCurrency;
	}

	/**
	 * The original refund/cancellation time in milliseconds since the epoch (Jan 1,
	 * 1970) or null for non-refunded.
	 */
	public Date getReversalTime() {
		return reversalTime;
	}

	public void setReversalTime(Date reversalTime) {
		this.reversalTime = reversalTime;
	}

	/**
	 * The title/info for the refund (or null for unknown). E.g. "Refunded" or
	 * "Cancelled".
	 */
	public String getReversalText() {
		return reversalText;
	}

	public void setReversalText(String reversalText) {
		this.reversalText = reversalText;
	}

	public String getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(String transactionData) {
		this.transactionData = transactionData;
	}

	/** The original data string from the purchase (or null for unknown). */
	public String getTransactionDataSignature() {
		return transactionDataSignature;
	}

	/**
	 * A signature for the purchase data string for validation of the data (or null
	 * for unknown).
	 */
	public void setTransactionDataSignature(String transactionDataSignature) {
		this.transactionDataSignature = transactionDataSignature;
	}

	@Override
	public String toString() {
		return "Transaction{" + "identifier='" + identifier + '\'' + ", storeName='" + storeName + '\'' + ", orderId='"
				+ orderId + '\'' + ", requestId='" + requestId + '\'' + ", userId='" + userId + '\'' + ", purchaseTime="
				+ purchaseTime + ", purchaseText='" + purchaseText + '\'' + ", purchaseCost=" + purchaseCost
				+ ", purchaseCostCurrency='" + purchaseCostCurrency + '\'' + ", reversalTime=" + reversalTime
				+ ", reversalText='" + reversalText + '\'' + ", transactionData='" + transactionData + '\''
				+ ", transactionDataSignature='" + transactionDataSignature + '\'' + '}';
	}
}