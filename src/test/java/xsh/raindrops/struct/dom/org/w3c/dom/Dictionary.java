package xsh.raindrops.struct.dom.org.w3c.dom;

/**
 * @author Raindrops on 2017年7月26日
 */
public class Dictionary {
	/**
	 * 编号
	 */
	private String dicCode;
	
	/**
	 * 项目名称
	 */
	private String dicName;
	
	/**
	 * 单位
	 */
	private String dicUnit;
	
	/**
	 * 英文名
	 */
	private String enName;
	
	/**
	 * 检查结果
	 */
	private String dicResult;
	
	/**
	 * 提示
	 */
	private String tip;
	
	/**
	 * 参考结果
	 */
	private String dicExplain;
	
	/**
	 * 科室名称
	 */
	private String departName;
	
	/**
	 * 机构组织
	 */
	private Integer instGroup;

	
	
	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getDicResult() {
		return dicResult;
	}

	public void setDicResult(String dicResult) {
		this.dicResult = dicResult;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getDicExplain() {
		return dicExplain;
	}

	public void setDicExplain(String dicExplain) {
		this.dicExplain = dicExplain;
	}

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getDicUnit() {
		return dicUnit;
	}

	public void setDicUnit(String dicUnit) {
		this.dicUnit = dicUnit;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public Integer getInstGroup() {
		return instGroup;
	}

	public void setInstGroup(Integer instGroup) {
		this.instGroup = instGroup;
	}

	@Override
	public String toString() {
		return "Dictionary [编号=" + dicCode + ", 项目名称=" + dicName + ", 单位=" + dicUnit + ", 英文名=" + enName
				+ ", 检查结果=" + dicResult + ", 提示=" + tip + ", 参考结果=" + dicExplain + ", 科室="
				+ departName + ", 组织机构=" + instGroup + "]";
	}
	
	
	
}
