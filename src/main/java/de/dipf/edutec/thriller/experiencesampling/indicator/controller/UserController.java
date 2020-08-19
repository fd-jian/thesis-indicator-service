package de.dipf.edutec.thriller.experiencesampling.indicator.controller;

import de.dipf.edutec.thriller.experiencesampling.UserIds;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserController {

    private static final String BASE_URL = "/users";

    private final InteractiveQueryService interactiveQueryService;

    @Transactional
    @GetMapping(value = BASE_URL)
    public UserIdsDto findUsers() {
        ReadOnlyKeyValueStore<Long, UserIds> queryableStore = interactiveQueryService
                .getQueryableStore("indicator-user-ids", QueryableStoreTypes.keyValueStore());

        return Optional.ofNullable(queryableStore.get(0L))
                .map(userIds -> new UserIdsDto(userIds.getIds(), userIds.getTime()))
                .orElse(new UserIdsDto())
        ;
    }

}
