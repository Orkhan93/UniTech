//package az.spring.security;
//
//import az.spring.entity.User;
//import az.spring.repository.UserRepository;
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Slf4j
//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Getter
//    private User userDetail;
//
//    public UserDetailServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("Inside loadUserByUsername {}", username);
//        User user = userRepository.findFirstByEmail(username);
//        userDetail = userRepository.findFirstByEmail(username);
//        if (user == null) throw new RuntimeException("User not found...");
//        return new org.springframework.security.core.userdetails.User(user.getEmail(),
//                new BCryptPasswordEncoder().encode(user.getPassword()), new ArrayList<>());
//    }
//
//}