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
package com.expedia.serf.web.controller;

import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.expedia.serf.PathBuilder;
import com.expedia.serf.hmedia.Relations;
import com.expedia.serf.hmedia.Resources;
import com.expedia.serf.web.MediaTypes;

/**
 * @author Willie Wheeler
 */
@RestController
public class EntryController {
	@Autowired private PathBuilder pathBuilder;
	@Autowired private Repositories repositories;
	
	@RequestMapping(
			value = "",
			method = RequestMethod.GET,
			produces = MediaTypes.APPLICATION_HAL_JSON_VALUE)
	public Resources getEntryResource() {
		val resources = new Resources();
		resources.addLink(Relations.SELF, pathBuilder.entryPath());
		return resources;
	}
}
