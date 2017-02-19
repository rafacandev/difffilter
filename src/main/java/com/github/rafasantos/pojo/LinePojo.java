package com.github.rafasantos.pojo;

/**
 * Plain old java object to describe a line when comparing diffs between two lines of text.
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public abstract class LinePojo {
	
	public enum DiffType {EQUALS, INSERTED, DELETED, UPDATED};
	
	private String primaryKey;
	private Long lineNumber;
	private String originalLine;
	private String diffText;
	private DiffType diffType;
	
	@Override
	public abstract boolean equals(Object o);

	public String getDiffText() {
		return diffText;
	}

	public DiffType getDiffType() {
		return diffType;
	}
	
	public Long getLineNumber() {
		return lineNumber;
	}

	public String getOriginalLine() {
		return originalLine;
	}
	
	public String getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public abstract int hashCode();
	
	public abstract boolean isIdentical(LinePojo other);

	public void setDiffText(String diffText) {
		this.diffText = diffText;
	}

	public void setDiffType(DiffType diffType) {
		this.diffType = diffType;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setOriginalLine(String originalLine) {
		this.originalLine = originalLine;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
}
