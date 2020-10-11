package com.auspos.suburb.repository;

import com.auspos.suburb.domain.Suburb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuburbRepository extends JpaRepository<Suburb, String> {

    List<Suburb> findByPostCode(long postCode);

    List<Suburb> findBySuburbNameIgnoreCase(String suburbName);

    List<Suburb> findBySuburbNameIgnoreCaseAndPostCode(String suburbName, long postCode);
}
