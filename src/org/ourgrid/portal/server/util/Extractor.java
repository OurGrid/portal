package org.ourgrid.portal.server.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Extractor {

	public List<File> extract(String path, String destination) throws IOException;
	
	public File compress(String path, String parentPath, String destination) throws IOException;

}
