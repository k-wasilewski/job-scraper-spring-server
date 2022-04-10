package com.example.jobscraperspringserver.repositories;

import com.example.jobscraperspringserver.types.ProductWrapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository <ProductWrapper, Long> {

}