package fsGuns;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileChecker {
	static String Resources[] = {
		"Instructions.txt",
		"accessory.yml",
		"frame.yml",
		"bullet.yml",
		"config.yml"
	};
	
	static public void checkAndGenerate(fsGunsPlugin p,File folder) {
		if(!folder.exists()) {
			folder.mkdir();
		}
		byte buffer[] = new byte[1024];
		for(String rname: Resources) {
			File f = new File(folder, rname);
			if(!f.exists()) {
				InputStream is = p.getClass().getResourceAsStream("/resource/" + rname);
				if(is != null) {
					try {
						OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
						while(true) {
							int s = is.read(buffer);
							if(s == -1)break;
							out.write(buffer,0,s);
						}
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
