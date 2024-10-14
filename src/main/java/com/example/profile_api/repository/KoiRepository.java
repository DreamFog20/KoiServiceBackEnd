
package com.example.profile_api.repository;

import com.example.profile_api.model.Koi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KoiRepository extends JpaRepository<Koi, Integer> {

}
