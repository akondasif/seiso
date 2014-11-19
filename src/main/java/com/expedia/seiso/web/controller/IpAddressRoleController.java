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
package com.expedia.seiso.web.controller;

import lombok.extern.slf4j.XSlf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.expedia.seiso.domain.entity.key.IpAddressRoleKey;
import com.expedia.seiso.web.dto.MapItemDto;
import com.expedia.seiso.web.dto.PEItemDto;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@RestController
@RequestMapping(Controllers.REQUEST_MAPPING_VERSION)
@XSlf4j
public class IpAddressRoleController extends AbstractItemController {
	private static final String SINGLE_URI_TEMPLATE = "/service-instances/{serviceInstanceKey}/ip-address-roles/{name}";

	@RequestMapping(value = SINGLE_URI_TEMPLATE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MapItemDto get(@PathVariable String serviceInstanceKey, @PathVariable String name) {
		return super.get(new IpAddressRoleKey(serviceInstanceKey, name));
	}

	@RequestMapping(value = SINGLE_URI_TEMPLATE, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void put(@PathVariable String serviceInstanceKey, @PathVariable String name, PEItemDto ipAddressRoleDto) {

		log.trace("Putting IP address role: serviceInstanceKey={}, name={}", serviceInstanceKey, name);
		super.put(ipAddressRoleDto.getItem());
	}

	@RequestMapping(value = SINGLE_URI_TEMPLATE, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String serviceInstanceKey, @PathVariable String name) {
		super.delete(new IpAddressRoleKey(serviceInstanceKey, name));
	}
}
