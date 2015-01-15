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
package com.expedia.seiso.web.jackson.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import lombok.NonNull;
import lombok.val;

import org.springframework.stereotype.Component;

import com.expedia.seiso.web.Relations;
import com.expedia.seiso.web.hateoas.Link;
import com.expedia.seiso.web.hateoas.PagedResources;
import com.expedia.seiso.web.hateoas.Resource;
import com.expedia.seiso.web.hateoas.Resources;

/**
 * @author Willie Wheeler
 */
@Component
public class V1ResourceAssembler {
	
	public List<V1Resource> toV1Resources(@NonNull Resources resources) {
		val v1ResourceList = new ArrayList<V1Resource>();
		val resourceList = resources.getItems();
		for (val resource : resourceList) { v1ResourceList.add(toV1Resource(resource)); }
		return v1ResourceList;
	}
	
	public List<V1Resource> toV1PagedResources(@NonNull PagedResources pagedResources) {
		val v1ResourceList = new ArrayList<V1Resource>();
		val resourceList = pagedResources.getItems();
		for (val resource : resourceList) { v1ResourceList.add(toV1Resource(resource)); }
		return v1ResourceList;
	}
	
	public V1Resource toV1Resource(@NonNull Resource resource) {
		val srcProps = resource.getProperties();
		val destProps = new TreeMap<String, Object>();
		
		// Links
		val selfLink = findSelfLink(resource.getV1Links());
		if (selfLink != null) {
			destProps.put("_self", selfLink.getHref());
		}
		
		// State
		for (val srcProp : srcProps.entrySet()) {
			val propKey = srcProp.getKey();
			val propVal = srcProp.getValue();
			destProps.put(propKey, propVal);
		}
		
		val v1Resource = new V1Resource();
		v1Resource.setProperties(destProps);
		return v1Resource;
	}
	
	private Link findSelfLink(List<Link> links) {
		if (links == null) { return null; }
		for (val link : links) {
			if (Relations.SELF.equals(link.getRel())) {
				return link;
			}
		}
		return null;
	}
}
