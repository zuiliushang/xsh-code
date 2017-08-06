package xsh.raindrops.struct.dom.org.w3c.dom;

public class SourceUser {

	private String name;
	
	private String identityNumber;
	
	private String gender;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "SourceUser [name=" + name + ", identityNumber=" + identityNumber + ", gender=" + gender + "]";
	}
	
	
	
}
