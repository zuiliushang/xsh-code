package xsh.raindrops.struct.dom.org.w3c.dom;

import java.util.Date;
import java.util.List;

public class ReportResultData {

	/**
	 * 文件里解析出来的体检项
	 */
	private List<Dictionary> dictionaries;
	
	/**
	 * 文件里解析出来的用户信息
	 */
	private SourceUser sourceUser;
	
	/**
	 * 文件里解析出来的体检结果
	 */
	private String summary;
	
	/**
	 * 文件里解析出来的体检建议
	 */
	private String suggest;
	
	/**
	 * 文件里解析出来的报告编号
	 */
	private String reportCode;
	
	/**
	 * 文件里解析出来的体检机构编号
	 */
	private String institutionCode;

	/**
	 * 检查时间
	 */
	private Date examTime;

	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	public SourceUser getSourceUser() {
		return sourceUser;
	}

	public void setSourceUser(SourceUser sourceUser) {
		this.sourceUser = sourceUser;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getInstitutionCode() {
		return institutionCode;
	}

	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}

	public Date getExamTime() {
		return examTime;
	}

	public void setExamTime(Date examTime) {
		this.examTime = examTime;
	}

	@Override
	public String toString() {
		return "ReportResultData [dictionaries=" + dictionaries + ", sourceUser=" + sourceUser + ", summary=" + summary
				+ ", suggest=" + suggest + ", reportCode=" + reportCode + ", institutionCode=" + institutionCode
				+ ", examTime=" + examTime + "]";
	}
	
}
