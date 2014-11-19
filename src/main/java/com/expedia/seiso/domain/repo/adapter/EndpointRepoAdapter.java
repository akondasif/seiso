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
package com.expedia.seiso.domain.repo.adapter;

import javax.transaction.Transactional;

import lombok.NonNull;
import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.expedia.seiso.domain.entity.Endpoint;
import com.expedia.seiso.domain.entity.Item;
import com.expedia.seiso.domain.entity.key.EndpointKey;
import com.expedia.seiso.domain.entity.key.ItemKey;
import com.expedia.seiso.domain.repo.EndpointRepo;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@Component
@Transactional
public class EndpointRepoAdapter implements RepoAdapter {
	@Autowired
	private EndpointRepo endpointRepo;

	@Override
	public boolean supports(@NonNull Class<?> itemClass) {
		return itemClass == Endpoint.class;
	}

	@Override
	public Item find(@NonNull ItemKey key) {
		val endpointKey = (EndpointKey) key;
		return endpointRepo.findOne(endpointKey.getId());
	}

	@Override
	public void delete(@NonNull ItemKey key) {
		val endpointKey = (EndpointKey) key;
		endpointRepo.delete(endpointKey.getId());
	}
}