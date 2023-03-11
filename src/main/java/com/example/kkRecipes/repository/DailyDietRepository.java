package com.example.kkRecipes.repository;

import com.example.kkRecipes.model.DailyDiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDietRepository extends JpaRepository<DailyDiet, Long> {
    Page<DailyDiet> findAllByAddedByUserAndCompleted(long addedByUser, boolean completed, Pageable pageable);
}
