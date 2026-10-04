package com.sayurku.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

// Stok satu produk di satu cabang. Kombinasi (produk, cabang) unik: satu baris saja.
// Product dan Branch ada di service yang sama, jadi boleh pakai relasi + FK beneran.
@Entity
@Table(name = "branch_stocks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "branch_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchStock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "harvest_date", nullable = false)
    private LocalDate harvestDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void touch() {
        updatedAt = LocalDateTime.now();
    }

    // ---------- logika domain ----------
    // Kesegaran = tanggal panen stok di cabang ini + masa segar produknya.

    /** Tanggal stok ini nggak segar lagi */
    public LocalDate expiryDate() {
        return harvestDate.plusDays(product.getFreshnessDays());
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
