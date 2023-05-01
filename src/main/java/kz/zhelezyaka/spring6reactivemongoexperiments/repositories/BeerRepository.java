package kz.zhelezyaka.spring6reactivemongoexperiments.repositories;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
}
