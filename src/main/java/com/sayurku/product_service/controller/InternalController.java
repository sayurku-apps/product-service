package com.sayurku.product_service.controller;

import com.sayurku.product_service.dto.InternalProductResponse;
import com.sayurku.product_service.dto.StockMovementRequest;
import com.sayurku.product_service.service.InternalStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Jalur antar-service. Gateway TIDAK meneruskan /internal/**, jadi tidak bisa dipanggil dari luar
// (di Docker cuma port gateway yang terbuka). Respons di sini berisi harga modal.
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final InternalStockService internalStockService;

    // GET /internal/products?ids=a,b,c
    @GetMapping("/products")
    public List<InternalProductResponse> findProducts(@RequestParam List<UUID> ids) {
        return internalStockService.findProducts(ids);
    }

    @PostMapping("/branches/{branchId}/stocks/reserve")
    public List<InternalProductResponse> reserve(@PathVariable UUID branchId,
                                                 @Valid @RequestBody StockMovementRequest request) {
        return internalStockService.reserve(branchId, request);
    }

    @PostMapping("/branches/{branchId}/stocks/release")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void release(@PathVariable UUID branchId, @Valid @RequestBody StockMovementRequest request) {
        internalStockService.release(branchId, request);
    }
}
