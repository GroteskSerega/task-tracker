package com.example.task_tracker.mapper.v1;

import com.example.task_tracker.entity.User;
import com.example.task_tracker.web.dto.v1.UserListResponse;
import com.example.task_tracker.web.dto.v1.UserResponse;
import com.example.task_tracker.web.dto.v1.UserUpsertRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User requestToUser(UserUpsertRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(String userId, UserUpsertRequest request);

    UserResponse userToResponse(User user);

    default UserListResponse userListToUserListResponse(List<User> users) {
        return new UserListResponse(users
                .stream()
                .map(this::userToResponse)
                .toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUser(UserUpsertRequest request, @MappingTarget User user);
}
