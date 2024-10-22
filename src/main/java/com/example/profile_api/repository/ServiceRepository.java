
package com.example.profile_api.repository;

import com.example.profile_api.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByServiceType(String serviceType);

    List<Service> findByNameContaining(String name);
}
