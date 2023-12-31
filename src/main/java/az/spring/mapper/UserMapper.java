package az.spring.mapper;

import az.spring.entity.Account;
import az.spring.entity.User;
import az.spring.request.UserRegistration;
import az.spring.response.AccountResponse;
import az.spring.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToModel(UserRegistration registration);

    UserResponse modelToResponse(User user);

}