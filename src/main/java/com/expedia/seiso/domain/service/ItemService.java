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
package com.expedia.seiso.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.expedia.seiso.core.exception.ResourceNotFoundException;
import com.expedia.seiso.domain.entity.Item;
import com.expedia.seiso.domain.entity.key.ItemKey;

/**
 * Essentially a generic item CRUD repository that puts a fixed API in front of repositories that can have differing
 * APIs, especially where finding by key is involved.
 * 
 * @author Willie Wheeler
 */
public interface ItemService {

	/**
	 * <p>
	 * Saves a list of items.
	 * </p>
	 * 
	 * @param items
	 *            items to save
	 */
	SaveAllResponse saveAll(List<? extends Item> items);

	/**
	 * <p>
	 * Saves the given item to the database, either creating or updating as necessary. In the case of updates, this
	 * method merges the given item into the existing item before saving.
	 * </p>
	 * 
	 * @param item
	 *            item to save
	 */
	void save(Item item);

	@SuppressWarnings("rawtypes")
	List findAll(Class itemClass);

	@SuppressWarnings("rawtypes")
	Page findAll(Class itemClass, Pageable pageable);
	
	/**
	 * Finds an item based on an arbitrary key.
	 * 
	 * @param key
	 *            item key
	 * @return persistent item
	 * @throws ResourceNotFoundException
	 *             if the specified item doesn't exist
	 */
	Item find(ItemKey key);
	
	void delete(Item item);

	void delete(ItemKey key);
}
