package com.fdmgroup.apmproject.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.UserRepository;

@Service
public class UserSecurityDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(username + " not found");
		} else {
			return new UserSecurityDetails(user.get());
		}
	}
}
