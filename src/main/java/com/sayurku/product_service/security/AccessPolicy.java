package com.sayurku.product_service.security;

import com.sayurku.product_service.exception.ForbiddenException;

import java.util.UUID;

// Aturan siapa boleh mengubah apa (mirip Policy/Gate di Laravel).
// Identitas datang dari header yang dipasang api-gateway setelah token diperiksa:
// X-User-Role dan, khusus STAFF, X-User-Branch-Id.
public final class AccessPolicy {

    public static final String ROLE = "X-User-Role";
    public static final String BRANCH_ID = "X-User-Branch-Id";

    private AccessPolicy() {}

    /** Katalog (kategori, produk, harga) dan cabang: hanya pusat */
    public static void requireAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new ForbiddenException("Hanya ADMIN");
        }
    }

    /** Stok cabang: ADMIN semua cabang, STAFF hanya cabang tempatnya bekerja */
    public static void requireBranchAccess(String role, UUID userBranchId, UUID branchId) {
        if ("ADMIN".equals(role)) {
            return;
        }
        if (!"STAFF".equals(role)) {
            throw new ForbiddenException("Hanya ADMIN atau STAFF");
        }
        if (!branchId.equals(userBranchId)) {
            throw new ForbiddenException("STAFF hanya boleh mengubah stok cabangnya sendiri");
        }
    }
}
