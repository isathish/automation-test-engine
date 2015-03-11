/*******************************************************************************
 * ATE, Automation Test Engine
 *
 * Copyright 2014, Montreal PROT, or individual contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Montreal PROT.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.bigtester.ate.model.data.dbtable;
import javax.persistence.GenerationType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

// TODO: Auto-generated Javadoc
/**
 * This class AbstractDBTable defines ....
 * @author Peidong Hu
 *
 */
@MappedSuperclass
public abstract class BaseDBTable { //NOPMD
	
	
	/** The id Column. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="idColumn")
	
	/**
	 * Gets the id Column.
	 *
	 * @return the id Column
	 */
	@Getter
	
	/**
	 * Sets the id Column.
	 *
	 * @param cid the new id Column
	 */
	@Setter
	private long idColumn; //NOPMD
	
	
}
