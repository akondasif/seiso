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
package com.expedia.seiso.core.util;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
public class C {
	public static final String MEDIA_TYPE_TEXT_URI_LIST = "text/uri-list";

	public static final int DEFAULT_PAGE_NUMBER = 0;
	public static final int DEFAULT_PAGE_SIZE = 100;

	// Error codes
	public static final String EC_INTERNAL_ERROR = "internal_error";

	/** Syntax fine, but semantics wrong */
	public static final String EC_INVALID_REQUEST = "invalid_request";

	/** JSON syntax is fine, but semantics wrong (e.g. array passed where we expect a single value) */
	public static final String EC_INVALID_REQUEST_JSON_PAYLOAD = "invalid_request_json_payload";

	public static final String EC_RESOURCE_NOT_FOUND = "resource_not_found";

	public static final String AMQP_EXCHANGE_SEISO_NOTIFICATIONS = "seiso.notifications";
	public static final String AMQP_EXCHANGE_SEISO_ACTION_REQUESTS = "seiso.action_requests";

	// Fanout exchanges ignore the routing key, so just use "".
	// http://www.rabbitmq.com/tutorials/tutorial-three-java.html
	// public static final String AMQP_DUMMY_ROUTING_KEY = "";
}
