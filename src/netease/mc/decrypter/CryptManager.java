package netease.mc.decrypter;

import java.io.File;
import java.io.IOException;

public class CryptManager {
	/*
	 * 这个类封装了存档的解密与加密操作
	 */
	static long currntTime;
	public static void decryptWorld(World world) {
		if (!world.loaded) { //损坏的存档不能解密
			return;
		}
		if (!world.canDecrypted) {
			System.err.println("E:存档未加密!");
			return;
		}
		if (world.isOldEncrypt()) {
			System.err.println("E:检测到旧版存档加密，该工具无法解密" );
			return;
		}
		currntTime = System.currentTimeMillis();
		for (File file : world.files) {
			try {
				boolean isCrypted = Crypt.canDencryt(Reader.getHeader(file));
				if (isCrypted) {
					Reader.write(file, Crypt.dEncrypt(file, world.key));
					System.out.println("[解密]" + file.getName() + "成功");
				}
			} catch (IOException e) {
				System.err.println("E:" + file.getName() + e.getMessage());
			}
		}
		System.out.println("I:解密完成 耗时: " + (System.currentTimeMillis() - currntTime) + "ms");
	}
	
	public static void cryptWorld(World beWorld, World neWorld) {
		if (beWorld.canDecrypted && neWorld.canDecrypted) {
			System.err.println("E:两个存档db均为冈易存档，无需加密!");
			return;
		}
		if (beWorld.canDecrypted && !neWorld.canDecrypted) { //输入时填反了
			System.err.println("W:国际版存档与冈易版存档db不对应,已自动处理");
			cryptWorld(neWorld, beWorld);
			return;
		}
		if (beWorld.isOldEncrypt() || neWorld.isOldEncrypt()) {
			System.err.println("E:存档使用了旧版加密,无法获取秘钥!");
			return;
		}
		System.err.println("W:加密操作将覆盖国际版存档db!");
		cryptFiles(beWorld.files, neWorld.key);
		System.out.println("I:已输出至" + beWorld.db.getPath() + " 耗时:" + (System.currentTimeMillis() - currntTime) + "ms");
	}
	
	public static void cryptFiles(File[] files, byte[] key) {
		currntTime = System.currentTimeMillis();
		for (File f : files) {
			try {
				if (f.length() == 0) {
					System.out.println("[忽略]" + f.getName());
					continue;
				}
				Reader.write(f, Crypt.addHeader(Crypt.header, Crypt.crypt(f, key)));
				System.out.println("[已加密]" + f.getName());
			} catch (IOException e) {
				System.err.println("E:" + e.getMessage());
			}
		}
	}
	
	public static int scanFile(File file) {
		try {
			byte[] header = Reader.getHeader(file); //
			if (Reader.checkHeader(header, Crypt.header)) {
				System.out.println("[存档加密]" + file.getName());
				return 1;
			} 
			if (Reader.checkHeader(header, Crypt.bHeader)) {
				System.out.println("[模组加密]"+ file.getName());
				return 1;
			}
		} catch (IOException e1) {
			System.err.println("E:" + e1.getMessage());
		}
		System.err.println("[未加密]"+file.getName());
		return 0;
	}
	
	public static int scanDir(File file) {
		int count = 0;
		for (File file1 : file.listFiles()) {
			if (file1.isDirectory()) {
				count += scanDir(file1);
				continue;
			}
			count += scanFile(file1);
		}
		return count;
	}
	
	public static void scan(String path) {
		File file = new File(Utils.fixPath(path));
		if (!file.exists()) {
			System.err.println("E:路径不存在！");
			return;
		}
		System.out.println("-----------扫描文件:加密方式-------------");
		
		if (!file.isDirectory()) {
			CryptManager.scanFile(file);
		}
		
		System.out.println("I:搜索到" + CryptManager.scanDir(file) + "个文件被加密");
	}
	
	public static boolean saveKey(String dbPath, File kFile) {
		World world = new World(new File(Utils.fixPath(dbPath)));
		if (kFile.exists()) {
			System.err.println("W:操作将覆盖源秘钥！");
		}
		if (!world.loaded) {
			return false;
		}
		if (!world.canDecrypted) {
			return false;
		}
		try {
			Reader.write(kFile, world.key);
			System.out.println("I:秘钥已写出至" + kFile.getPath());
			return true;
		} catch (IOException e) {
			System.err.println("E:"+ e.getMessage());
		}
		return false;
	}
}
