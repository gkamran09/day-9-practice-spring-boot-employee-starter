package com.afs.restapi.repository;

import com.afs.restapi.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<Company,Long> {
}
