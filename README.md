# receipt_checker
A Java library to validate [Apple Receipts](https://developer.apple.com/library/content/releasenotes/General/ValidateAppStoreReceipt/Introduction.html)
Based on work done in [gdx-pay](https://github.com/libgdx/gdx-pay).
Intended to be simple to modify and use.
 
### Example Use
```
/** Your end use might look something like this */
public class FooApplePaymentController {

  private static final AppleReceiptValidator validator;
  
  public FooApplePaymentController(){
    /** Use sandbox and no password */ 
    AppleReceiptValidator validator = new AppleReceiptValidator(true, null);
  }
  /** Receiving a subscription update from Apple */ 
  public void receiveUpdate(){
    /** Depends on your framework */
    String fooUpdate = getRequestAsJson(...);
    if(!validator.isValid(fooUpdate)){
      //Error handling here
    }
    /** Your business logic here */
  }
  
}
```

### Dependencies
- [Jackson](https://github.com/FasterXML/jackson)

### Installation Steps
TODO (pom.xml & maven)
