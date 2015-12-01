/* 
 * Copyright 2013-2016 the original author or authors.
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
package com.expedia.seiso.domain.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.hibernate.validator.constraints.Email;

/**
 * @author Willie Wheeler
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false, of = "username")
@ToString(of = { "username", "firstName", "lastName" })
@Entity
public class Person extends AbstractItem {
	
	@NotNull
	@Size(min = 1, max = 40)
	private String username;
	
	@Size(max = 40)
	private String firstName;
	
	@Size(max = 40)
	private String lastName;
	
	@Size(max = 160)
	private String displayName;
	
	@Size(max = 80)
	private String title;
	
	@Size(max = 80)
	private String company;
	
	@Size(max = 80)
	private String department;
	
	@Size(max = 80)
	private String division;
	
	@Size(max = 80)
	private String subdivision;
	
	@Size(max = 80)
	private String location;
	
	@Size(max = 160)
	private String streetAddress;
	
	@Size(max = 40)
	private String workPhone;
	
	@Email
	private String email;

	// Not sure we want this here for the long term, but maybe. After all we will want to store Amazon instance IDs
	// with our own machines. [WLW]	
	@Size(max = 240)
	private String ldapDn;
	
	// Don't get too attached to this as it's just for fun, and probably won't last.
	@Size(min = 4, max = 4)
	private String mbType;
	
	// Setting this lazy. Otherwise Hibernate is issuing a separate select for it. :-O
	// UGH, having lazy here means there will be a proxy, and this was causing an EntityLinks lookup to fail since it
	// was doing that by classname. (The proxy has a funky classname.) [WLW]
	// @ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Person manager;

	@OrderBy("firstName, lastName, id")
	@OneToMany(mappedBy = "manager")
	private List<Person> directReports;
}
