package com.sayurku.product_service.repository;

import com.sayurku.product_service.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
    List<Branch> findByIsActiveTrueOrderByNameAsc();

    Optional<Branch> findByIdAndIsActiveTrue(UUID id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
