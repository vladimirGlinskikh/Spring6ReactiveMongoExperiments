package kz.zhelezyaka.spring6reactivemongoexperiments.web.fn;

import kz.zhelezyaka.spring6reactivemongoexperiments.model.BeerDTO;
import kz.zhelezyaka.spring6reactivemongoexperiments.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    public Mono<ServerResponse> listBeers(ServerRequest request) {
        return ServerResponse.ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }
}
