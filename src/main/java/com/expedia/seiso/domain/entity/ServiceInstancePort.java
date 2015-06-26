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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.XSlf4j;

import com.expedia.seiso.core.ann.Projection;
import com.expedia.seiso.core.ann.Projection.Cardinality;
import com.expedia.seiso.core.ann.Projections;
import com.expedia.seiso.domain.entity.key.ItemKey;
import com.expedia.seiso.domain.entity.key.ServiceInstancePortKey;
import com.expedia.seiso.domain.repo.RepoKeys;
import com.expedia.serf.ann.RestResource;

/**
 * @author Willie Wheeler
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false, of = { "serviceInstance", "number" })
@ToString(of = { "serviceInstance", "number", "protocol", "description" })
@Entity
//@formatter:off
@Projections({

	// FIXME Blech, we shouldn't have to pull in the entire service instance here. [WLW]
	// For now leave the default COLLECTION and SINGLE queries in place because one of the unit tests expects them.
	// But I don't think we want these if the repo isn't exported. [WLW]
	@Projection(cardinality = Cardinality.COLLECTION, paths = "serviceInstance"),
	
	// TODO Assembles the ports without their respective service instances. Are we using this? [WLW]
	@Projection(cardinality = Cardinality.COLLECTION, name = "serviceInstancePorts"),
	
	@Projection(cardinality = Cardinality.SINGLE, paths = { "serviceInstance", "endpoints" }),
	
	// TODO Assembles a port without its service instance. Are we using this? [WLW]
	@Projection(cardinality = Cardinality.SINGLE, name = "serviceInstancePort")
	})
//@formatter:on
@XSlf4j
public class ServiceInstancePort extends AbstractItem {
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "service_instance_id")
	@RestResource(path = "service-instance")
	private ServiceInstance serviceInstance;
	
	@NotNull
	@Min(0)
	@Max(65535)
	private Integer number;
	
	@Size(max = 40)
	private String protocol;
	
	@Size(max = 250) 
	private String description;

	// FIXME For some reason, this is not cascade deleting endpoints. I get a constraint violation when calling it from
	// ServiceInstancePortControllerV1.deletePort(). [WLW]
	@NonNull
	@OneToMany(mappedBy = "port", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Endpoint> endpoints = new ArrayList<>();

	@Override
	public ItemKey itemKey() {
		// FIXME This NPEs when there's no service instance loaded. So at least make it more explicit with ISE.
		// Need to get away from itemKeys and use URIs instead.
		if (serviceInstance == null) {
			throw new IllegalStateException("Need serviceInstance to generate itemKey");
		}
		return new ServiceInstancePortKey(serviceInstance.getKey(), number);
	}

	// TODO Adopt this pattern for bidirectional associations throughout. [WLW]
	public ServiceInstancePort setServiceInstance(ServiceInstance serviceInstance) {
		log.trace("Setting service instance");
		this.serviceInstance = serviceInstance;
		if (serviceInstance != null) {
			val ports = serviceInstance.getPorts();
			if (!ports.contains(this)) {
				ports.add(this);
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.expedia.seiso.domain.entity.Item#itemPath()
	 */
	@Override
	public String[] itemPath() {
		return new String[] {
				RepoKeys.SERVICE_INSTANCES,
				serviceInstance.getKey(),
				RepoKeys.SERVICE_INSTANCE_PORTS,
				String.valueOf(number)
		};
	}
}
