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
package com.expedia.seiso.domain.repo.impl;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.expedia.seiso.domain.entity.Machine;
import com.expedia.seiso.domain.repo.custom.MachineRepoCustom;

/**
 * @author Ken Van Eyk
 * @author Willie Wheeler
 */
public class MachineRepoImpl implements MachineRepoCustom {
	private static final String ENTITY_NAME = "Machine";
	private static final Set<String> FIELD_NAMES =
			new LinkedHashSet<String>(Arrays.asList("name", "hostname", "ipAddress"));

	@PersistenceContext private EntityManager entityManager;
	@Autowired private RepoImplUtils repoUtils;

	@Override
	public Class<Machine> getResultType() { return Machine.class; }

	@Override
	public Page<Machine> search(@NonNull Set<String> searchTokens, Pageable pageable) {
		return repoUtils.search(ENTITY_NAME, entityManager, FIELD_NAMES, searchTokens, pageable);
	}
}
