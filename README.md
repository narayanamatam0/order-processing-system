# Order Processing System

Java 17 / Spring Boot 3.5 multi-module learning project.

| Module | Port | Current functionality |
| --- | --- | --- |
| common-events | None | Shared Kafka contracts; not a runnable application |
| order-service | 8081 | Create and retrieve orders through REST |
| inventory-service | 8082 | Application foundation and health endpoint |
| payment-service | 8083 | Application foundation and health endpoint |

## Build and run

From the project root in Windows PowerShell:

```powershell
.\mvnw.cmd clean verify
java -jar .\order-service\target\order-service-0.0.1-SNAPSHOT.jar
```

From Git Bash, Linux, or macOS:

```bash
./mvnw clean verify
java -jar order-service/target/order-service-0.0.1-SNAPSHOT.jar
```

In IntelliJ, use Java 17, reload the root Maven project, then run
`com.ecommerce.order.OrderServiceApplication`.
Stop the previous instance before starting the updated service on port 8081.
The wrapper downloads Maven and dependencies on the first build.

## Postman walkthrough

1. Create a POST request to `http://localhost:8081/api/orders`.
2. Select Body -> raw -> JSON (`Content-Type: application/json`).
3. Send:

```json
{
  "customerId": "CUSTOMER-101",
  "items": [
    {"productId": "PRODUCT-101", "quantity": 2, "unitPrice": 500.00}
  ],
  "currency": "INR"
}
```

Expect HTTP **201 Created**, a generated `orderId`, `status: PENDING`,
`totalAmount: 1000.00` (JSON clients may display 1000.0 or 1000), and a
`Location` header pointing to `/api/orders/{orderId}`.

4. Copy the actual orderId from the response and send
   `GET http://localhost:8081/api/orders/{orderId}`. Expect HTTP **200 OK**
   and the same order.
5. Send quantity 0: expect HTTP **400 Bad Request** and an `errors` map
   identifying the invalid field. Empty items, blank identifiers, null items,
   negative prices, and prices with more than two fractional digits are rejected.
6. Send quantity 1.5: expect HTTP **400**, rather than silently truncating it.
7. GET an unknown valid UUID: expect HTTP **404 Not Found**.
8. GET an invalid UUID such as `not-a-uuid`: expect HTTP **400 Bad Request**.

`GET http://localhost:8081/actuator/health` checks startup health.

## Read the code in this order

1. `dto/CreateOrderRequest.java`: JSON request contract and Bean Validation.
2. `controller/OrderController.java`: HTTP mapping, `@Valid`, constructor
   injection, 201 response and Location header.
3. `service/OrderService.java`: item mapping, BigDecimal total calculation,
   generated UUID, and thread-safe in-memory storage.
4. `dto/OrderResponse.java`: immutable response with a defensive list copy.
5. `exception/ApiExceptionHandler.java`: JSON Problem Detail error responses.
6. `OrderApiIntegrationTest.java`: exercise the real controller, validation,
   service, and error handler using MockMvc.

## Scope of this learning step

Orders are stored only in one running process's memory and are lost on restart.
There is no persistence, authentication, duplicate-request protection, or Kafka
publication yet. All created orders stay PENDING. Kafka and the other services
are not required to exercise these two REST endpoints. This is not an end-to-end
checkout system yet.

For this POC the request supplies unit prices, with at most two fractional digits,
and currency is validated as three uppercase letters only, not against an ISO
currency registry. Production pricing must come from a trusted catalog. The next
step is database persistence, followed by Kafka publication and consumers.
