package kz.zhelezyaka.spring6reactivemongoexperiments.repositories;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {

    Mono<Beer> findFirstByBeerName(String beerName);
}
