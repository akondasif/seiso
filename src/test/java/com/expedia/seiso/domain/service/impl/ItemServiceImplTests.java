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
package com.expedia.seiso.domain.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import lombok.extern.slf4j.XSlf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.ReflectionUtils;

import com.expedia.seiso.domain.entity.Item;
import com.expedia.seiso.domain.entity.Person;
import com.expedia.seiso.domain.entity.key.ItemKey;
import com.expedia.seiso.domain.entity.key.SimpleItemKey;
import com.expedia.seiso.domain.meta.ItemMeta;
import com.expedia.seiso.domain.meta.ItemMetaLookup;
import com.expedia.seiso.domain.repo.PersonRepo;
import com.expedia.seiso.domain.repo.adapter.RepoAdapters;
import com.expedia.seiso.domain.repo.adapter.SimpleItemRepoAdapter;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@XSlf4j
public class ItemServiceImplTests {
	
	// Class under test
	@InjectMocks private ItemServiceImpl itemService;
	
	// Dependencies
	@Mock private ItemMetaLookup itemMetaLookup;
	@Mock private Repositories repositories;
	@Mock private PersonRepo personRepo;
	@Mock private RepoAdapters repoAdapters;
	@Mock private SimpleItemRepoAdapter simpleItemRepoAdapter;
	@Mock private ItemDeleter itemDeleter;
	@Mock private ItemMerger itemMerger;
	
	// Test data
	@Mock private ItemMeta personMeta;
	@Mock private Pageable personPageable;
	private Method personFindByKeyMethod;
	private List<Person> people;
	private Person person;
	private SimpleItemKey personKey;
	@Mock private Page<Person> personPage;
	
	@Before
	public void init() throws Exception {
		this.itemService = new ItemServiceImpl();
		MockitoAnnotations.initMocks(this);
		initTestData();
		initDependencies();
	}
	
	private void initTestData() {
		// @formatter:off
		this.person = new Person()
				.setUsername("wwheeler")
				.setFirstName("Willie")
				.setLastName("Wheeler");
		// @formatter:on
		
		this.people = new ArrayList<Person>();
		people.add(person);
		
		this.personFindByKeyMethod = ReflectionUtils.findMethod(PersonRepo.class, "findByUsername", String.class);
		log.trace("method={}", personFindByKeyMethod);
		
		when(personMeta.getRepositoryFindByKeyMethod()).thenReturn(personFindByKeyMethod);
		
		this.personKey = new SimpleItemKey(Person.class, person.getUsername());
	}
	
	private void initDependencies() {
		when(itemMetaLookup.getItemMeta(Person.class)).thenReturn(personMeta);
		
		when(repositories.getRepositoryFor(Person.class)).thenReturn(personRepo);
		
		when(personRepo.findAll(personPageable)).thenReturn(personPage);
		
		when(repoAdapters.getRepoAdapterFor(Person.class)).thenReturn(simpleItemRepoAdapter);
		when(simpleItemRepoAdapter.find(personKey)).thenReturn(person);
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void findAll() {
		val result = itemService.findAll(Person.class, personPageable);
		assertNotNull(result);
		verify(personRepo).findAll(personPageable);
	}
	
	@Test(expected = NullPointerException.class)
	public void findAll_nullItemClass() {
		itemService.findAll(null, personPageable);
	}
	
	@Test(expected = NullPointerException.class)
	public void findAll_nullPageable() {
		itemService.findAll(Person.class, null);
	}
	
	@Test
	public void find() {
		val result = itemService.find(new SimpleItemKey(Person.class, person.getUsername()));
		assertNotNull(result);
		assertEquals(person, result);
	}
	
	@Test(expected = NullPointerException.class)
	public void find_nullItemKey() {
		itemService.find(null);
	}
	
	@Test
	public void delete() {
		itemService.delete(person);
		verify(itemDeleter, times(1)).delete(person);
	}
	
	@Test
	public void delete_item() {
		itemService.delete(person);
		verify(itemDeleter).delete(person);
	}
	
	@Test(expected = NullPointerException.class)
	public void delete_nullItem() {
		itemService.delete((Item) null);
	}
	
	@Test
	public void delete_itemKey() {
		itemService.delete(personKey);
		verify(itemDeleter).delete(person);
	}
	
	@Test(expected = NullPointerException.class)
	public void delete_nullItemKey() {
		itemService.delete((ItemKey) null);
	}
}
