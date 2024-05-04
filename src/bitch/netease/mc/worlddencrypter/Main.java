package bitch.netease.mc.worlddencrypter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	/*
	 * 冈易，你坏事做尽！！！
	 */
	public static Scanner sc = new Scanner(System.in);
	private static File CURRENT_f, MANIFEST_f;
	private static byte[] key;
	private static File bd;
	private static File[] rootFiles;

	public static void main(String[] args) {
		Utils.printStrings(args);
		if (args.length == 0) {
			System.out.print("请在此拖入存档的bd文件夹\n->");
			bd = new File(sc.nextLine());
		} else {
			bd = new File(args[0]);
		}
		setUp();
		long s = System.currentTimeMillis();
		for (File file : rootFiles) {
			try {
				boolean isEncrypted = Utils.checkEncryt(Utils.getHeader(file));
				System.out.println("I:" + file.getName() + "加密状态:		" + isEncrypted);
				if (isEncrypted) {
					Utils.write(file, Utils.dEncrypt(file, key));
					System.out.println("I:" + file.getName() + "解密成功");
				}
			} catch (IOException e) {
				System.err.println("E:" + file.getName() + e.getMessage());
			}
		}
		System.out.println("I:解密成功！  耗时: " + (System.currentTimeMillis() - s) + "ms");
		Utils.exit();
	}

	public static void setUp() {
		if (!bd.exists() || !bd.isDirectory()) {
			System.err.println("E:bd路径无效。");
			Utils.exit();
		}
		rootFiles = bd.listFiles();

		if (rootFiles != null) {
			for (File file : rootFiles) {
				if (file.isFile() && file.getName().startsWith("MANIFEST")) {
					MANIFEST_f = file;
					System.out.println("I:读取到清单" + MANIFEST_f.getName());
				}
				if (file.isFile() && file.getName().equals("CURRENT")) {
					CURRENT_f = file;
					System.out.println("I:读取到清单" + CURRENT_f.getName());
				}
			}
			if (CURRENT_f == null || MANIFEST_f == null) {
				System.out.println("E:未读取到清单文件！");
				Utils.exit();
			}
			System.out.println("I:读取清单成功！");
		} else {
			System.err.println("E:bd文件夹损坏！");
			Utils.exit();
		}

		try {
			if (!Utils.checkEncryt(Utils.getHeader(CURRENT_f))) {
				System.out.println("E:存档未加密或不支持的加密算法！");
				Utils.exit();
			}
			key = Utils.getKey(CURRENT_f, MANIFEST_f.getName());
			System.out.println("I:获取到秘钥:");
			Utils.printBytes(key);
		} catch (IOException e) {
			System.err.println("E:" + e.getMessage());
		}
	}
}
