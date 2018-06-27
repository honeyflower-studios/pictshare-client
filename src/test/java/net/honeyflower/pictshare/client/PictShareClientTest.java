package net.honeyflower.pictshare.client;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@RestClientTest(PictShareClient.class)
public class PictShareClientTest {

	@Autowired
    private PictShareClient client;
 
    /*@Autowired
    private MockRestServiceServer server;*/
 
    /*@Autowired
    private ObjectMapper objectMapper;*/
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
    @Test
    public void testUploadPictureBase64() throws Exception {
    	
    	String base64Image = imageToBase64Encoder(getClassPathResource("Test.png"));
    	UploadResult result = client.uploadBase64(base64Image);
        System.out.println(result);
        assertTrue(result.isStatusOK());
    }
    
    
    @Test
    public void testUploadPicture() throws Exception {
    	
    	UploadResult result = client.upload(imageToBytes(getClassPathResource("Test.png")));
    	assertTrue(result.isStatusOK());
    }
    
    
	private byte[] imageToBytes(AbstractResource imagePath) throws IOException {
		byte imageData[];
		try (InputStream imageInFile = imagePath.getInputStream()) {
			// Reading a Image file from file system
			imageData = new byte[(int) imagePath.contentLength()];
			imageInFile.read(imageData);
		} catch (IOException ioe) {
			throw ioe;
		}
		return imageData;
	}
	
	private String imageToBase64Encoder(AbstractResource imagePath) throws IOException {
		String base64Image = "";
		byte imageData[] = imageToBytes(imagePath);
		base64Image = "data:base64," + Base64.getEncoder().encodeToString(imageData);
		return base64Image;
	}
	
	private ClassPathResource getClassPathResource(String path) {
		return new ClassPathResource(path);
}

}
