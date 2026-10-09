# Day 26 — Distributed Idempotency API

Java 17 + Spring Boot + PostgreSQL + Docker Compose. Demonstrates safe retries using an `Idempotency-Key`: repeated requests with the same key and same payload return the original result; reusing a key with a different payload returns HTTP 409.

## Run
```bash
docker compose up --build
```

## Create a simulated payment (PowerShell)
```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8081/api/payments -Headers @{'Idempotency-Key'='demo-001'} -ContentType 'application/json' -Body '{"customer":"aditya","amount":499}'
```

## Create a simulated payment (curl)
```bash
curl -X POST http://localhost:8081/api/payments -H "Content-Type: application/json" -H "Idempotency-Key: demo-001" -d '{"customer":"aditya","amount":499}'
```

Repeat the same request with the same key: response should show `replayed: true` and the same payment ID. Reuse the key with a different amount to see HTTP 409.

List records: `curl http://localhost:8081/api/payments`
Health: `curl http://localhost:8081/actuator/health`

## GitHub
Create repo `distributed-idempotency-day26`, then:
```bash
git init
git add .
git commit -m "Day 26: idempotency API"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/distributed-idempotency-day26.git
git push -u origin main
```

This is a learning demo, not a real payment processor. Production needs customer-scoped keys, expiry/retention, durable in-progress states, provider-side idempotency, retries, observability and load tests.
