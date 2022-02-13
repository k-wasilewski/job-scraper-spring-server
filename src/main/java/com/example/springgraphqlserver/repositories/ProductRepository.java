package com.example.springgraphqlserver.repositories;

import com.example.springgraphqlserver.types.ProductWrapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository <ProductWrapper, Long> {

}