package emadeldeen.paymentsmanagement.business;

import emadeldeen.paymentsmanagement.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        User user = userRepository.getByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return new UserDetailsImp(user);
    }
}
