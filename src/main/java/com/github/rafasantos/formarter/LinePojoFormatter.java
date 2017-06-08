package com.github.rafasantos.formarter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.rafasantos.pojo.LineDelimitedPojo;
import com.github.rafasantos.util.AppConstants;

public class LinePojoFormatter {
	
	/**
	 * Returns a formated string for a given {@code LineDlimitedPojo} according to the {@code template}.
	 * 
	 * @param template
	 * @param line
	 * @return formated line string based on the template
	 */
	public static String formatTemplate(String template, LineDelimitedPojo line) {
		Set<String> matches = findMatches(template);
		for (String match : matches) {
			if (AppConstants.IGNORE_LINE.equals(match)) {
				template = "";
				return null;
			} else if (AppConstants.ORIGINAL_LINE.equals(match)) {
				template = template.replace("{" + match + "}", line.getOriginalLine());
			} else {
				String value = "";
				Integer key = new Integer(match);
				String valueCandidate = line.getField(key);
				if (valueCandidate != null) {
					value = valueCandidate;
				}
				template = template.replace("{" + match + "}", value);
			}
		}
		return template;
	}

	// Find matches in the template
	private static Set<String> findMatches(final String template) {
		Set<String> result = new HashSet<>();
		if (template != null) {
			Pattern pattern = Pattern.compile("\\{\\d+\\}|"+AppConstants.ORIGINAL_LINE +"|"+AppConstants.IGNORE_LINE);
			Matcher matcher = pattern.matcher(template);
			while (matcher.find()) {
				if (AppConstants.ORIGINAL_LINE.equals(template.substring(matcher.start(), matcher.end()))
						|| AppConstants.IGNORE_LINE.equals(template.substring(matcher.start(), matcher.end()))) {
					result.add(template.substring(matcher.start(), matcher.end()));
				} else {
					int startMatcher = matcher.start() + 1;
					int endMatcher = matcher.end() - 1;
					result.add(template.substring(startMatcher, endMatcher));
				}
			}
		}
		return result;
	}

}
