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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.expedia.seiso.core.ann.FindByKey;
import com.expedia.seiso.core.ann.RestResource;
import com.expedia.seiso.domain.entity.Machine;
import com.expedia.seiso.domain.repo.custom.MachineRepoCustom;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@Transactional(readOnly = true)
@RestResource(path = RepoKeys.MACHINES)
public interface MachineRepo extends PagingAndSortingRepository<Machine, Long>, MachineRepoCustom {

	@Query("from Machine m order by m.name")
	@Override
	Iterable<Machine> findAll();

	@FindByKey
	Machine findByName(@Param("name") String name);

	// FIXME This won't work til ItemSearchController can handle single return values. [WLW]
	// @RestResource(path = "find-by-fqdn")
	Machine findByFqdn(@Param("fqdn") String fqdn);

	// FIXME This won't work til ItemSearchController can handle single return values. [WLW]
	// @RestResource(path = "find-by-ip-address")
	Machine findByIpAddress(@Param("ip") String ipAddress);

}
