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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ourgrid.portal.server.util.FileUtil;

public class DataStoredDAO {

	private Map<Long, String> uploadId2RootDir;
	
	public DataStoredDAO() {
		this.uploadId2RootDir = new HashMap<Long, String>();
	}
	
	public void createDataStore(Long uploadId, String rootDir) {
		this.uploadId2RootDir.put(uploadId, rootDir);
	}
	
	public String getDataStore(Long uploadId) {
		return this.uploadId2RootDir.get(uploadId);
	}

	public File getStoredFileByName(Long uploadId, String fileName) {
		String dataStore = getDataStore(uploadId);
		
		if (dataStore == null) {
			return null;
		}
		
		List<File> files = FileUtil.getFiles(new File(dataStore));

		for (File file : files) {
			if (file.getName().equals(fileName)) {
				return file;
			}
		}
		
		return null;
	}
	
}