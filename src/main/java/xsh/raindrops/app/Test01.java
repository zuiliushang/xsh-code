package xsh.raindrops.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.druid.util.StringUtils;
import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@Import(FdfsClientConfig.class)
public class Test01 {
	
	@Autowired
	private FastFileStorageClient storageClient;
	
	@Test
	public void test() throws FileNotFoundException {
		File file = new File("F:\\1.png");
		 String fileName=file.getName();
		 FileInputStream inputStream=new FileInputStream(file);
         String strs= fileName.substring(fileName.lastIndexOf(".") + 1);  ;
         if(StringUtils.isEmpty(strs)){
             return ;
         }
         StorePath storePath= storageClient.uploadImageAndCrtThumbImage(inputStream,file.length(),strs,null);
         System.out.println("path------"+storePath.getFullPath());
     }
	
}
