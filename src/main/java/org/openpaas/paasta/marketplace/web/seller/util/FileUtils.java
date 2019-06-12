package org.openpaas.paasta.marketplace.web.seller.util;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

	public static String fileUpload(String path, MultipartFile partFile) throws Exception {
		String newFileName = "";

		File file = new File(path);
		if (file.exists() == false) {
			file.mkdirs();
		}
		log.info(partFile.getOriginalFilename() + ", " + partFile.getName() + ", " + partFile.getContentType() + ", " + partFile.getResource());

		if (partFile.getContentType().contains("image")) {
			newFileName = Long.toString(System.nanoTime()) + "." + partFile.getContentType().replace("image/", "");
		} else {
			newFileName = partFile.getOriginalFilename();
		}
		log.info("File Name: " + newFileName);
		file = new File(path + newFileName);
		partFile.transferTo(file);

		return newFileName;
	}

}
