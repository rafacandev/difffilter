package com.github.rafasantos.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.rafasantos.context.AppContext;
import com.github.rafasantos.formarter.LinePojoFormatter;
import com.github.rafasantos.pojo.LineDelimitedPojo;
import com.github.rafasantos.pojo.LinePojo;
import com.github.rafasantos.service.DiffService;

/**
 * Consolidates all necessary information to perform diffs.
 * Delegates the diff analysis to {@code DiffService}.
 * Additionally, apply the corresponding template according with its {@code LinePojo.diffType}.
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class DiffController {

	private DiffService diffService;

	/**
	 * Constructor which wires dependencies provided by {@code applicationContext}.
	 * @param applicationContext
	 */
	public DiffController(AppContext appContext) {
		diffService = appContext.getBean(DiffService.class);
	}

	/**
	 * Get the {@code List<LinePojo>} containing differences between the {@code leftReader} and the {@code rightReader} applying the 
	 * corresponding templates.
	 * @param leftReader
	 * @param rightReader
	 * @param uniqueKeyIndexes
	 * @param equalsTemplate
	 * @param insertTemplate
	 * @param updateTemplate
	 * @param deleteTemplate
	 * @return
	 * @throws IOException
	 */
	public List<LinePojo> getDiff(BufferedReader leftReader, BufferedReader rightReader,
			String uniqueKeyIndexes, String delimiter, String equalsTemplate, String insertTemplate,
			String updateTemplate, String deleteTemplate) throws IOException {

		//TODO change from int[] to List<Integer>
		int[] primaryKeys = getUniqueKeyIndexesAsInt(uniqueKeyIndexes);

		Map<String, LineDelimitedPojo> leftMap = diffService.readerToMap(leftReader, primaryKeys, delimiter);
		Map<String, LineDelimitedPojo> rightMap = diffService.readerToMap(rightReader, primaryKeys, delimiter);

		Set<LineDelimitedPojo> diffSet = diffService.getDiff(leftMap, rightMap);

		List<LinePojo> lineDiffs = new ArrayList<>(diffSet);

		DiffService.sort(lineDiffs);
		
		for (LinePojo p : lineDiffs) {
			LineDelimitedPojo l = (LineDelimitedPojo) p;
			String lineString = formatLinePojo(equalsTemplate, insertTemplate, updateTemplate, deleteTemplate, l);
			l.setDiffText(lineString);
		}
		return lineDiffs;
	}

	private String formatLinePojo(String equalsTemplate, String insertTemplate, String updateTemplate,
			String deleteTemplate, LineDelimitedPojo l) {
		String result = new String();
		switch (l.getDiffType()) {
		case EQUALS:
			result = LinePojoFormatter.formatTemplate(equalsTemplate, l) + "\n";
			break;
		case INSERTED:
			result = LinePojoFormatter.formatTemplate(insertTemplate, l) + "\n";
			break;
		case UPDATED:
			result = LinePojoFormatter.formatTemplate(updateTemplate, l) + "\n";
			break;
		case DELETED:
			result = LinePojoFormatter.formatTemplate(deleteTemplate, l) + "\n";
			break;
		default:
			break;
		}
		return result;
	}

	private int[] getUniqueKeyIndexesAsInt(String primaryKeyIndexes) {
		int primaryKeys[] = null;
		if (primaryKeyIndexes != null && !primaryKeyIndexes.isEmpty()) {
			if (primaryKeyIndexes.equals("ORIGINAL_LINE")) {
				return null;
			}
			String[] primaryKeyIndexesSplitted = primaryKeyIndexes.split(",");
			primaryKeys = new int[primaryKeyIndexesSplitted.length];
			for (int i = 0; i < primaryKeyIndexesSplitted.length; i++) {
				primaryKeys[i] = Integer.parseInt(primaryKeyIndexesSplitted[i]);
			}
		}
		return primaryKeys;
	}
}
