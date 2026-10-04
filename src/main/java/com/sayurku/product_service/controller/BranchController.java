package com.sayurku.product_service.controller;

import com.sayurku.product_service.dto.BranchRequest;
import com.sayurku.product_service.dto.BranchResponse;
import com.sayurku.product_service.dto.BranchStockRequest;
import com.sayurku.product_service.dto.BranchStockResponse;
import com.sayurku.product_service.security.AccessPolicy;
import com.sayurku.product_service.service.BranchService;
import com.sayurku.product_service.service.BranchStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BranchStockService branchStockService;

    @GetMapping
    public List<BranchResponse> findAll() {
        return branchService.findAll();
    }

    @GetMapping("/{id}")
    public BranchResponse findById(@PathVariable UUID id) {
        return branchService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponse create(@RequestHeader(AccessPolicy.ROLE) String role,
                                 @Valid @RequestBody BranchRequest request) {
        AccessPolicy.requireAdmin(role);
        return branchService.create(request);
    }

    @PutMapping("/{id}")
    public BranchResponse update(@RequestHeader(AccessPolicy.ROLE) String role,
                                 @PathVariable UUID id, @Valid @RequestBody BranchRequest request) {
        AccessPolicy.requireAdmin(role);
        return branchService.update(id, request);
    }

    // Katalog cabang: ?page= &size= &sort=product.name,asc
    @GetMapping("/{id}/stocks")
    public PagedModel<BranchStockResponse> findStocks(@PathVariable UUID id, Pageable pageable) {
        return new PagedModel<>(branchStockService.findByBranch(id, pageable));
    }

    // ADMIN: cabang mana pun. STAFF: hanya cabangnya sendiri.
    @PutMapping("/{id}/stocks/{productId}")
    public BranchStockResponse upsertStock(@RequestHeader(AccessPolicy.ROLE) String role,
                                           @RequestHeader(value = AccessPolicy.BRANCH_ID, required = false) UUID userBranchId,
                                           @PathVariable UUID id,
                                           @PathVariable UUID productId,
                                           @Valid @RequestBody BranchStockRequest request) {
        AccessPolicy.requireBranchAccess(role, userBranchId, id);
        return branchStockService.upsert(id, productId, request);
    }
}
