package com.bokesoft.ecomm.estore.utils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DBTools {

	public static List<Map<String, Object>> wrapDBResult(ResultSet resultSet) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Iterator<Map<String, Object>> iterator = result.iterator();
		if(iterator.hasNext()){
			result.add(iterator.next());
		}
		return result;
	}

	
	
}
