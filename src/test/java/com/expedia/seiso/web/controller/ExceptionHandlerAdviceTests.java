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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;
import lombok.val;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import com.expedia.seiso.core.exception.InvalidRequestException;
import com.expedia.seiso.core.exception.ResourceNotFoundException;
import com.expedia.seiso.core.util.C;
import com.expedia.seiso.web.controller.error.ValidationErrorMap;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
public class ExceptionHandlerAdviceTests {

	// Class under test
	private ExceptionHandlerAdvice advice;

	// Test data
	private InvalidRequestException invalidRequestException;
	private ResourceNotFoundException resourceNotFoundException;
	private JsonMappingException jsonMappingException;
	private RuntimeException runtimeException;

	@Mock
	private BindingResult bindingResult;
	@Mock
	private WebRequest webRequest;

	@Before
	public void init() throws Exception {
		this.advice = new ExceptionHandlerAdvice();
		MockitoAnnotations.initMocks(this);
		initTestData();
	}

	private void initTestData() {
		this.invalidRequestException = new InvalidRequestException("my-ire-message", bindingResult);
		this.resourceNotFoundException = new ResourceNotFoundException("my-rnfe-message");
		this.jsonMappingException = new JsonMappingException("my-jme-message");
		this.runtimeException = new RuntimeException("my-re-message");
	}

	@Test
	public void handleInvalidRequestException() {
		val result = advice.handleInvalidRequestException(invalidRequestException, webRequest);
		assertNotNull(result);
		assertEquals(C.EC_INVALID_REQUEST, result.getCode());
		assertEquals(invalidRequestException.getMessage(), result.getMessage());
	}

	@Test
	public void handleNoSuchItemException() {
		val result = advice.handleNoSuchItemException(resourceNotFoundException, webRequest);
		assertNotNull(result);
		assertEquals(C.EC_RESOURCE_NOT_FOUND, result.getCode());
		assertNotNull(result.getMessage());
	}

	@Test
	public void handleJsonMappingException() {
		val result = advice.handleJsonMappingException(jsonMappingException, webRequest);
		assertNotNull(result);
		assertEquals(C.EC_INVALID_REQUEST_JSON_PAYLOAD, result.getCode());
		assertNotNull(result.getMessage());
	}

	@Test
	public void handleRuntimeException() {
		val result = advice.handleRuntimeException(runtimeException, webRequest);
		assertNotNull(result);
		assertEquals(C.EC_INTERNAL_ERROR, result.getCode());
		assertNotNull(result.getMessage());
	}

	@Test
	public void handleBindExceptionException() throws Exception {

		ObjectError expectedError = new ObjectError("name", "message");
		ValidationErrorMap expected = new ValidationErrorMap();
		expected.addError(expectedError.getObjectName(), expectedError.getDefaultMessage());

		BindException bindException = new BindException(this, "foo");
		bindException.addError(expectedError);
		val actual = advice.handleBindException(bindException);

		Assert.assertEquals(expected.errorMap, actual.errorMap);
	}
}
