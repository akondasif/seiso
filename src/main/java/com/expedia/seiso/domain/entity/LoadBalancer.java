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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.expedia.seiso.core.ann.Key;
import com.expedia.seiso.core.ann.Projection;
import com.expedia.seiso.core.ann.Projection.Cardinality;
import com.expedia.seiso.core.ann.Projections;
import com.expedia.seiso.domain.entity.key.ItemKey;
import com.expedia.seiso.domain.entity.key.SimpleItemKey;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false, of = { "name" })
@ToString(of = { "name", "type" })
@Entity
@Projections({
	@Projection(cardinality = Cardinality.COLLECTION, paths = {
			"dataCenter.region.infrastructureProvider"
	}),
	@Projection(cardinality = Cardinality.SINGLE, paths = {
			"dataCenter.region.infrastructureProvider",
			"vips"
	}),
	@Projection(cardinality = Cardinality.SINGLE, name = "service-instances", paths = {
			"serviceInstances.service",
			"serviceInstances.environment"
	})
})
public class LoadBalancer extends AbstractItem {
	
	@Key
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "data_center_id")
	private DataCenter dataCenter;
	
	// FIXME This is probably just temporary. Eventually we'll tie the service instance to a load balancer through a
	// VIP. [WLW]
	@NonNull
	@OneToMany(mappedBy = "loadBalancer")
	@OrderBy("key")
	private List<ServiceInstance> serviceInstances = new ArrayList<>();
	
	@NonNull
	@OneToMany(mappedBy = "loadBalancer")
	private List<Vip> vips = new ArrayList<>();
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "api_url")
	private String apiUrl;
	
	@Override
	public ItemKey itemKey() { return new SimpleItemKey(LoadBalancer.class, name); }
}
