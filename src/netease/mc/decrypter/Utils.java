package netease.mc.decrypter;

public class Utils {
	/*
	 * 修复win11下拖拽文件自动加上引号的问题
	 */
	public static String fixPath(String dbPath) {
		if(dbPath.startsWith("\"") && dbPath.endsWith("\"")) {
			dbPath = dbPath.replace("\"", "");
			System.err.println("W:db路径请避免带有引号！");
			return dbPath;
		}
		return dbPath;
	}
	
	public static void printLogo(char c) {
		StringBuilder sb = buildLogo(new StringBuilder(), c);
		byte[] bs = {0x4b, 0x22, 0x3, 0x10, 0x3, 0x0a, 0x9, 0x16, 0x3, 0x2, 0x46, 0x4, 0x1f, 0x46, 0x0c, 0x3, 0x14, 0x4, 0x10, 0x15, 0x0c, 0x0e, 0x15};
		byte[] bs1 = new byte[90];
		for (int i = 0; i < 90; i++) {
			if (i < 67) {
				bs1[i] = (byte)0x20;
				continue;
			}
			bs1[i] = (byte) (bs[i-67]^0x66&0xff);
		}
		System.out.print(sb.append("\n"+ new String(bs1) + "\n").toString());
	}
	
	public static StringBuilder buildLogo(StringBuilder sb, char c) {
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			sb = genAscii(sb, c, (byte)0x81, (byte)0x1, (byte)0x81, (byte)0x80, (byte)0x7c);
			sb = genAscii(sb, c, (byte)0xc1, (byte)0x1, (byte)0x42, (byte)0x80, (byte)0x41);
			sb = genAscii(sb, c, (byte)0xa1, (byte)0x1, (byte)0x24, (byte)0x80, (byte)0x40, (byte)0x80, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x10);
			sb = genAscii(sb, c, (byte)0x91, (byte)0x1, (byte)0x18, (byte)0x80, (byte)0x40, (byte)0x80, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x38);
			sb = genAscii(sb, c, (byte)0x89, (byte)0x39, (byte)0x0, (byte)0x9c, (byte)0x40, (byte)0x9c, (byte)0x70, (byte)0xe4, (byte)0x4e, (byte)0x10, (byte)0xe1, (byte)0xc0);
			sb = genAscii(sb, c, (byte)0x85, (byte)0x7d, (byte)0x0, (byte)0xa0, (byte)0x40, (byte)0xbe, (byte)0x81, (byte)0x4, (byte)0x51, (byte)0x11, (byte)0xf2);
			sb = genAscii(sb, c, (byte)0x83, (byte)0x41, (byte)0x0, (byte)0xa0, (byte)0x41, (byte)0x20, (byte)0x81, (byte)0x4, (byte)0x51, (byte)0x11, (byte)0x2);
			sb = genAscii(sb, c, (byte)0x81, (byte)0x39, (byte)0x0, (byte)0x9c, (byte)0x7c, (byte)0x1c, (byte)0x71, (byte)0x3, (byte)0xde, (byte)0x0c, (byte)0xe2);
			sb = genAscii(sb, c, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x50);
			sb = genAscii(sb, c, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x3, (byte)0x90);
			return sb;
		}
		sb = genAscii(sb, c, (byte)0xe0, (byte)0xa0, (byte)0xc0, (byte)0x0, (byte)0x0, (byte)0x40);
		sb = genAscii(sb, c, (byte)0xa4, (byte)0xe6, (byte)0xa4, (byte)0x66, (byte)0xac, (byte)0xe4, (byte)0x60);
		sb = genAscii(sb, c, (byte)0xae, (byte)0xe8, (byte)0xae, (byte)0x88, (byte)0xea, (byte)0x4e, (byte)0x80);
		sb = genAscii(sb, c, (byte)0xa6, (byte)0xa6, (byte)0xa6, (byte)0x68, (byte)0x2c, (byte)0x66, (byte)0x80);
		sb = genAscii(sb, c, (byte)0xa0, (byte)0xa0, (byte)0xc0, (byte)0x0, (byte)0xc8);
		return sb;
	}
	
	public static StringBuilder genAscii(StringBuilder sb, char a, byte ...bs) {
		for (byte b : bs) {
			for (int i = 7; i >= 0; i--) {
				if (((b >> i) & 1) == 1) {
					sb.append(a);
					continue;
				}
				sb.append(' ');
			}
		}
		sb.append('\n');
		return sb;
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
		System.out.println("-------------------------");
		System.out.println("长度:" + "	" + s.length);
		System.out.println("-------------------------");
	}
	
	public static String scanLine() {
		/*
		 * 清除可能残留的输入
		 */
		if (Main.sc.hasNextLine()) Main.sc.nextLine(); 
		return Main.sc.nextLine();
	}
	
	public static int safeScanInt(int normal) {
		try {
			return Main.sc.nextInt();
		} catch (Exception e) {
			Main.sc.nextLine();//清空错误输出
		}
		return normal;
	}
}
