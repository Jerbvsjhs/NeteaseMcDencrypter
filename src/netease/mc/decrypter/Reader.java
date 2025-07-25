package netease.mc.decrypter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Reader {
	
	public static InputStream readFile(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	public static byte[] readFileB(File file) throws IOException {
		InputStream inputStream = readFile(file);
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		return buffer;
	}
	/*
	 * 2025 7.7
	 * 优化逻辑分支
	 */
	public static boolean checkHeader(byte[] b, byte[] header) {
		if (b.length != header.length) {
			return false;
		}
		for (int i = 0; i < b.length; i++) {
			if (b[i] != header[i]) {
				return false;
			}
		}
		return true;
	}
	
	public static byte[] readCheckHeader(File file, byte[] header) throws IOException {
		byte[] tHeader = new byte[4];
		InputStream inputStream = readFile(file);
		byte[] buffer = null;
		if (file.length() < tHeader.length) {
			return null;
		}
		if (inputStream.read(tHeader) != -1) {
			if (checkHeader(tHeader, Crypt.header)) {
				buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				return buffer;
			}
			System.err.println("E:" + file.getName() + "读取时判断头不匹配");
			return null;
		}
		return null;
	}
	
	public static byte[] getHeader(File file) throws IOException {
		InputStream inputStream = readFile(file);
		byte[] header = new byte[4];
		if (inputStream.read(header) == -1) {
			System.err.println("W:" + file.getName() + "未包含文件头");
		}
		return header;
	}
	
	public static void write(File file, byte[] buffer) throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		if (file.isDirectory()) {
			file.delete();
			file.createNewFile();
		}
		if (buffer == null) {
			return;
		}
		try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			fileOutputStream.write(buffer);
		}
	}
}
