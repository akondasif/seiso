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
package com.expedia.seiso.web.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import lombok.val;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.expedia.seiso.domain.entity.Node;
import com.expedia.seiso.domain.entity.key.SimpleItemKey;
import com.expedia.seiso.domain.service.ItemService;

public class ItemConverterFactoryTests {
	private static final String NODE_KEY = "my-node";

	// Class under test
	@InjectMocks
	private ItemConverterFactory factory;

	// Dependencies
	@Mock
	private ItemService itemService;

	// Test data
	private Node node;

	@Before
	public void setUp() throws Exception {
		this.factory = new ItemConverterFactory();
		MockitoAnnotations.initMocks(this);
		initTestData();
		initDependencies();
	}

	private void initTestData() {
		this.node = new Node();
	}

	private void initDependencies() {
		when(itemService.find(new SimpleItemKey(Node.class, NODE_KEY))).thenReturn(node);
	}

	@Test
	public void getConverter() {
		val converterResult = factory.getConverter(Node.class);
		assertNotNull(converterResult);
		val nodeResult = converterResult.convert(NODE_KEY);
		assertEquals(node, nodeResult);
	}
}
