package bitch.netease.mc.worlddencrypter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	/*
	 * ���ף��㻵������������
	 */
	public static Scanner sc = new Scanner(System.in);
	private static File CURRENT_f, MANIFEST_f;
	private static byte[] key;
	private static File bd;
	private static File[] rootFiles;

	public static void main(String[] args) {
		Utils.printStrings(args);
		if (args.length == 0) {
			System.out.print("���ڴ�����浵��bd�ļ���\n->");
			bd = new File(sc.nextLine());
		} else {
			bd = new File(args[0]);
		}
		setUp();
		long s = System.currentTimeMillis();
		for (File file : rootFiles) {
			try {
				boolean isEncrypted = Utils.checkEncryt(Utils.getHeader(file));
				System.out.println("I:" + file.getName() + "����״̬:		" + isEncrypted);
				if (isEncrypted) {
					Utils.write(file, Utils.dEncrypt(file, key));
					System.out.println("I:" + file.getName() + "���ܳɹ�");
				}
			} catch (IOException e) {
				System.err.println("E:" + file.getName() + e.getMessage());
			}
		}
		System.out.println("I:���ܳɹ���  ��ʱ: " + (System.currentTimeMillis() - s) + "ms");
		Utils.exit();
	}

	public static void setUp() {
		if (!bd.exists() || !bd.isDirectory()) {
			System.err.println("E:bd·����Ч��");
			Utils.exit();
		}
		rootFiles = bd.listFiles();

		if (rootFiles != null) {
			for (File file : rootFiles) {
				if (file.isFile() && file.getName().startsWith("MANIFEST")) {
					MANIFEST_f = file;
					System.out.println("I:��ȡ���嵥" + MANIFEST_f.getName());
				}
				if (file.isFile() && file.getName().equals("CURRENT")) {
					CURRENT_f = file;
					System.out.println("I:��ȡ���嵥" + CURRENT_f.getName());
				}
			}
			if (CURRENT_f == null || MANIFEST_f == null) {
				System.out.println("E:δ��ȡ���嵥�ļ���");
				Utils.exit();
			}
			System.out.println("I:��ȡ�嵥�ɹ���");
		} else {
			System.err.println("E:bd�ļ����𻵣�");
			Utils.exit();
		}

		try {
			if (!Utils.checkEncryt(Utils.getHeader(CURRENT_f))) {
				System.out.println("E:�浵δ���ܻ�֧�ֵļ����㷨��");
				Utils.exit();
			}
			key = Utils.getKey(CURRENT_f, MANIFEST_f.getName());
			System.out.println("I:��ȡ����Կ:");
			Utils.printBytes(key);
		} catch (IOException e) {
			System.err.println("E:" + e.getMessage());
		}
	}
}
