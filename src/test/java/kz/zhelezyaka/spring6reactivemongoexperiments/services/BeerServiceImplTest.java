package kz.zhelezyaka.spring6reactivemongoexperiments.services;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Beer;
import kz.zhelezyaka.spring6reactivemongoexperiments.mappers.BeerMapper;
import kz.zhelezyaka.spring6reactivemongoexperiments.mappers.BeerMapperImpl;
import kz.zhelezyaka.spring6reactivemongoexperiments.model.BeerDTO;
import kz.zhelezyaka.spring6reactivemongoexperiments.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    BeerRepository beerRepository;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDTO(getTestBeer());
    }

    @Test
    void testFindByBeerStyle() {
        BeerDTO beerDTO = getSavedBeerDTO();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        beerService.findByBeerStyle(beerDTO.getBeerStyle())
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void findFirstByBeerNameTest() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDTO beerDTO = getSavedBeerDTO();
        Mono<BeerDTO> foundDTO = beerService.findFirstByBeerName(beerDTO.getBeerName());
        foundDTO.subscribe(dto -> {
            System.out.println(dto.toString());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    @DisplayName("Test Save Beer Using Subscriber")
    void saveBeerUseSubscriber() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDTO = new AtomicReference<>();

        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(savedDTO -> {
            System.out.println(savedDTO.getId());
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        BeerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Save Beer Using Block")
    void testSaveBeerUseBlock() {
        BeerDTO savedDTO = beerService.saveBeer(Mono.just(getTestBeerDTO())).block();
        assertThat(savedDTO).isNotNull();
        assertThat(savedDTO.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Beer Using Block")
    void testUpdateBlocking() {
        final String newName = "New Beer Name";  // use final so cannot mutate
        BeerDTO savedBeerDTO = getSavedBeerDTO();
        savedBeerDTO.setBeerName(newName);

        BeerDTO updatedDTO = beerService.saveBeer(Mono.just(savedBeerDTO)).block();

        BeerDTO fetchedDTO = beerService.getById(updatedDTO.getId()).block();
        assertThat(fetchedDTO.getBeerName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateStreaming() {
        final String newName = "New Beer Name";

        AtomicReference<BeerDTO> atomicDTO = new AtomicReference<>();

        beerService.saveBeer(Mono.just(getTestBeerDTO()))
                .map(savedBeerDTO -> {
                    savedBeerDTO.setBeerName(newName);
                    return savedBeerDTO;
                })
                .flatMap(beerService::saveBeer)
                .flatMap(savedUpdatedDTO -> beerService.getById(savedUpdatedDTO.getId())) // get from db
                .subscribe(dtoFromDb -> {
                    atomicDTO.set(dtoFromDb);
                });

        await().until(() -> atomicDTO.get() != null);
        assertThat(atomicDTO.get().getBeerName()).isEqualTo(newName);
    }

    @Test
    void testDeleteBeer() {
        BeerDTO beerToDelete = getSavedBeerDTO();

        beerService.deleteBeerById(beerToDelete.getId()).block();

        Mono<BeerDTO> expectedEmptyBeerMono = beerService.getById(beerToDelete.getId());

        BeerDTO emptyBeer = expectedEmptyBeerMono.block();

        assertThat(emptyBeer).isNull();

    }

    public BeerDTO getSavedBeerDTO() {
        return beerService.saveBeer(Mono.just(getTestBeerDTO())).block();
    }

    public static BeerDTO getTestBeerDTO() {
        return new BeerMapperImpl().beerToBeerDTO(getTestBeer());
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Derbes")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(15)
                .upc("1282389")
                .build();
    }
}