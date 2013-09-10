/*
 * Copyright (C) 2008 Universidade Federal de Campina Grande
 *  
 * This file is part of OurGrid. 
 *
 * OurGrid is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.ourgrid.portal.server.logic.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;

public class RequestDAO {

	private Map<Integer, AbstractRequest> requests;
	
	public RequestDAO() {
		requests = new HashMap<Integer, AbstractRequest>();
	}

	public void addRequest(Integer jobId, AbstractRequest request) {
		requests.put(jobId, request);
	}
	
	public AbstractRequest getRequest(Integer jobId) {
		return requests.get(jobId);
	}

	public void removeRequest(Integer jobId) {
		requests.remove(jobId);
	}

	public List<AbstractRequest> getJobsRequestByLogin(String login) {
		
		List<AbstractRequest> jobs = new LinkedList<AbstractRequest>();
		
		for (AbstractRequest request : requests.values()) {
			if (request.getUserLogin().equals(login)) {
				jobs.add(request);
			}
		}
		
		Collections.sort(jobs);
		
		return jobs;
	}
}