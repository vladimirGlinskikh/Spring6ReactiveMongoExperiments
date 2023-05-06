package kz.zhelezyaka.spring6reactivemongoexperiments.services;

import kz.zhelezyaka.spring6reactivemongoexperiments.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<CustomerDTO> findByCustomerName(String customerName);

    Mono<CustomerDTO> findFirstByCustomerName(String customerName);

    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO);

    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> getById(String customerId);

    Flux<CustomerDTO> listCustomers();

    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(String customerId);
}
