package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MongRepository extends JpaRepository<Mong, Long> {
}
