package kz.zhelezyaka.spring6reactivemongoexperiments.mappers;

import kz.zhelezyaka.spring6reactivemongoexperiments.domain.Beer;
import kz.zhelezyaka.spring6reactivemongoexperiments.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDTOToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDTO(Beer beer);
}