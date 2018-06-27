package net.honeyflower.pictshare.client;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UploadResult {
	
	private static final String okStatus="OK";
	
	@NotEmpty
    private String status; //":"OK",
    private String type; //":"png",
    
    @NotEmpty
    private String hash; //":"1x8lrtzfp3.png",
    
    @NotEmpty
    @URL
    private String url; //":"http://img.dev.rebring.com/1x8lrtzfp3.png",
    //"domain":"http://img.dev.rebring.com",
    private String deletecode;//":"zjfdj091wd0qs4d6hqnsdppmagf9w4dv"
    
    
    public boolean isStatusOK() {
    	return status.equalsIgnoreCase(okStatus);
    }

}
