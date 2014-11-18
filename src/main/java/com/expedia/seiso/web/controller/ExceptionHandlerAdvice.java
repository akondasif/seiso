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

import lombok.val;
import lombok.extern.slf4j.XSlf4j;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.expedia.seiso.core.exception.InvalidRequestException;
import com.expedia.seiso.core.exception.ResourceNotFoundException;
import com.expedia.seiso.core.util.C;
import com.expedia.seiso.domain.service.response.ErrorObject;
import com.expedia.seiso.web.controller.error.ValidationErrorMap;
import com.expedia.seiso.web.controller.error.ValidationErrorMapFactory;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author Willie Wheeler (wwheeler@expedia.com)
 */
@ControllerAdvice
@XSlf4j
public class ExceptionHandlerAdvice {
	
	@ExceptionHandler(InvalidRequestException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public ErrorObject handleInvalidRequestException(InvalidRequestException e, WebRequest request) {
		// TODO Want to be able to report on the specific invalid fields. [WLW]
		return new ErrorObject(C.EC_INVALID_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorObject handleNoSuchItemException(ResourceNotFoundException e, WebRequest request) {
		return new ErrorObject(C.EC_RESOURCE_NOT_FOUND, e.getMessage());
	}
	
	// TODO Handle other kinds of JSON issue, like illegal JSON, wrong schema, etc. [WLW]
	
	// FIXME Oh, this doesn't fire, because it's the deserializer that throws the exception, not the controller.
	@ExceptionHandler(JsonMappingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorObject handleJsonMappingException(JsonMappingException e, WebRequest request) {
		return new ErrorObject(C.EC_INVALID_REQUEST_JSON_PAYLOAD, e.getMessage());
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorObject handleRuntimeException(RuntimeException e, WebRequest request) {
		log.error("Internal server error", e);
		val fullMsg = e.getClass().getName() + ": " + e.getMessage();
		return new ErrorObject(C.EC_INTERNAL_ERROR, fullMsg);
	}
	
	
    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ValidationErrorMap handleBindException(BindException bindException) {
        
        ValidationErrorMap validationErrorMap = ValidationErrorMapFactory.buildFrom( bindException );
        
        return validationErrorMap;
    }
}
