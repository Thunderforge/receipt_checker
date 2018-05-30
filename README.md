# receipt_checker
A Java library to validate [Apple Receipts](https://developer.apple.com/library/content/releasenotes/General/ValidateAppStoreReceipt/Introduction.html)
Based on work done in [gdx-pay](https://github.com/libgdx/gdx-pay).
Intended to be simple to modify and use.
 
### Example Use
```
/** Your end use might look something like this */
public class FooApplePaymentController {

  /** Use sandbox, no password, and logging */ 
  private static final AppleReceiptValidator validator = new AppleReceiptValidator(true, null, true);
  
  public FooApplePaymentController(){
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
- [Jackson](https://github.com/FasterXML/jackson}
- [SLF4J](https://www.slf4j.org/)

### Installation Steps
TODO (pom.xml & maven)
