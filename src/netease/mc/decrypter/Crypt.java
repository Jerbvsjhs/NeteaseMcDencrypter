package netease.mc.decrypter;

import java.io.File;
import java.io.IOException;

public class Crypt {
	public static byte[] header = { (byte) 0x80, 0x1D, 0x30, 0x1 };
	public static byte[] bHeader = { (byte) 0x90, 0x1D, 0x30, 0x1}; //旧版加密头
	public static byte[] cHeader = { (byte) 0x4d, 0x41, 0x4e, 0x49}; //MANI
	
	public static boolean canDencryt(byte[] h) { //旧版无法解密，就当未加密了
		if (Reader.checkHeader(h, header)) {
			return true;
		}
		/*
		 * 旧版加密方式为aes cfb8
		 * 该算法目前没有漏洞
		 * 获取秘钥的唯一方法就是调用冈易api
		 * 抱歉了大伙 
		 * 这些超出了我的能力范围
		 * 如果有会逆向的欢迎来帮我
		 */
		return false;
	}
	
	public static byte[] addByte(byte[] old, byte b) {
		byte[] r = new byte[old.length + 1];
		for (int i = 0; i < old.length; i++) {
			r[i] = old[i];
		}
		r[old.length] = b;
		return r;
	}
	
	public static byte[] addHeader(byte[] header, byte[] buff) {
		int length = header.length + buff.length;
		byte[] r = new byte[length];
		for (int i = 0; i < r.length; i++) {
			if (i < header.length) {
				r[i] = header[i];
				continue;
			}
			r[i] = buff[i - header.length];
		}
		return r;
	}
	
	public static byte[] xor(byte[] p, byte[] k) {
		byte[] data = new byte[p.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) (p[i] ^ k[i % k.length]);
		}
		return data;
	}
	
	public static byte[] getKey(File currentFile, String manifestName) throws IOException {
		byte[] source = addByte(manifestName.getBytes(), (byte) 0x0A);
		byte[] bin = Reader.readCheckHeader(currentFile, header);
		
		if (bin == null) { //将空值传递防止错误扩大
			return null;
		}
		return optimizeKey(xor(bin, source));
	}
	
	public static void saveKey(byte[] key, File file) throws IOException {
		Reader.write(file, key);
	}
	
	public static byte[] readKey(File kfile) throws IOException {
		return Reader.readFileB(kfile);
	}
	
	public static byte[] optimizeKey(byte[] key) { //length = 16
		byte[] b = new byte[8];
		for (int i = 0; i < (key.length / 2); i++) {
			b[i] = key[i];
			if (key[i] != key[i+8]) {
				System.err.println("W:秘钥过长,存档内容可能损坏！");
				return key;
			}
		}
		return b;
	}
	
	public static byte[] dEncrypt(File file, byte[] key) throws IOException {
		byte[] b = Reader.readCheckHeader(file, header);
		return xor(b, key);
	}
	
	public static byte[] crypt(File file, byte[] key) throws IOException {
		byte[] b = Reader.readFileB(file);
		return xor(b, key);
	}
}
