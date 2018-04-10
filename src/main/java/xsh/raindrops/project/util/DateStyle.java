package xsh.raindrops.project.util;

/**
 * 
 * 日期格式
 * @author xusihan
 *
 */
public enum DateStyle {
	
	MM_DD("MM-dd"),  
    YYYY_MM("yyyy-MM"),  
    YYYY_MM_DD("yyyy-MM-dd"),  
    MM_DD_HH_MM("MM-dd HH:mm"),  
    MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),  
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),  
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),  
    YYYY_MM_DD_HH_MM_SS_("yyyyMMddHHmmss"),
    
    YYYY_MM_DD_DOT("YYYY.MM.dd"),
    
    
    HH_MM("HH:mm"),  
    HH_MM_SS("HH:mm:ss"), 
    DD_HH_MM("dd日 HH:mm"),
    
    YYYYMMdd_Cn("yyyy年MM月dd日")
    
    ;
    
    private String value;
    
    DateStyle(String value) {
        this.value = value;  
    }  
      
    public String getValue() {
        return value;  
    }  
}
