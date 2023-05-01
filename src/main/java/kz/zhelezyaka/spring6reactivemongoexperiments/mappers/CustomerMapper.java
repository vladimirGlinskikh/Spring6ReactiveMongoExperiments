package kz.zhelezyaka.spring6reactivemongoexperiments.mappers;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Customer;
import kz.zhelezyaka.spring6reactivemongoexperiments.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}