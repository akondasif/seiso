/* 
 * Copyright 2013-2016 the original author or authors.
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Willie Wheeler
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false, of = "key")
@ToString(of = { "key", "name", "region" })
@Entity
public class DataCenter extends AbstractItem {

	@NotNull
	@Size(min = 1, max = 40)
	@Pattern(regexp = "[a-z0-9-]+")
	@Column(name = "ukey", nullable = false, unique = true)
	private String key;

	@NotNull
	@Size(min = 1, max = 80)
	private String name;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;

	@NonNull
	@OneToMany(mappedBy = "dataCenter")
	@OrderBy("key")
	private List<ServiceInstance> serviceInstances = new ArrayList<>();

	@NonNull
	@OneToMany(mappedBy = "dataCenter")
	private List<LoadBalancer> loadBalancers = new ArrayList<>();
}
