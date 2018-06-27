package net.honeyflower.pictshare.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class PictShareClient {
	
	private final RestTemplate restTemplate;
		
	private final String uploadUrl;
	private final String deleteBaseUrl;
	
	private static final List<MediaType> accepts = Arrays.asList(MediaType.APPLICATION_JSON);
	private static final String userAgent = "honeyflower-systems";
	 
    public PictShareClient(RestTemplateBuilder restTemplateBuilder, String baseUrl, String uploadCode, String deleteCode) {
    	ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().set(HttpHeaders.USER_AGENT, userAgent);
            request.getHeaders().setAccept(accepts);
            return execution.execute(request, body);
        };
        
        restTemplate = restTemplateBuilder.rootUri(baseUrl).additionalInterceptors(interceptor).build();
        
        if (StringUtils.hasText(uploadCode)) {
        	uploadUrl = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment("backend.php").queryParam("upload_code", uploadCode).toUriString();
        } else {
        	uploadUrl = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment("backend.php").toUriString();;
        }
        
        if (StringUtils.hasText(deleteCode)) {
        	deleteBaseUrl = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment("delete_" + deleteCode).toUriString();
        } else {
        	deleteBaseUrl = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment("delete").toUriString();
        }
    }
    
    public PictShareClient(String baseUrl, String uploadCode, String deleteCode) {
    	this(new RestTemplateBuilder(), baseUrl, uploadCode, deleteCode);
    }
    
    public UploadResult uploadBase64(String base64Image) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    	MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
    	map.add("base64", base64Image);

    	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

    	ResponseEntity<UploadResult> response = restTemplate.postForEntity(uploadUrl, request , UploadResult.class );
    	return response.getBody();
    }
    
    public UploadResult upload(byte[] image) {
    	ByteArrayResource fileAsResource = new ByteArrayResource(image) {
            @Override
            public String getFilename() {
                return "lalala";
            }
        };
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("postimage", fileAsResource);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<UploadResult> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, UploadResult.class);
        return response.getBody();
    }
    
    public String delete(String pictureHash) {
    	String deleteUrl = UriComponentsBuilder.fromHttpUrl(deleteBaseUrl).pathSegment(pictureHash).toUriString();
    	return restTemplate.getForEntity(deleteUrl, String.class).getBody();
    }
}
