package com.sayurku.product_service.service;

import com.sayurku.product_service.dto.BranchRequest;
import com.sayurku.product_service.dto.BranchResponse;
import com.sayurku.product_service.entity.Branch;
import com.sayurku.product_service.exception.DuplicateResourceException;
import com.sayurku.product_service.exception.ResourceNotFoundException;
import com.sayurku.product_service.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public List<BranchResponse> findAll() {
        return branchRepository.findByIsActiveTrueOrderByNameAsc().stream()
                .map(BranchResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BranchResponse findById(UUID id) {
        return BranchResponse.from(getEntityById(id));
    }

    @Transactional
    public BranchResponse create(BranchRequest request) {
        if (branchRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Cabang '" + request.name() + "' sudah ada");
        }

        Branch branch = Branch.builder()
                .name(request.name())
                .address(request.address())
                .phone(request.phone())
                .build();

        return BranchResponse.from(branchRepository.save(branch));
    }

    @Transactional
    public BranchResponse update(UUID id, BranchRequest request) {
        Branch branch = getEntityById(id);
        if (branchRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new DuplicateResourceException("Cabang '" + request.name() + "' sudah ada");
        }

        branch.setName(request.name());
        branch.setAddress(request.address());
        branch.setPhone(request.phone());

        return BranchResponse.from(branch);
    }

    @Transactional(readOnly = true)
    public Branch getEntityById(UUID id) {
        // cabang yang sudah ditutup dianggap tidak ada
        return branchRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cabang tidak ditemukan: " + id));
    }
}
