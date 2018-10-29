package com.liyuu.strategy.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.liyuu.strategy.app.App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 数据存储地址
 * @author lixinquan
 *
 */
public final class StorageUtil {

	private static final String DIR_IMAGE = "images";
	private static final String DIR_LOG = "logs";

	private static StorageUtil mUtil;
	
	/**
	 * 文件存放的ROOT目录名
	 */
	public static void initialize() {
		mUtil = new StorageUtil();
	}
	
	public static void release() {
		mUtil = null;
	}
	
	public static StorageUtil getInstance() {
		return mUtil;
	}
	
	public static File getRootDir() {
		return mUtil.mRootDir;
	}
	
	public static File getImageDir() {
		return mUtil.mImageDir;
	}
	
	public static File getLogDir() {
		return mUtil.mLogDir;
	}

	/**
	 * 文件存放地址是否在外部设备上
	 * @return
	 */
	public static boolean isExternalRootDir() {
		if(mUtil.mRootDir.getAbsolutePath().startsWith("/data")) {
			return false;
		} else {
			return true;
		}
	}

	private File mRootDir;
	private final File mImageDir;
	private final File mLogDir;

	private StorageUtil() {
		if (hasSDcard()) {
			File file = App.getInstance().getExternalFilesDir("");
			if(file != null) {
				mRootDir = file.getParentFile();
			} else {
				mRootDir = App.getInstance().getFilesDir().getParentFile();
			}
        } else {
        	mRootDir = App.getInstance().getFilesDir().getParentFile();
        }
		
		mImageDir = new File(mRootDir,DIR_IMAGE);
		mLogDir = new File(mRootDir,DIR_LOG);

		mkDirs(mImageDir);
		mkDirs(mLogDir);
	}
	
	private boolean hasSDcard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
	
	private void mkDirs(File file) {
		if(file.exists() == false) {
			file.mkdirs();
		}
	}

	/**
	 *
	 * @param root 根目录
	 * @param deleteSelf 是否删除根目录
	 */
	public static void deleteChildFiles(File root, boolean deleteSelf) {
		if(root == null) return;
		File[] file = root.listFiles();
		for(File f : file) {
			if(f.isDirectory()) {
				deleteChildFiles(f, true);
			} else {
				f.delete();
			}
		}
		if(deleteSelf) {
			root.delete();
		}
	}
	
	/**
	 * 获取目标文件的大小
	 * @param root
	 * @return
	 */
	public static long getFileSize(File root) {
		if(root == null) return 0;
		File[] file = root.listFiles();
		long sum = 0;
		for(File f : file) {
			if(f.isDirectory()) {
				sum += getFileSize(f);
			} else {
				sum += f.length();
			}
		}
		return sum;
	}

	/**
	 * 从图片库的选择结果中获取图片的地址
	 * @param context
	 * @param data
	 * @return
	 */
	public static String queryImagePathFromIntentPick(Context context, Intent data) {
		if(data == null) return null;
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}

	/**
	 * 将字符串内容写入文件中
	 * @param file
	 * @param content
	 */
	public static void writeStringToFile(File file, String content) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			Logger.e(e);
		} finally {
			close(writer);
		}
	}

	/**
	 * 从文件中读取内容，并返回字符串内容
	 * @param file
	 * @return
	 */
	public static String readStringFromFile(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			return reader.readLine();
		} catch (Exception e) {
			Logger.e(e);
		} finally {
			close(reader);
		}
		return null;
	}

	public static void close(Closeable closeable) {
		if(closeable == null) return;
		try {
			closeable.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
