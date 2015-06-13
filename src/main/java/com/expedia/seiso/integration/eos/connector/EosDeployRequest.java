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
package com.expedia.seiso.integration.eos.connector;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Willie Wheeler
 */
@Data
public class EosDeployRequest {
	
	@JsonProperty("Version")
	private String version;
	
	@JsonProperty("Arguments")
	private String arguments;
	
	@JsonProperty("NodeNameList")
	private String nodeNameList;
	
	@JsonProperty("DeploySameVersion")
	private Boolean deploySameVersion;
	
	@JsonProperty("OverrideStateRestriction")
	private Boolean overrideStateRestriction;
	
	@JsonProperty("SkipRotateIn")
	private Boolean skipRotateIn;
	
	@JsonProperty("SkipRotateOut")
	private Boolean skipRotateOut;
	
	@JsonProperty("SkipDvt")
	private Boolean skipDvt;
	
	@JsonProperty("SkipSetActive")
	private Boolean skipSetActive;
}
