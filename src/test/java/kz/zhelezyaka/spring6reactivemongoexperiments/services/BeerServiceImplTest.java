package kz.zhelezyaka.spring6reactivemongoexperiments.services;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Beer;
import kz.zhelezyaka.spring6reactivemongoexperiments.mappers.BeerMapper;
import kz.zhelezyaka.spring6reactivemongoexperiments.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDTO(getTestBeer());
    }

    @Test
    void saveBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(savedDTO -> {
            System.out.println(savedDTO.getId());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
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