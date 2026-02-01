package com.example.task_tracker.mapper.v1;

import com.example.task_tracker.entity.User;
import com.example.task_tracker.web.dto.v1.UserListResponse;
import com.example.task_tracker.web.dto.v1.UserResponse;
import com.example.task_tracker.web.dto.v1.UserUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UserUpsertRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(String userId, UserUpsertRequest request);

    UserResponse userToResponse(User user);

    default UserListResponse userListToUserListResponse(List<User> users) {
        UserListResponse response = new UserListResponse();

        response.setUsers(users
                .stream()
                .map(this::userToResponse)
                .collect(Collectors.toList()));

        return response;
    }
}
