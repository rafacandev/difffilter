package com.github.rafasantos.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.rafasantos.context.AppContext;
import com.github.rafasantos.pojo.LineDelimitedPojo;
import com.github.rafasantos.pojo.LinePojo;
import com.github.rafasantos.transformer.LineTransformer;

/**
 * Processes differences between maps of {@code Map<String, LinePojo>}.
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class DiffService {
	
	private LineTransformer transformer;

	public DiffService(AppContext appContext) {
		transformer = appContext.getBean(LineTransformer.class);
	}

	/**
	 * Return a {@code Set<LinePojo>} with the differences between the {@code leftMap}
	 * and the {@code rightMap}.
	 * @param leftMap
	 * @param rightMap
	 * @return
	 */
	public Set<LineDelimitedPojo> getDiff(Map<String, LineDelimitedPojo> leftMap, Map<String, LineDelimitedPojo> rightMap) {
		// TODO use Java 8 Streams
		Set<LineDelimitedPojo> result = new HashSet<>();
		for (String key : rightMap.keySet()) {
			// if is an key that has been updated
			if (leftMap.containsKey(key)) {
				LineDelimitedPojo leftLine = leftMap.get(key);
				LineDelimitedPojo rightLine = rightMap.get(key);
				if (!leftLine.isIdentical(rightLine)) {
					rightLine.setDiffType(LineDelimitedPojo.DiffType.UPDATED);
					result.add(rightLine);
				} else {
					rightLine.setDiffType(LineDelimitedPojo.DiffType.EQUALS);
					result.add(rightLine);
				}
			}
			// if is an key that has been inserted
			if (!leftMap.containsKey(key)) {
				LineDelimitedPojo l = rightMap.get(key); 
				l.setDiffType(LineDelimitedPojo.DiffType.INSERTED);
				result.add(l);
			}
		}
		
		for (String key : leftMap.keySet()) {
			if (!rightMap.containsKey(key)) {
				LineDelimitedPojo l = leftMap.get(key);
				l.setDiffType(LineDelimitedPojo.DiffType.DELETED);
				result.add(l);
			}
		}
		return result;
	}
	
	/**
	 * Converts from a {@code BufferedReader} to a {@code Map<String, LinePojo>}
	 * @param reader
	 * @param primaryKeys
	 * @return
	 * @throws IOException
	 */
	public Map<String, LineDelimitedPojo> readerToMap(BufferedReader reader, int[] primaryKeys, String delimiter) throws IOException {
		Map<String, LineDelimitedPojo> result = new LinkedHashMap<>();
		String lineString;
		long lineNumber = 1;
		while ((lineString = reader.readLine()) != null) {
			LineDelimitedPojo l = this.transformer.transform(lineString, lineNumber, primaryKeys, delimiter);
			result.put(l.getPrimaryKey(), l);
			lineNumber++;
		}
		return result;
	}

	/**
	 * Sort {@code lines} preferably by line numbers.
	 * @param lines
	 */
	public static void sort(List<LinePojo> lines) {
		lines.sort((LinePojo a, LinePojo b) -> {
			if (a.getLineNumber() == b.getLineNumber()) {
				if (a.getDiffType() == LineDelimitedPojo.DiffType.DELETED) {
					return -1;
				} else if (b.getDiffType() == LineDelimitedPojo.DiffType.DELETED) {
					return 1;
				}
			} else {
				return ((a.getLineNumber() > b.getLineNumber()) ? 1 : -1);
			}
			return 0;
		});
	}

}
