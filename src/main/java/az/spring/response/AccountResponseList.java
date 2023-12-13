package az.spring.response;

import az.spring.wrapper.AccountWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AccountResponseList {

    List<AccountWrapper> accountResponseList;

}