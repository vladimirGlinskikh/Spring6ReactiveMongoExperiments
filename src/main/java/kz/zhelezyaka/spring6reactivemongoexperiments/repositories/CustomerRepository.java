package kz.zhelezyaka.spring6reactivemongoexperiments.repositories;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Customer> findFirstByCustomerName(String customerName);
    Flux<Customer> findByCustomerName(String customerName);
}
