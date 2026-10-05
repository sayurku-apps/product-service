# product-service

Bagian dari [SAYURKU](https://github.com/sayurku-apps), e-commerce sayuran segar berarsitektur microservices (Spring Boot 4, Java 26, PostgreSQL).

Service ini mengurus **katalog dan stok**: kategori, produk, cabang toko, dan stok tiap produk di tiap cabang.

- Port: `8082`
- Database: `sayurku_product_db`
- Dipanggil dari luar lewat [api-gateway](https://github.com/sayurku-apps/api-gateaway) (`:8080`)

## Model bisnis

Satu toko dengan beberapa cabang. Katalog `Product` berlaku untuk semua cabang dengan harga jual yang sama. Stok dan tanggal panen dicatat per cabang di `BranchStock`.

- `price` = harga jual, `costPrice` = harga modal (rahasia, tidak tampil di GET publik).
- **Kesegaran:** `freshnessDays` di produk (bayam 2 hari, kentang 30 hari) + `harvestDate` di stok cabang. Respons stok menyertakan `expiryDate`, `remainingFreshnessDays`, dan `stillFresh`.
- Checkout mengurangi stok dengan satu `UPDATE ... WHERE stock >= qty AND harvest_date >= ...`, jadi stok tidak bisa minus walau ada dua checkout bersamaan, dan stok yang sudah tidak segar ditolak (409).

## Hak akses

Identitas datang dari header yang dipasang api-gateway (`X-User-Role`, `X-User-Branch-Id`), dicek di `AccessPolicy`.

| Aksi | Siapa |
|---|---|
| Melihat katalog, cabang, stok | siapa saja (tanpa login) |
| Kategori, produk, harga, cabang | ADMIN |
| Mengisi stok cabang | ADMIN (semua cabang), STAFF (hanya cabangnya sendiri) |

## Endpoint

| Method | Path | Akses | Keterangan |
|---|---|---|---|
| GET | `/api/categories` | publik | |
| POST | `/api/categories` | ADMIN | `{name, iconUrl}` |
| GET | `/api/products?categoryId=&page=&size=&sort=name,asc` | publik | Produk aktif |
| GET | `/api/products/{id}` | publik | |
| GET | `/api/products/{id}/stocks` | publik | Stok produk ini di tiap cabang |
| POST | `/api/products` | ADMIN | `{categoryId, name, description, price, costPrice, unit, freshnessDays, imageUrl}` |
| PUT | `/api/products/{id}` | ADMIN | |
| DELETE | `/api/products/{id}` | ADMIN | Soft delete (nonaktif) |
| GET | `/api/branches` | publik | Cabang aktif |
| GET | `/api/branches/{id}` | publik | |
| POST | `/api/branches` | ADMIN | `{name, address, phone}` |
| PUT | `/api/branches/{id}` | ADMIN | |
| GET | `/api/branches/{id}/stocks?page=&size=` | publik | Katalog satu cabang (stok > 0) |
| PUT | `/api/branches/{id}/stocks/{productId}` | ADMIN / STAFF cabang itu | `{stock, harvestDate}`; dibuat kalau belum ada |

Jalur antar-service (dipanggil order-service, wajib header `X-Internal-Token`, tidak dirute gateway):

| Method | Path | Keterangan |
|---|---|---|
| GET | `/internal/products?ids=a,b` | Data produk termasuk `costPrice` |
| POST | `/internal/branches/{id}/stocks/reserve` | `{items:[{productId, quantity}]}`, kurangi stok, kembalikan snapshot harga |
| POST | `/internal/branches/{id}/stocks/release` | Kembalikan stok (pesanan batal) |

## Menjalankan

Butuh Java 26 dan PostgreSQL dengan database `sayurku_product_db`.

```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
# isi password database dan internal.token (sama di semua service)
./mvnw spring-boot:run        # Windows PowerShell: .\mvnw.cmd spring-boot:run
```

Cara paling mudah menjalankan semua service sekaligus: Docker Compose di repo [infra](https://github.com/sayurku-apps/infra).

## Konfigurasi

| Properti | Env var | Keterangan |
|---|---|---|
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` | Password Postgres |
| `internal.token` | `INTERNAL_TOKEN` | Rahasia bersama untuk `/internal/**` |

## Database

Skema dikelola **Flyway** (`src/main/resources/db/migration`). Perubahan skema = file baru `V2__...sql`, jangan mengubah file lama. Hibernate hanya memvalidasi (`ddl-auto=validate`).

## Tes

```bash
./mvnw test
```

Tes end-to-end lintas service ada di repo infra (`tests/e2e_*.py`).
