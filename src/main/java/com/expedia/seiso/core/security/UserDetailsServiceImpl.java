/* 
 * Copyright 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expedia.seiso.core.security;

import lombok.NonNull;
import lombok.val;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expedia.seiso.domain.repo.UserRepo;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
		val user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("No such user: " + username);
		}

		// Initialize these, as the Spring Security filter seems to be outside the OpenEntityManagerInView filter.
		Hibernate.initialize(user.getRoles());

		return new UserDetailsAdapter(user);
	}
}
