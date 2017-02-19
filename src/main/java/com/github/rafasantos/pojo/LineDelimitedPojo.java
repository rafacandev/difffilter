package com.github.rafasantos.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Plain old java object to describe a line from a character delimited text (e.g.: csv, tsv).
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class LineDelimitedPojo extends LinePojo {
	
	private Map<Integer,String> fields = new HashMap<>();
	
	public void addField(Integer index, String value) {
		this.fields.put(index, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineDelimitedPojo other = (LineDelimitedPojo) obj;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		return true;
	}

	public String getField(Integer index) {
		return this.fields.get(index);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		return result;
	}

	public boolean isIdentical(LinePojo other) {
		return equals(other);
	}

}
