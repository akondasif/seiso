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

import java.beans.PropertyDescriptor;
import java.util.Collection;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.XSlf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.expedia.seiso.domain.entity.Item;
import com.expedia.seiso.domain.repo.adapter.RepoAdapterLookup;

/**
 * Merges client-submitted item data into persistent (or to-be-persistent) items.
 * 
 * @author Willie Wheeler
 */
@Component
@RequiredArgsConstructor
@XSlf4j
public class ItemMerger {
	@NonNull private RepoAdapterLookup repoAdapterLookup;

	/**
	 * Merges a source item into a destination item. Thus this method modifies the destination item.
	 * 
	 * @param src
	 *            source item
	 * @param dest
	 *            destination item
	 * @return merged destination item
	 */
	public void merge(@NonNull Item src, @NonNull Item dest) {
		val itemClass = src.getClass();
		val propDescs = BeanUtils.getPropertyDescriptors(itemClass);
		for (val propDesc : propDescs) {
			val propName = propDesc.getName();
			if (isMergeable(propName)) {
				val propClass = propDesc.getPropertyType();
				if (BeanUtils.isSimpleProperty(propClass)) {
					mergeSimpleProperty(src, dest, propDesc);
				} else if (Item.class.isAssignableFrom(propClass)) {
					mergeSingleAssociation(src, dest, propClass, propName);
				} else if (Collection.class.isAssignableFrom(propClass)) {
					// FIXME I think we need to do this to avoid nulling out many-many associations. But we need to
					// resolve the association data to persistent associations. Currently, including this leads to
					// "detached entity passed to persist" exceptions. [WLW]
					// mergeCollectionProperty(src, dest, propClass, propName);
				} else {
					log.warn("Property '{}' has unrecognized class {}; skipping", propName, propClass.getSimpleName());
				}
			}
		}
	}

	private boolean isMergeable(String propName) {
		// Reject client-provided IDs and audit data. Seiso sets this.
		return !("class".equals(propName) || "id".equals(propName));
	}

	@SneakyThrows
	private void mergeSimpleProperty(Item src, Item dest, PropertyDescriptor propDesc) {
		val getter = propDesc.getReadMethod();
		val setter = propDesc.getWriteMethod();

		if (getter == null || setter == null) {
			log.trace("Skipping simple property: {}", propDesc.getName());
			return;
		}

		val propValue = getter.invoke(src);
		setter.invoke(dest, propValue);
	}

	/**
	 * @param src
	 *            non-persistent data we want to merge into the persistent entity
	 * @param dest
	 *            persistent entity
	 * @param assocClass
	 * @param assocName
	 */
	@SneakyThrows
	@SuppressWarnings("rawtypes")
	private void mergeSingleAssociation(Item src, Item dest, Class assocClass, String assocName) {
		val itemDesc = BeanUtils.getPropertyDescriptor(src.getClass(), assocName);
		log.trace("src.class={}, dest.class={}, itemDesc={}", src.getClass().getName(), dest.getClass().getName(),
				itemDesc);

		val getter = itemDesc.getReadMethod();
		val setter = itemDesc.getWriteMethod();

		if (getter == null || setter == null) {
			log.trace("Skipping single association: {}", itemDesc.getName());
			return;
		}

		val assocData = (Item) getter.invoke(src);
		log.trace("assocData={}", assocData);

		Item persistentAssoc = null;
		if (assocData != null) {
			val assocKey = assocData.itemKey();
			persistentAssoc = repoAdapterLookup.getRepoAdapterFor(assocClass).find(assocKey);
		}

		setter.invoke(dest, persistentAssoc);
	}

	/**
	 * @param src
	 *            non-persistent data we want to merge into the persistent entity
	 * @param dest
	 *            persistent entity
	 * @param assocClass
	 * @param assocName
	 */
	@SneakyThrows
	@SuppressWarnings("rawtypes")
	private void mergeCollection(Item src, Item dest, Class assocClass, String assocName) {
		val itemDesc = BeanUtils.getPropertyDescriptor(src.getClass(), assocName);
		val collection = itemDesc.getReadMethod().invoke(src);
		itemDesc.getWriteMethod().invoke(dest, collection);
	}
}
