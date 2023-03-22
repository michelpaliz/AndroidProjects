package com.user_manager_v1.repository;

import com.user_manager_v1.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Integer> {

    @Query(value = "SELECT person_id FROM person WHERE person_id = :email ", nativeQuery = true)
    List<String> checkUserEmail(@Param("email") String email);


}
