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
package com.expedia.seiso.domain.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.expedia.seiso.core.ann.FindByKey;
import com.expedia.seiso.domain.entity.Person;
import com.expedia.seiso.domain.repo.custom.PersonRepoCustom;
import com.expedia.serf.ann.RestResource;

/**
 * @author Willie Wheeler
 */
@RestResource(rel = RepoKeys.PEOPLE, path = RepoKeys.PEOPLE)
public interface PersonRepo extends PagingAndSortingRepository<Person, Long>, PersonRepoCustom {

	Person findByLdapDn(@Param("dn") String ldapDn);

	@FindByKey
	Person findByUsername(@Param("username") String username);
	
	@RestResource(path = "find-by-first-name")
	Page<Person> findByFirstName(@Param("name") String firstName, Pageable pageable);

	@RestResource(path = "find-by-last-name")
	Page<Person> findByLastName(@Param("name") String lastName, Pageable pageable);
	
	@RestResource(path = "find-by-email")
	Person findByEmail(@Param("email") String email);
	
	// Don't get too attached to this as it's just for fun, and probably won't last.
	List<Person> findByMbType(String mbType);
	
	List<Person> findByMbTypeIn(String[] mbTypes);
	
//	@RestResource(path = "find-by-source")
//	Page<Person> findBySourceKey(@Param("key") String key, Pageable pageable);
}
