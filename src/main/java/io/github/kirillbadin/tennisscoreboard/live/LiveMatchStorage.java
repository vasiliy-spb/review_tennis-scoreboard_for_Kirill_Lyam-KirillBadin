package io.github.kirillbadin.tennisscoreboard.live;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LiveMatchStorage {

    // Класс не относится к доменным моделям, поэтому лучше перенести его в более подходящий пакет.

    private final Map<UUID, LiveMatch> storage = new ConcurrentHashMap<>();

    // Хранилище может само создавать ID для матча (по аналогии с БД) и возвращать его из этого метода.
    public void save(LiveMatch liveMatch) {
        storage.put(liveMatch.getUuid(), liveMatch);
    }

    public Optional<LiveMatch> getMatch(UUID uuid){
        return Optional.ofNullable(storage.get(uuid));
    }

    public void remove(UUID uuid) {
        storage.remove(uuid);
    }
}
