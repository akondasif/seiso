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
package com.expedia.seiso.web.jackson.hal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.NonNull;
import lombok.val;

import org.springframework.stereotype.Component;

import com.expedia.rf.hmedia.Link;
import com.expedia.rf.hmedia.PagedResources;
import com.expedia.rf.hmedia.Resource;
import com.expedia.rf.hmedia.Resources;

/**
 * Maps base resources to a HAL-specific representation to support HAL serialization.
 * 
 * @author Willie Wheeler
 */
@Component
public class HalResourceAssembler {
	
	public HalResource toHalResources(@NonNull Resources resources) {
		val halResources = new HalResource();
		halResources.setLinks(toHalLinks(resources.getLinks(), true));
		halResources.setEmbedded(toHalEmbeddedItems(resources.getItems()));
		return halResources;
	}
	
	public HalResource toHalPagedResources(@NonNull PagedResources pagedResources) {
		val halPagedResources = new HalResource();
		halPagedResources.setLinks(toHalLinks(pagedResources.getLinks(), true));
		halPagedResources.setEmbedded(toHalEmbeddedItems(pagedResources.getItems()));
		
		// State (page metadata)
		val state = new TreeMap<String, Object>();
		state.put("metadata", pagedResources.getMetadata());
		halPagedResources.setState(state);
		
		return halPagedResources;
	}
	
	public HalResource toHalResource(@NonNull Resource resource, boolean topLevel) {
		val halResource = new HalResource();
		halResource.setLinks(toHalLinks(resource.getLinks(), topLevel));
		
		// State
		val state = new TreeMap<String, Object>();
		val props = resource.getProperties();
		for (val prop : props.entrySet()) {
			val propName = prop.getKey();
			val propValue = prop.getValue();
			
			// Suppress database ID, as we don't want clients knowing/using it. We want them to use URIs.
			if (!"id".equals(propName)) { state.put(propName, propValue); }
		}
		
		// Embedded
		val embedded = new TreeMap<String, Object>();
		val assocs = resource.getAssociations();
		for (val assoc : assocs.entrySet()) {
			val assocName = assoc.getKey();
			val assocValue = assoc.getValue();
			if (assocValue == null) {
				embedded.put(assocName, null);
			} else if (assocValue instanceof Resource) {
				embedded.put(assocName, toHalResource((Resource) assocValue, false));
			} else if (assocValue instanceof List) {
				val halResourceKids = new ArrayList<HalResource>();
				val resourceKids = (List<Resource>) assocValue;
				for (val resourceKid : resourceKids) {
					halResourceKids.add(toHalResource(resourceKid, false));
				}
				embedded.put(assocName, halResourceKids);
			} else {
				// Failing fast here. Considered simply logging a warning, but
				// 1) it would generate too many repetitive warnings in the logs and
				// 2) it would easily go unnoticed by clients
				throw new IllegalArgumentException("Unsupported association type: " + assocValue.getClass());
			}
		}

		halResource.setEmbedded(embedded.isEmpty() ? null : embedded);
		halResource.setState(state);

		return halResource;
	}
	
	private Map<String, Object> toHalLinks(List<Link> links, boolean includeCuries) {
		
		// Don't show _links unless there are actually links.
		if (links == null || links.isEmpty()) { return null; }
		
		val halLinks = new LinkedHashMap<String, Object>();
		
		// FIXME If the link relation can have multiple links, then we need to generate an array here. Note that this
		// depends on the nature of the relation itself, not the actual number of links in any given case. So for
		// example if a manager has a single direct report, we would still generate an array of direct report links
		// since in principle the manager could have many.
		for (val link : links) {
			val halLink = new LinkedHashMap<String, Object>();
			halLink.put("href", link.getHref());
			
			val title = link.getTitle();
			if (title != null) { halLink.put("title", title); }
			
			val templated = link.getTemplated();
			if (templated != null) { halLink.put("templated", templated); }
			
			halLinks.put(link.getRel(), halLink);
		}
		
		if (includeCuries) { halLinks.put("curies", Curie.SEISO_CURIES); }
		
		return halLinks;
	}
	
	private Map<String, Object> toHalEmbeddedItems(List<Resource> resourceList) {
		val embedded = new TreeMap<String, Object>();
		val halResourceList = new ArrayList<HalResource>();
		for (val resource : resourceList) {
			halResourceList.add(toHalResource(resource, false));
		}
		embedded.put("items", halResourceList);
		return embedded;
	}
}
