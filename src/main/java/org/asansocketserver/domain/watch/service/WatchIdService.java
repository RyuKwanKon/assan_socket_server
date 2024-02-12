package org.asansocketserver.domain.watch.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Service
public class WatchIdService {

    private final Set<String> disconnectedWatchIds = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void addDisconnectedWatchId(String watchId) {
        disconnectedWatchIds.add(watchId);
    }

    public void removeWatchId(String watchId) {
        disconnectedWatchIds.remove(watchId);
    }

}