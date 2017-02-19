package com.github.rafasantos.transformer;

import com.github.rafasantos.pojo.LineDelimitedPojo;
import com.github.rafasantos.util.AppConstants;

// TODO Make this generic
public class LineTransformer {

	public LineDelimitedPojo transform(String s, long lineNumber, int[] primaryKeys, String delimiter) {
		String[] sArray = s.split(AppConstants.FIELD_DELIMITER);
		LineDelimitedPojo result = new LineDelimitedPojo();
		result.setLineNumber(lineNumber);
		result.setOriginalLine(s);
		String[] originalLineSplitted = s.split(delimiter);
		StringBuffer pkStringBuffer = new StringBuffer();
		if (primaryKeys != null) {	
			for(int i = 0; i < primaryKeys.length; i++) {
				pkStringBuffer.append(originalLineSplitted[primaryKeys[i]]);
			}
			result.setPrimaryKey(pkStringBuffer.toString());
		} else {
			result.setPrimaryKey(s);
		}
		for (int i = 0; i < sArray.length; i++) {
			result.addField(i, sArray[i]);
		}
		return result;
	}

}
