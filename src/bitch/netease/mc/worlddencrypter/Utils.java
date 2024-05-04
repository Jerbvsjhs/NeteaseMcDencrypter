package bitch.netease.mc.worlddencrypter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

public class Utils {
	public static byte[] header = { (byte) 0x80, 0x1D, 0x30, 0x1 };

	public static InputStream readFile(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public static byte[] readWithoutHeader(File file) throws IOException {
		byte[] header_f = new byte[4];
		InputStream inputStream = Utils.readFile(file);
		byte[] buffer = null;
		if (inputStream.read(header_f) != -1) {
			if (isHeader(header_f, header)) {
				buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				return buffer;
			} else {
				System.out.println("E:文件头不匹配。");
				Utils.printBytes(header_f);
			}
		} else {
			System.out.println("E:文件头过短。");
		}
		return buffer;
	}

	public static byte[] getHeader(File file) throws IOException {
		InputStream inputStream = Utils.readFile(file);
		byte[] header = new byte[4];
		if (inputStream.read(header) == -1) {
			System.out.println("W:无法读取文件" + file.getName() + "的头。");
		}
		return header;
	}

	public static boolean isHeader(byte[] b, byte[] header) {
		boolean r = true;
		if (b.length == header.length) {
			for (int i = 0; i < b.length; i++) {
				if (b[i] != header[i]) {
					r = false;
					break;
				}
			}
		} else {
			r = false;
		}
		return r;
	}

	public static boolean checkEncryt(byte[] h) {
		return Utils.isHeader(h, header);
	}

	public static byte[] dEncrypt(File file, byte[] key) throws IOException {
		byte[] b = Utils.readWithoutHeader(file);
		return Utils.xor(b, key);
	}

	public static byte[] getKey(File currentFile, String manifestName) throws IOException {
		byte[] source = Utils.addByte(manifestName.getBytes(), (byte) 0x0A);
		byte[] bin = Utils.readWithoutHeader(currentFile);
		return Utils.xor(bin, source);
	}

	public static byte[] xor(byte[] p, byte[] k) {
		byte[] data = new byte[p.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) (p[i] ^ k[i % k.length]);
		}
		return data;
	}

	public static void printBytes(byte[] b) {
		System.out.println("-------------------------");
		for (byte c : b) {
			String bin = String.format("%8s", c);
			String hex = String.format("0x%X", c);
			System.out.println(c + "	" + bin.replace(' ', '0') + "	" + hex);
		}
		System.out.println("长度:" + "	" + b.length);
		System.out.println("-------------------------");
	}

	public static void printStrings(String[] s) {
		System.out.println("-------------------------");
		for (String string : s) {
			System.out.println(string);
		}
		System.out.println("长度:" + "	" + s.length);
		System.out.println("-------------------------");
	}

	public static byte[] addByte(byte[] old, byte b) {
		byte[] r = new byte[old.length + 1];
		for (int i = 0; i < old.length; i++) {
			r[i] = old[i];
		}
		r[old.length] = b;
		return r;
	}

	public static byte[] keyOptimize(byte[] key) {
		Set<Byte> set = new LinkedHashSet<>();
		for (byte b : key) {
			set.add(b);
		}
		byte[] r = new byte[set.size()];
		int i = 0;
		for (byte b1 : set) {
			r[i] = b1;
			i += 1;
		}
		return r;
	}

	public static void write(File file, byte[] buffer) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(buffer);
		fileOutputStream.close();
	}

	public static void exit() {
		System.out.println("请按任意键结束. . .");
		Main.sc.nextLine();
		System.exit(0);
	}
}
