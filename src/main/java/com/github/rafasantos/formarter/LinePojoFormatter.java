package com.github.rafasantos.formarter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.rafasantos.pojo.LineDelimitedPojo;
import com.github.rafasantos.util.AppConstants;

public class LinePojoFormatter {
	

	public static String formatTemplate(String template, LineDelimitedPojo p) {
		Set<String> matches = findMatches(template);
		for (String s : matches) {
			if (AppConstants.ORIGINAL_LINE.equals(s)) {
				template = template.replaceAll("\\{" + s + "\\}", p.getOriginalLine());
			} else {
				String value = "";
				try {
					Integer key = new Integer(s);
					String valueCandidate = p.getField(key);
					if (valueCandidate != null) {
						value = valueCandidate;
					}
				} catch (Exception e) {
					// TODO: log exception
				}
				template = template.replaceAll("\\{" + s + "\\}", value);
			}
		}
		return template;
	}

	private static Set<String> findMatches(String template) {
		Set<String> result = new HashSet<>();
		if (template != null) {
			String s = template;
			Pattern p = Pattern.compile("\\{\\d+\\}|"+AppConstants.ORIGINAL_LINE);
			Matcher m = p.matcher(template);
			while (m.find()) {
				if (AppConstants.ORIGINAL_LINE.equals(s.substring(m.start(), m.end()))) {
					result.add(s.substring(m.start(), m.end()));
				} else {
					int startMatcher = m.start() + 1;
					int endMatcher = m.end() - 1;
					result.add(s.substring(startMatcher, endMatcher));
				}
			}
		}
		return result;
	}

}
