package com.user_manager_v1.repository;

import com.user_manager_v1.models.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
}
