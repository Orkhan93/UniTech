package az.spring.mapper;

import az.spring.entity.User;
import az.spring.request.UserRegistration;
import az.spring.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToModel(UserRegistration registration);

    UserResponse modelToResponse(User user);


}