package Utils;

import Client.Config;
import Model.Message;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {
    public static File fileChooser(FileNameExtensionFilter filter, File file) {
        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());
        // 设置不允许多选
        fc.setMultiSelectionEnabled(false);
        if (filter != null) fc.setFileFilter(filter);
        if (file != null) fc.setSelectedFile(file);
        int result = fc.showSaveDialog(null);

        // JFileChooser.APPROVE_OPTION是个整型常量，代表0。就是说当返回0的值我们才执行相关操作，否则什么也不做。
        if (result == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    public static byte[] readContent(String filePath) {
        File file = new File(filePath);
        byte[] data = null;
        try {
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void saveContent(byte[] bytes, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File chooseFile() {
        return fileChooser(null, null);
    }

    public static File chooseImage() {
        return fileChooser(new FileNameExtensionFilter("图片", "jpg", "jpeg", "png", "gif"), null);

    }

    public static String getGZippedFileB64(File file) {
        String filePath = file.getAbsolutePath();
        byte[] data = FileUtils.readContent(filePath);
        if (data != null) {
            data = ByteUtils.GZip(data);
            return ByteUtils.encodeByteToBase64String(data);
        }
        return null;
    }

    public static String getExtension(String filename) {
        int i = filename.lastIndexOf('.');

        if (i > 0 &&  i < filename.length() - 1) {
            return filename.substring(i+1).toLowerCase();
        }
        return null;
    }

    public static Path getTempDirectory() {
        String dir = String.valueOf(Config.getInstance().getUserName().hashCode()).toUpperCase();
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir") + "/chatroom/" + dir + "/");
        File file = new File(tempDir.toString());
        if (!file.exists()) {
            if (!file.mkdirs()) return null;
        }
        return tempDir;
    }
}
