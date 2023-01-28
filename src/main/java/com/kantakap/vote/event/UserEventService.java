package com.kantakap.vote.event;

import com.kantakap.vote.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class UserEventService {
    private final Sinks.Many<User> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer();
    private final Flux<User> flux = sink.asFlux().cache(0);

    public void emitEvent(User user) {
        sink.tryEmitNext(user);
    }

    public Flux<User> subscribe() {
        return flux;
    }
}
