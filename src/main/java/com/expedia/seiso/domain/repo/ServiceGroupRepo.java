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
package com.expedia.seiso.domain.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.expedia.seiso.core.ann.FindByKey;
import com.expedia.seiso.domain.entity.ServiceGroup;
import com.expedia.serf.ann.RestResource;

/**
 * @author Willie Wheeler
 */
@RestResource(path = RepoKeys.SERVICE_GROUPS)
public interface ServiceGroupRepo extends CrudRepository<ServiceGroup, Long> {

	@Query("from ServiceGroup order by name")
	List<ServiceGroup> findAll();

	@FindByKey
	ServiceGroup findByKey(@Param("key") String key);
	
	@RestResource(path = "find-by-source")
	List<ServiceGroup> findBySourceKey(@Param("key") String key);
}
