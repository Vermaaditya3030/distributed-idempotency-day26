# Architecture
Client -> Payment API (:8081) -> PostgreSQL `payment_record`.

1. Require `Idempotency-Key`.
2. Validate customer and positive amount.
3. Hash canonical request fields with SHA-256.
4. Save one simulated payment under a unique key.
5. Same key + same payload returns the stored result.
6. Same key + different payload returns HTTP 409.

The unique database constraint protects against concurrent duplicate submissions. For production, handle in-progress requests and external payment-provider timeouts explicitly.
