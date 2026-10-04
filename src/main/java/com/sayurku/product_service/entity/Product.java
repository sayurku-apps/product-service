package com.sayurku.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;


// Katalog pusat: satu produk berlaku untuk semua cabang dengan harga jual yang sama.
// Stok dan tanggal panen per cabang ada di BranchStock.
@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;         // harga jual

    // Harga modal. Rahasia dapur: jangan ditampilkan ke pembeli.
    // Margin = price - costPrice, disalin ke order item saat checkout.
    @Column(name = "cost_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(nullable = false, length = 20)
    private String unit;              // kg / ikat / buah

    // Sifat sayurnya (bayam tahan 2 hari, kentang 30 hari), jadi tetap di katalog
    @Column(name = "freshness_days", nullable = false)
    private Integer freshnessDays;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
