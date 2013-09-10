package org.ourgrid.portal.client.plugin.blender.util;

import java.util.Map;
import java.util.TreeMap;

public class BlenderPropertiesUtil {
	
	
	public static Map<String, String> getBlenderPropertiesOutputTypeFile(){
		
		Map<String,String> map = new TreeMap<String,String>();
		
		map.put("AVICODEC", "avi");
		map.put("AVIJPEG", "avi");
		map.put("AVIRAW", "avi");
		map.put("BMP", "bmp");
		map.put("CINEON", "cin");
		map.put("DPX", "dpx");
		map.put("EXR", "exr");
		map.put("HAMX", "jpg");
		map.put("HDR", "hdr");
		map.put("IRIS", "rgb");
		map.put("JPEG", "jpg");
		map.put("JP2", "jp2");
		map.put("MPEG", "dvd");
		map.put("PNG", "png");
		map.put("RAWTGA", "tga");
		map.put("TGA", "tga");
		map.put("TIFF", "tif");
		
		return map;
	}

}
