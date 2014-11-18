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
package com.expedia.seiso.domain.entity;

import java.util.Date;

import org.springframework.hateoas.Identifiable;

import com.expedia.seiso.domain.entity.key.ItemKey;

/**
 * <p>
 * A configuration item (CI) entity. CIs are the primary domain object in the configuration management (CM) domain.
 * </p>
 * <p>
 * Implements {@link Identifiable} so we can use Spring HATEOAS' link building capability.
 * </p>
 * 
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
public interface Item extends Identifiable<Long> {
	
	ItemKey itemKey();
	
	Item setId(Long id);
	
	User getCreatedBy();
	
	Item setCreatedBy(User createdBy);
	
	Date getCreatedDate();
	
	Item setCreatedDate(Date createdDate);
	
	User getUpdatedBy();
	
	Item setUpdatedBy(User updatedBy);
	
	Date getUpdatedDate();
	
	Item setUpdatedDate(Date updatedDate);
}
