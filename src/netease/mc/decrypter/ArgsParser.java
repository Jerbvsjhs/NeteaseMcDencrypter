package netease.mc.decrypter;

import java.io.File;

public class ArgsParser {
	static String decBd, encBd, gBd;
	static String keyPath;
	
	static boolean flagDEC = false;
    static boolean flagENC = false;
    static boolean flagKEY = false;
    static boolean flagGKEY = false;
    
	public static void parse(String[] args) {
		for (String string : args) {
			String[] s = string.split(":", 2);
			switch(s[0].toUpperCase()) {
			case "-DEC":
				if (s.length != 2) {
					System.err.println("-dec参数过短");
					return;
				}
				decBd = s[1];
				flagDEC = true;
				break;
			case "-ENC":
				if (s.length != 2) {
					System.err.println("-enc参数过短");
					return;
				}
				encBd = s[1];
				flagENC = true;
				break;
			case "-KEY":
				if (s.length == 1) {
					keyPath = Main.workingDir + "/key.bin";
					flagKEY = true;
				}
				if(s.length == 2) {
					keyPath = s[1];
					flagKEY = true;
				}
				break;
			case "-GKEY":
				if (s.length != 2) {
					System.err.println("-gkey参数过短");
					return;
				}
				gBd = s[1];
				flagGKEY = true;
				break;
			case "-SC":
				if (s.length != 2) {
					System.err.println("-sc参数过短");
					return;
				}
				CryptManager.scan(s[1]);
				break;
			case "-LOGO":
				Utils.printLogo(Main.tChars[Main.random.nextInt(Main.tChars.length)]);
				break;
			default:
				System.err.println("E:无效参数");
				return;
			}
		}
		
		if (flagGKEY) {
			if (!flagKEY) {
				System.err.println("-key参数未设置错误");
				return;
			}
			CryptManager.saveKey(gBd, new File(keyPath));
		}
		
		if (flagDEC && !flagENC) {
			Main.preDecrypt(decBd);
			return;
		}
		if (flagENC) {
			if (flagDEC && flagKEY) {
				System.err.println("E:-enc参数错误");
				return;
			}
			
			if (flagDEC) {
				Main.preCrypt(encBd, decBd);
				return;
			}
			
			if (flagKEY) {
				Main.preCrypt(encBd, new File(keyPath));
				return;
			}
			System.err.println("W:-enc参数什么都没做!");
		}

	}
}
