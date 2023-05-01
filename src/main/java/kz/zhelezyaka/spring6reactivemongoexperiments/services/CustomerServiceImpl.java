package kz.zhelezyaka.spring6reactivemongoexperiments.services;

import kz.zhelezyaka.spring6reactivemongoexperiments.model.CustomerDTO;
import reactor.core.publisher.Mono;

public class CustomerServiceImpl implements CustomerService {
    @Override
    public Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public Mono<CustomerDTO> getById(String customerId) {
        return null;
    }
}
