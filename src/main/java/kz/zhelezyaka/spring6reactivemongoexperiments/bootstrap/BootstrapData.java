package kz.zhelezyaka.spring6reactivemongoexperiments.bootstrap;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Beer;
import kz.zhelezyaka.spring6reactivemongoexperiments.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll()
                .doOnSuccess(success -> {
                    loadBeerData();
                }).subscribe();
    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Derbes")
                        .beerStyle("ALE")
                        .upc("125679")
                        .price(new BigDecimal("12.9"))
                        .quantityOnHand(134)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Rudnenskoe")
                        .beerStyle("IPA")
                        .upc("125121")
                        .price(new BigDecimal("11.9"))
                        .quantityOnHand(145)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Yantarnoe")
                        .beerStyle("IPA")
                        .upc("125679234")
                        .price(new BigDecimal("10.23"))
                        .quantityOnHand(111)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }
}
