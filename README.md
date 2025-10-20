# Spring Boot Inventory & Order API

## Cara Menjalankan

1. Pastikan sudah install Java 21 dan Maven.
2. Konfigurasi database di `src/main/resources/application.properties` (default: MySQL).
3. Jalankan aplikasi:
   ```bash
   ./mvnw spring-boot:run
   ```
   atau
   ```bash
   mvn spring-boot:run
   ```

## Contoh API

### Create Order
```http
POST /api/orders
Content-Type: application/json

{
  "itemId": 1,
  "qty": 5
}
```

### Update Order (Qty)
```http
PUT /api/orders/1
Content-Type: application/json

{
  "qty": 10
}
```

### Update Order (Item & Qty)
```http
PUT /api/orders/1
Content-Type: application/json

{
  "itemId": 2,
  "qty": 3
}
```

### Delete Order
```http
DELETE /api/orders/1
```

### Get Order by ID
```http
GET /api/orders/1
```

### Get All Orders (pagination)
```http
GET /api/orders?page=0&size=10
```

### Error Response (Insufficient Stock)
```json
{
  "message": "Insufficient stock. Available: 10, Requested: 15",
  "status": "INSUFFICIENT_STOCK"
}
```

### Error Response (Not Found)
```json
{
  "message": "Order not found with id: 99",
  "status": "NOT_FOUND"
}
```

### Inventory API (tambahkan sesuai kebutuhan)
// Endpoint dan contoh request/response untuk inventory bisa ditambahkan jika diperlukan

## Postman Collection

Download & import:  
[Postman Collection Example](https://.postman.co/workspace/My-Workspace~f2d11fd1-fa7e-4b47-8772-09a2fc7bef54/collection/30656608-e8db712c-5e2e-4aec-a5ba-d8ccbc75da7d?action=share&creator=30656608)
