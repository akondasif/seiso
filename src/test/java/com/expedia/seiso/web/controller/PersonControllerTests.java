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
package com.expedia.seiso.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.expedia.seiso.domain.entity.Person;
import com.expedia.seiso.domain.repo.PersonRepo;
import com.expedia.seiso.web.assembler.ResponseHeadersBuilder;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 *
 */
public class PersonControllerTests {
	
	// Class under test
	@InjectMocks private PersonController controller;
	
	// Dependencies
	@Mock private PersonRepo personRepo;
	@Mock private ResponseHeadersBuilder responseHeadersBuilder;
	
	// Test objects
	private List<Person> people;
	private Pageable pageable;
	@Mock private Page<Person> personPage;
	
	@Before
	public void setUp() throws Exception {
		this.controller = new PersonController();
		MockitoAnnotations.initMocks(this);
		initTestObjects();
		initDependencies();
	}
	
	private void initTestObjects() {
		this.people = new ArrayList<>();
		people.add(new Person().setUsername("alpha"));
		people.add(new Person().setUsername("beta"));
		people.add(new Person().setUsername("gamma"));
		people.add(new Person().setUsername("delta"));
		
		this.pageable = new PageRequest(3, 100, Direction.ASC, "username");
		
		when(personPage.iterator()).thenReturn(people.iterator());
	}
	
	private void initDependencies() {
		when(personRepo.findBySource(anyString(), eq(pageable))).thenReturn(personPage);
	}
	
	@Test
	public void testGetUsernames() {
		val response = controller.getUsernames("ad", pageable);
		assertNotNull(response);
		
		val usernames = response.getBody();
		
		assertEquals(people.size(), usernames.size());
		for (int i = 0; i < usernames.size(); i++) {
			assertEquals(people.get(i).getUsername(), usernames.get(i));
		}
	}
}
