package com.cineverse.booking.repository;

import com.cineverse.booking.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, String> {
    List<Theatre> findByOwnerId(String ownerId);
}
