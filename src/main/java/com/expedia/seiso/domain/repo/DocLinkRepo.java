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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.expedia.seiso.core.ann.RestResource;
import com.expedia.seiso.domain.entity.DocLink;

/**
 * @author Willie Wheeler
 */
@RestResource(path = RepoKeys.DOC_LINKS)
public interface DocLinkRepo extends PagingAndSortingRepository<DocLink, Long> {
	
	@RestResource(path = "find-by-service")
	@Query("from DocLink docLink where docLink.service.key = :key")
	Page<DocLink> findByService(@Param("key") String key, Pageable pageable);
	
	@RestResource(path = "find-by-source")
	Page<DocLink> findBySourceKey(@Param("key") String key, Pageable pageable);
}
