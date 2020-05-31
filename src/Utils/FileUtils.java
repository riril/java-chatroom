package Utils;

import Client.Config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileUtils {
    public static File fileChooser(FileNameExtensionFilter filter, File file) {
        JFileChooser fc = new JFileChooser();
        // ���ò������ѡ
        fc.setMultiSelectionEnabled(false);
        if (filter != null) fc.setFileFilter(filter);
        if (file != null) fc.setSelectedFile(file);
        int result = fc.showSaveDialog(null);

        // JFileChooser.APPROVE_OPTION�Ǹ����ͳ���������0������˵������0��ֵ���ǲ�ִ����ز���������ʲôҲ������
        if (result == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile(); // ����·�� file.getAbsolutePath();
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


    public static String getExtension(String filename) {
        if (filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return null;
    }

    public static Path getTempDirectory() {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir") + "/chatroom/" + Config.getInstance().getUserName() + "/");
        System.out.println(tempDir.toString());
        File file = new File(tempDir.toString());
        if (!file.exists()) {
            if (!file.mkdirs()) return null;
        }
        return tempDir;
    }
}
