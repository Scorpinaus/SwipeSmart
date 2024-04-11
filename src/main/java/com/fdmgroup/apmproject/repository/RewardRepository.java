package com.fdmgroup.apmproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Reward;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Integer> {

}
