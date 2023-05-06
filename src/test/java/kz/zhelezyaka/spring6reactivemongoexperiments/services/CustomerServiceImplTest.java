package kz.zhelezyaka.spring6reactivemongoexperiments.services;


import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Customer;
import kz.zhelezyaka.spring6reactivemongoexperiments.mappers.CustomerMapper;
import kz.zhelezyaka.spring6reactivemongoexperiments.mappers.CustomerMapperImpl;
import kz.zhelezyaka.spring6reactivemongoexperiments.model.CustomerDTO;
import kz.zhelezyaka.spring6reactivemongoexperiments.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    CustomerRepository customerRepository;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = customerMapper.customerToCustomerDTO(getTestCustomer());
    }

    @Test
    void testFindByCustomerName() {
        CustomerDTO customerDTO = getSavedCustomerDTO();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        customerService.findByCustomerName(customerDTO.getCustomerName())
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void findFirstByCustomerNameTest() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        CustomerDTO customerDTO = getSavedCustomerDTO();
        Mono<CustomerDTO> foundDTO = customerService.findFirstByCustomerName(customerDTO.getCustomerName());
        foundDTO.subscribe(dto -> {
            System.out.println(dto.toString());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    @DisplayName("Test Save Customer Using Subscriber")
    void saveCustomerUseSubscriber() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();

        Mono<CustomerDTO> savedMono = customerService.saveCustomer(Mono.just(customerDTO));

        savedMono.subscribe(savedDTO -> {
            System.out.println(savedDTO.getId());
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Save Customer Using Block")
    void testSaveCustomerUseBlock() {
        CustomerDTO savedDTO = customerService.saveCustomer(Mono.just(getTestCustomerDTO())).block();
        assertThat(savedDTO).isNotNull();
        assertThat(savedDTO.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Customer Using Block")
    void testUpdateBlocking() {
        final String newName = "New Customer Name";
        CustomerDTO savedCustomerDTO = getSavedCustomerDTO();
        savedCustomerDTO.setCustomerName(newName);

        CustomerDTO updatedDTO = customerService.saveCustomer(Mono.just(savedCustomerDTO)).block();

        CustomerDTO fetchedDTO = customerService.getById(updatedDTO.getId()).block();
        assertThat(fetchedDTO.getCustomerName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateStreaming() {
        final String newName = "New Customer Name";

        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();

        customerService.saveCustomer(Mono.just(getTestCustomerDTO()))
                .map(savedCustomerDTO -> {
                    savedCustomerDTO.setCustomerName(newName);
                    return savedCustomerDTO;
                })
                .flatMap(customerService::saveCustomer)
                .flatMap(savedUpdatedDTO -> customerService.getById(savedUpdatedDTO.getId())) // get from db
                .subscribe(dtoFromDb -> {
                    atomicDTO.set(dtoFromDb);
                });

        await().until(() -> atomicDTO.get() != null);
        assertThat(atomicDTO.get().getCustomerName()).isEqualTo(newName);
    }

    @Test
    void testDeleteCustomer() {
        CustomerDTO customerToDelete = getSavedCustomerDTO();

        customerService.deleteCustomerById(customerToDelete.getId()).block();

        Mono<CustomerDTO> expectedEmptyCustomerMono = customerService.getById(customerToDelete.getId());

        CustomerDTO emptyCustomer = expectedEmptyCustomerMono.block();

        assertThat(emptyCustomer).isNull();
    }

    private CustomerDTO getSavedCustomerDTO() {
        return customerService.saveCustomer(Mono.just(getTestCustomerDTO())).block();
    }

    public static CustomerDTO getTestCustomerDTO() {
        return new CustomerMapperImpl().customerToCustomerDTO(getTestCustomer());
    }

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("Vladimir")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }
}