package com.realidtek.rfid.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.text.TextUtils;

public class FileHelper {
	public static final int KB = 1024;
	public static final int MB = KB * KB;
	public static final int GB = KB * MB;
	public static final int TB = KB * GB;

	public static final String GBK = "GBK";
	public static final String UTF8 = "UTF-8";
	public static String charset = GBK;

	public static boolean exist(String pathAndFileName) {
		return new File(pathAndFileName).exists();
	}

	public static boolean notExist(String pathAndFileName) {
		return !exist(pathAndFileName);
	}

	public static boolean create(String pathAndFileName) {
		boolean re = false;
		try {
			File file = new File(pathAndFileName);
			if (!file.exists() && haveFreeSpace(file.getParent())) {
				re = file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return re;
	}

	public static boolean delete(String pathAndFileName) {
		boolean re = false;
		re = new File(pathAndFileName).delete();
		return re;
	}

	public static boolean backup(String sourcePathAndFileName,
			String destinationPathAndFileName) {
		return copy(sourcePathAndFileName, destinationPathAndFileName);
	}

	public static boolean recover(String sourcePathAndFileName,
			String destinationPathAndFileName) {
		return copy(sourcePathAndFileName, destinationPathAndFileName);
	}

	public static boolean copy(String sourcePathAndFileName,
			String destinationPathAndFileName) {
		boolean re = false;

		try {
			if (!TextUtils.isEmpty(sourcePathAndFileName)
					&& !TextUtils.isEmpty(destinationPathAndFileName)) {

				FileInputStream in = new FileInputStream(sourcePathAndFileName);
				boolean toCopy = true;
				if (notExist(destinationPathAndFileName)) {
					toCopy = create(destinationPathAndFileName);
				}
				if (toCopy) {
					FileOutputStream out = new FileOutputStream(
							destinationPathAndFileName);
					byte[] bt = new byte[1024];
					int count;
					while ((count = in.read(bt)) > 0) {
						out.write(bt, 0, count);
					}
					in.close();
					out.close();
					re = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return re;
	}

	public static boolean move(String sourcePathAndFileName,
			String destinationPathAndFileName) {
		boolean re = false;

		if (copy(sourcePathAndFileName, destinationPathAndFileName)) {
			re = delete(sourcePathAndFileName);
		}

		return re;
	}

	public static boolean rename(String sourcePathAndFileName,
			String destinationPathAndFileName) {
		return move(sourcePathAndFileName, destinationPathAndFileName);
	}

	public static File getFile(String pathAndFileName) {
		File file = new File(pathAndFileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static boolean haveFreeSpace(String pathAndFileName) {
		return getFreeSpace(pathAndFileName) > 0;
	}

	public static boolean haveFreeSpace(File file) {
		return getFreeSpace(file) > 0;
	}

	public static long getFreeSpace(File file) {
		return getFreeSpace(file.getParent(), charset) * MB;
	}

	private static long getFreeSpace(String path, String charset) {
		long re = -1;
		try {
			if (System.getProperty("os.name").startsWith("Linux")) {
				re = getFreeSpaceOnLinux(path);
			}
			if (re == -1) {
				throw new UnsupportedOperationException(
						"The method getFreeSpace(String path) has not been implemented for this operating system.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	private static long getFreeSpaceOnLinux(String path) throws Exception {
		long bytesFree = -1;

		Process p = Runtime.getRuntime().exec("df " + path);
		InputStream reader = new BufferedInputStream(p.getInputStream());
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = reader.read();
			if (c == -1)
				break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		reader.close();

		// parse the output text for the bytes free info
		StringTokenizer tokenizer = new StringTokenizer(outputText, "\n");
		tokenizer.nextToken();
		if (tokenizer.hasMoreTokens()) {
			String line2 = tokenizer.nextToken();
			StringTokenizer tokenizer2 = new StringTokenizer(line2, " ");
			if (tokenizer2.countTokens() >= 4) {
				String tmp = tokenizer2.nextToken();
				tmp = tokenizer2.nextToken();
				tmp = tokenizer2.nextToken();
				tmp = tokenizer2.nextToken();
				if (!TextUtils.isEmpty(tmp)) {
					bytesFree = Long.parseLong(tmp.trim().substring(0,
							tmp.length() - 1));
					return bytesFree;
				}
			}

			return bytesFree;
		}

		throw new Exception("Can not read the free space of " + path + " path");
	}

	public static long getFreeSpace(String pathAndFileName) {
		return getFreeSpace(new File(pathAndFileName));
	}

	public static long getFileSizes(File f) {
		long s = 0;
		FileInputStream fis = null;
		try {
			if (f.exists()) {
				fis = new FileInputStream(f);
				s = fis.available();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	public static String read(String pathAndFileName) {
		String re = "";

		File file = new File(pathAndFileName);
		if (file.exists()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));
				String temp = null;
				StringBuffer sb = new StringBuffer();
				temp = br.readLine();
				while (temp != null) {
					sb.append(temp + "\n");
					temp = br.readLine();
				}
				re = sb.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return re;
	}

	public static boolean write(String pathAndFileName, String msg) {
		return append(pathAndFileName, msg);
	}

	public static boolean append(String pathAndFileName, String msg) {
		return write(pathAndFileName, msg, true);
	}

	public static boolean cover(String pathAndFileName, String msg) {
		return write(pathAndFileName, msg, false);
	}

	public static boolean write(String pathAndFileName, String msg,
			boolean isAppened) {
		boolean re = false;

		if (haveFreeSpace(pathAndFileName)) {
			File file = new File(pathAndFileName);
			BufferedWriter bw = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				if (file.exists()) {
					bw = new BufferedWriter(new FileWriter(file, isAppened));
					bw.write(msg);
					re = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return re;
	}

	public Byte[] readFile(String fileName) {
		File f = new File(fileName);
		InputStream file;
		try {
			List<Byte> bufferList = new ArrayList<Byte>();
			file = new FileInputStream(f);
			int length = 0;
			byte[] buffer = new byte[1024];

			while ((length = file.read(buffer)) > 0) {
				for (int i = 0; i < length; i++) {
					bufferList.add(Byte.valueOf(buffer[i]));
				}
			}

			Byte[] bufArray = new Byte[bufferList.size()];
			return bufferList.toArray(bufArray);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}