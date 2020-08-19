package de.dipf.edutec.thriller.experiencesampling.indicator.controller;

import de.dipf.edutec.thriller.experiencesampling.UserIds;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserController {

    private static final String BASE_URL = "/users";

    private final InteractiveQueryService interactiveQueryService;

    @Transactional
    @GetMapping(value = BASE_URL)
    public UserIdsDto findUsers() {
        ReadOnlyKeyValueStore<Long, UserIds> queryableStore = interactiveQueryService.getQueryableStore("indicator-user-ids", QueryableStoreTypes.keyValueStore());
        UserIds userIds = queryableStore.get(0L);
        return new UserIdsDto(userIds.getIds(), userIds.getTime());
//        List<UserIdsDto> userIds = new ArrayList<>();
//        while (all.hasNext()) {
//            KeyValue<Long, UserIds> next = all.next();
//            userIds.add(new UserIdsDto(next.value.getIds(), next.key));
//        }
//       return userIds.size();
//        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(all.next().key), ZoneId.systemDefault()).toString();
    }

}
