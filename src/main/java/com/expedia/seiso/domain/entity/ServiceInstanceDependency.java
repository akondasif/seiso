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
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.expedia.seiso.core.ann.Projection;
import com.expedia.seiso.core.ann.Projection.Cardinality;
import com.expedia.seiso.core.ann.Projections;
import com.expedia.seiso.domain.entity.key.ItemKey;
import com.expedia.seiso.domain.entity.key.SimpleItemKey;
import com.expedia.seiso.web.ApiVersion;

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
	@Projection(cardinality = Cardinality.COLLECTION, paths = {
			"dependency",
			"dependent"
	}),
	@Projection(apiVersions = ApiVersion.V2, cardinality = Cardinality.COLLECTION, name = "by-dependent", paths = {
			"dependency.service"
	}),
	@Projection(apiVersions = ApiVersion.V2, cardinality = Cardinality.COLLECTION, name = "by-dependency", paths = {
			"dependent.service"
	}),
	@Projection(cardinality = Cardinality.SINGLE, paths = {
			"dependency",
			"dependent"
	})
})
// @formatter:on
public class ServiceInstanceDependency extends AbstractItem {
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "dependent_id", nullable = false)
	private ServiceInstance dependent;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "dependency_id", nullable = false)
	private ServiceInstance dependency;
	
	@Override
	public ItemKey itemKey() {
		Long id = getId();
		return (id == null ? null : new SimpleItemKey(ServiceInstanceDependency.class, id));
	}
}
