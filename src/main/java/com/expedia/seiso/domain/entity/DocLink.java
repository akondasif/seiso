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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;
import lombok.experimental.Accessors;

import com.expedia.seiso.core.ann.Parent;
import com.expedia.seiso.core.ann.Projection;
import com.expedia.seiso.core.ann.Projection.Cardinality;
import com.expedia.seiso.core.ann.Projections;
import com.expedia.seiso.domain.entity.key.ItemKey;
import com.expedia.seiso.domain.entity.key.SimpleItemKey;

/**
 * @author Willie Wheeler
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
// @formatter:off
@Projections({
	@Projection(cardinality = Cardinality.COLLECTION),
	@Projection(cardinality = Cardinality.SINGLE)
})
// @formatter:on
public class DocLink extends AbstractItem {
	
	@Parent
	@ManyToOne
	@JoinColumn(name = "service_id", nullable = false)
	private Service service;
	
	private String title;
	private String href;
	private String description;
	
	@Override
	public ItemKey itemKey() {
		val id = getId();
		return (id == null ? null : new SimpleItemKey(DocLink.class, id));
	}
}
