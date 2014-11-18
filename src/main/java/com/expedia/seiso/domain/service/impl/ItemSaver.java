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

import java.util.Date;

import javax.transaction.Transactional;

import lombok.NonNull;
import lombok.val;
import lombok.extern.slf4j.XSlf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;

import com.expedia.seiso.core.util.ReflectionUtils;
import com.expedia.seiso.core.util.SecurityUtils;
import com.expedia.seiso.domain.entity.Item;
import com.expedia.seiso.domain.entity.key.ItemKey;
import com.expedia.seiso.domain.repo.adapter.RepoAdapters;
import com.expedia.seiso.gateway.NotificationGateway;
import com.expedia.seiso.gateway.model.ConfigManagementEvent;

@Component
@Transactional
@XSlf4j
public class ItemSaver {
	@Autowired private Repositories repositories;
	@Autowired private RepoAdapters repoAdapters;
	@Autowired private ItemMerger itemMerger;
	
	// FIXME Use the NotificationAspect instead of this.
	@Autowired private NotificationGateway notificationGateway;
	
	public void save(@NonNull Item itemData) {
		val itemClass = itemData.getClass();
		val currUser = SecurityUtils.getCurrentUser();
		val now = new Date();
		
		Item itemToSave = doFind(itemData.itemKey());
		val newItem = (itemToSave == null);
		if (newItem) {
			itemToSave = ReflectionUtils.createInstance(itemClass);
			itemToSave.setCreatedBy(currUser);
			itemToSave.setCreatedDate(now);
		}
		
		itemMerger.merge(itemData, itemToSave);
		itemToSave.setUpdatedBy(currUser);
		itemToSave.setUpdatedDate(now);
		getRepositoryFor(itemClass).save(itemToSave);
		
		// FIXME Move to NotificationAspect.
		// FIXME This can probably generate a stack overflow, because there are bidirectional associations, and the
		// serialization will just follow the cycle. We need to send the notification once we have the MapDto. So
		// again push this up. [WLW]
		log.info("Sending notification");
		val op = (newItem ? ConfigManagementEvent.OP_CREATE : ConfigManagementEvent.OP_UPDATE);
		notificationGateway.notify(itemToSave, op);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private CrudRepository getRepositoryFor(Class<?> itemClass) {
		return (CrudRepository<?, Long>) repositories.getRepositoryFor(itemClass);
	}
	
	private Item doFind(ItemKey key) {
		return repoAdapters.getRepoAdapterFor(key.getItemClass()).find(key);
	}
}
