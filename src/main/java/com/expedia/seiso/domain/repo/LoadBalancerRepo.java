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
package com.expedia.seiso.domain.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.expedia.seiso.domain.entity.LoadBalancer;
import com.expedia.seiso.domain.repo.custom.LoadBalancerRepoCustom;

/**
 * @author Willie Wheeler
 */
public interface LoadBalancerRepo extends PagingAndSortingRepository<LoadBalancer, Long>, LoadBalancerRepoCustom {
	
	LoadBalancer findByName(@Param("name") String name);
	
	@Query("from LoadBalancer lb where lb.dataCenter.key = :key")
	Page<LoadBalancer> findByDataCenterKey(@Param("key") String key, Pageable pageable);
}
