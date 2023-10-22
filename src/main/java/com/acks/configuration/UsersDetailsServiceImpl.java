package com.acks.configuration;

import com.acks.dao.UserRepository;
import com.acks.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsersDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.getUsersByUserName(username);

        if (users == null) {
            throw new UsernameNotFoundException("Could not found user !!");
        }

        CustomUsersDetails customUsersDetails = new CustomUsersDetails(users);
        return customUsersDetails;
    }
}
