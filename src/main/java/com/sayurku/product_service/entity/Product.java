package com.sayurku.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


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

    // Vendor ada di SERVICE LAIN -> cuma disimpan ID-nya, tanpa relasi
    @Column(name = "vendor_id", nullable = false)
    private UUID vendorId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 20)
    private String unit;              // kg / ikat / buah

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "harvest_date", nullable = false)
    private LocalDate harvestDate;

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

    // ---------- logika domain ----------

    /** Tanggal produk ini nggak segar lagi */
    public LocalDate expiryDate() {
        return harvestDate.plusDays(freshnessDays);
    }

    /** Masih segar hari ini? */
    public boolean isStillFresh() {
        return !LocalDate.now().isAfter(expiryDate());
    }

    /** Sisa hari kesegaran, 0 kalau udah lewat */
    public long remainingFreshnessDays() {
        long left = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate());
        return Math.max(left, 0);
    }
}


