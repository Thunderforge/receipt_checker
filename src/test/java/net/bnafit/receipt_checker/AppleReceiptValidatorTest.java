package net.bnafit.receipt_checker;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.bnafit.receipt_checker.validation.AppleReceiptValidator;

public class AppleReceiptValidatorTest {

	private AppleReceiptValidator validator;

	@Before
	public void setup() {
		validator = new AppleReceiptValidator(true, "foo");
	}

	/*******************************************************************************
	 * Adapted from com.badlogicgames.gdxpay: gdx-pay-server by kdbeall
	 * 
	 * Copyright 2011
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
	 ******************************************************************************/
	@Test
	public void testIsValid() {
		/** our sample receipt for the sandbox (returns error 21004) */
		String receipt = "{\n"
				+ "\"signature\" = \"AluGxOuMy+RT1gkyFCoD1i1KT3KUZl+F5FAAW0ELBlCUbC9dW14876aW0OXBlNJ6pXbBBFB8K0LDy6LuoAS8iBiq3529aRbVRUSKCPeCDZ7apC2zqFYZ4N7bSFDMeb92wzN0X/dELxlkRH4bWjO67X7gnHcN47qHoVckSlGo/mpbAAADVzCCA1MwggI7oAMCAQICCGUUkU3ZWAS1MA0GCSqGSIb3DQEBBQUAMH8xCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEzMDEGA1UEAwwqQXBwbGUgaVR1bmVzIFN0b3JlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA5MDYxNTIyMDU1NloXDTE0MDYxNDIyMDU1NlowZDEjMCEGA1UEAwwaUHVyY2hhc2VSZWNlaXB0Q2VydGlmaWNhdGUxGzAZBgNVBAsMEkFwcGxlIGlUdW5lcyBTdG9yZTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMrRjF2ct4IrSdiTChaI0g8pwv/cmHs8p/RwV/rt/91XKVhNl4XIBimKjQQNfgHsDs6yju++DrKJE7uKsphMddKYfFE5rGXsAdBEjBwRIxexTevx3HLEFGAt1moKx509dhxtiIdDgJv2YaVs49B0uJvNdy6SMqNNLHsDLzDS9oZHAgMBAAGjcjBwMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAUNh3o4p2C0gEYtTJrDtdDC5FYQzowDgYDVR0PAQH/BAQDAgeAMB0GA1UdDgQWBBSpg4PyGUjFPhJXCBTMzaN+mV8k9TAQBgoqhkiG92NkBgUBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAEaSbPjtmN4C/IB3QEpK32RxacCDXdVXAeVReS5FaZxc+t88pQP93BiAxvdW/3eTSMGY5FbeAYL3etqP5gm8wrFojX0ikyVRStQ+/AQ0KEjtqB07kLs9QUe8czR8UGfdM1EumV/UgvDd4NwNYxLQMg4WTQfgkQQVy8GXZwVHgbE/UC6Y7053pGXBk51NPM3woxhd3gSRLvXj+loHsStcTEqe9pBDpmG5+sk4tw+GK3GMeEN5/+e1QT9np/Kl1nj+aBw7C0xsy0bFnaAd1cSS6xdory/CUvM6gtKsmnOOdqTesbp0bs8sn6Wqs0C9dgcxRHuOMZ2tm8npLUm7argOSzQ==\";\n"
				+ "\"purchase-info\" = \"ewoJInF1YW50aXR5IiA9ICIxIjsKCSJwdXJjaGFzZS1kYXRlIiA9ICIyMDExLTEwLTEyIDIwOjA1OjUwIEV0Yy9HTVQiOwoJIml0ZW0taWQiID0gIjQ3MjQxNTM1MyI7CgkiZXhwaXJlcy1kYXRlLWZvcm1hdHRlZCIgPSAiMjAxMS0xMC0xMiAyMDoxMDo1MCBFdGMvR01UIjsKCSJleHBpcmVzLWRhdGUiID0gIjEzMTg0NTAyNTAwMDAiOwoJInByb2R1Y3QtaWQiID0gImNvbS5kYWlseWJ1cm4ud29kMW1vbnRoIjsKCSJ0cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDAwOTk1NzYwMiI7Cgkib3JpZ2luYWwtcHVyY2hhc2UtZGF0ZSIgPSAiMjAxMS0xMC0xMiAyMDowNTo1MiBFdGMvR01UIjsKCSJvcmlnaW5hbC10cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDAwOTk1NzYwMiI7CgkiYmlkIiA9ICJjb20uZGFpbHlidXJuLndvZCI7CgkiYnZycyIgPSAiMC4wLjgiOwp9\";\n"
				+ "\"environment\" = \"Sandbox\";\n" + "\"pod\" = \"100\";\n" + "\"signing-status\" = \"0\";\n" + "}\n";
		assertTrue(!validator.isValid(receipt, null));
	}

	/**
	 * This test returns a status 21002 because AppleReceipt formats the JSON
	 * properly using Jackson
	 */
	@Test
	public void testIsValidBadFormat() {
		String receipt = "";
		assertTrue(!validator.isValid(receipt, null));
	}

	@Test
	public void testIsValidMalformedJson() {
		String receipt = "{ }";
		assertTrue(!validator.isValid(receipt, null));
	}

	@Test
	public void testIsValidMalformedJson2() {
		/** our sample receipt for the sandbox (returns error 21004) */
		String receipt = "{ test test }";
		assertTrue(!validator.isValid(receipt, null));
	}

}
