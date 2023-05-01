package kz.zhelezyaka.spring6reactivemongoexperiments.repositories;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
