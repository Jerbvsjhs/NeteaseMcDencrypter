package netease.mc.decrypter;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
	/*
	 * 静态编译将包含utils.exit
	 */
	public static Scanner sc = new Scanner(System.in);
	public static Random random = new Random();
	public static int runType;
	public static String workingDir = System.getProperty("user.dir");
	
	static char[] tChars = {'@', '*', 'o', '.', '+', '\"'};
	public static void main(String[] args) {
		if (args.length != 0) {
			ArgsParser.parse(args);
			return;
		}
		
		Utils.printLogo(tChars[random.nextInt(tChars.length)]);
		System.out.println("0.网易地图解密 \n1.地图导入网易 \n2.查看文件/文件夹加密状态 \n3.获取存档秘钥");
		System.out.print("请选择运行方式0/1/2:");
		if ((runType = Utils.safeScanInt(-1)) == -1) {
			System.err.println("E:只能输入数字！");
			return;
		}
		
		String dbPath;
		switch(runType) {
		case 0: //存档解密
			System.out.print("请将db文件夹拖至此处->");
			dbPath = Utils.scanLine();
			preDecrypt(dbPath);
			return;
		case 1: //存档加密
			System.out.print("-------------------------\n0.从秘钥加密 \n1.从存档加密\n请输入加密方式:");
			if ((runType = Utils.safeScanInt(-1)) == -1) {
				System.err.println("E:只能输入数字！");
				return;
			}
			
			if (runType == 0) { //秘钥加密
				File kFile = new File(workingDir + "\\key.bin");
				if (!kFile.exists() || kFile.isDirectory()) {
					System.err.println("E:秘钥不存在,请先获取秘钥!");
					return;
				}
				System.out.print("请将国际版存档db文件夹拖至此处->");
				String bePath = Utils.scanLine();
				preCrypt(bePath, kFile);
				return;
			}
			if (runType == 1) { 
				System.out.print("请将国际版存档db文件夹拖至此处->");
				String bePath = Utils.scanLine();
				System.out.print("请将网易版存档db文件夹拖至此处->");
				String nePath = sc.nextLine();
				preCrypt(bePath, nePath);
				return;
			}
			System.err.println("E:没有方式" + runType);
			return;
		case 2: //搜索
			System.out.print("请将文件/文件夹拖至此处->");
			dbPath = Utils.scanLine();
			CryptManager.scan(dbPath);
			return;
		case 3: //保存秘钥
			System.out.print("请将db文件夹拖至此处->");
			dbPath = Utils.scanLine();
			File kFile = new File(workingDir, "key.bin");
			if (CryptManager.saveKey(dbPath, kFile)) {
				System.err.println("E:保存秘钥失败");
			}
			return;
		default :
			System.err.println("E:无效的运行方式 " + runType);
		}
	}
	
	public static void preDecrypt(String dbPath) { //分离处理以便复用
		CryptManager.decryptWorld(new World(new File(Utils.fixPath(dbPath))));
	}
	
	public static void preCrypt(String bePath, String nePath) {
		if (bePath.equals(nePath)) {
			System.err.println("E:存档db不能相同！");
			return;
		}
		World beWorld = new World(new File(Utils.fixPath(bePath)));
		World neWorld = new World(new File(Utils.fixPath(nePath)));
		if (!beWorld.loaded) {
			return;
		}
		if (!neWorld.loaded) { //损坏的存档
			return;
		}
		CryptManager.cryptWorld(beWorld, neWorld);
	}
	
	public static void preCrypt(String bePath, File kFile) {
		World beWorld = new World(new File(Utils.fixPath(bePath)));
		if (!beWorld.loaded) {
			return;
		}
		if (beWorld.canDecrypted) {
			System.out.println("I:存档无需加密");
			return;
		}
		try {
			CryptManager.cryptFiles(beWorld.files, Reader.readFileB(kFile));
			System.out.println("I:已写出至" + beWorld.db.getPath());
		} catch (IOException e) {
			System.err.println("E:" + e.getMessage());
		}
		
	}
	
}
