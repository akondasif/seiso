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
import com.expedia.seiso.core.ann.RestResource;
import com.expedia.seiso.domain.entity.InfrastructureProvider;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@RestResource(path = RepoKeys.INFRASTRUCTURE_PROVIDERS)
public interface InfrastructureProviderRepo extends CrudRepository<InfrastructureProvider, Long> {

	@Query("from InfrastructureProvider order by name")
	List<InfrastructureProvider> findAll();

	@FindByKey
	InfrastructureProvider findByKey(@Param("key") String key);
}
