package com.absmis.security;

import com.absmis.domain.authority.User;
import com.absmis.exception.CustomException;
import com.absmis.repository.authority.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/*最终生成用户和权限共同组成的UserDetails*/
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            throw new CustomException(100,"userName or PassWord not found");
//            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new CustomUserDetails(user,user.getRole().getDescription());
        }
    }

}
