package netease.mc.decrypter;

import java.io.File;
import java.io.IOException;

public class World {
	/*
	 * 这个类将用于支持同时加解密多个存档
	 * 
	 * 我对变量并没有加修饰
	 * 懒得写getter和setter了
	 * 这不是好的代码习惯
	 */
	File CURRENT_f, MANIFEST_f;
	boolean canDecrypted = false;
	boolean crypted = true;
	boolean loaded = false;
	byte[] key;
	File db;
	File[] files;

	public World(File dbFile) {
		if (!dbFile.exists() || !dbFile.isDirectory()) {
			System.err.println("E:db路径无效!");
			return;
		}
		
		db = dbFile;
		files = db.listFiles(); 
		
		if (files == null) {
			System.err.println("E:db损坏！");
			return;
		}
		/*
		 * 遍历出manifest与current文件
		 * 写了点优化
		 */
		for (File file : files) {
			if (!file.isFile()) {
				continue;
			}
			String name = file.getName(); //避免多次调用getname
			if (name.startsWith("MANIFEST") && MANIFEST_f == null) {
				MANIFEST_f = file;
			}
			if (name.equals("CURRENT") && CURRENT_f == null) {
				CURRENT_f = file;
			}
			if (CURRENT_f != null && MANIFEST_f != null) {
				System.out.println("I:" + CURRENT_f.getName() + "," + MANIFEST_f.getName() + "读取成功!");
				break;
			}
		}
		
		if (CURRENT_f == null || MANIFEST_f == null) {
			System.err.println("E:" + "存档缺失CURRENT或MANIFEST文件!");
			return;
		}
		
		loaded = true;
		System.out.println("I:db已成功读取!");
		/*
		 * 检测存档加密
		 * 没有就返回
		 */
		try {
			byte[] header = Reader.getHeader(CURRENT_f);
			canDecrypted = Crypt.canDencryt(header);
			if (!canDecrypted) { //无法解密的存档也包含加密存档
				if (Reader.checkHeader(header, Crypt.cHeader)) {
					crypted = false;
					System.out.println("I:读取到未加密存档");
				}
				return;
			}
		} catch (IOException e) {
			System.err.println("W:" + "检测存档加密失败" + e.getMessage());
			return;
		}
		
		try {
			byte[] tKey = Crypt.getKey(CURRENT_f, MANIFEST_f.getName());
			if (tKey == null) {
				System.err.println("E:秘钥获取失败!");
				return;
			}
			key = tKey;
			System.out.println("I:获取到秘钥:");
			Utils.printBytes(key);
		} catch (IOException e) {
			System.err.println("E:获取存档秘钥失败" + e.getMessage());
		}
	}
	
	public boolean isOldEncrypt() {
		if (!canDecrypted && crypted) {
			return true;
		}
		return false;
	}
}
