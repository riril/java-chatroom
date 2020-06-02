package Utils;

import Client.ImageFrame;
import cn.hutool.core.io.FileTypeUtil;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class LinkUtils {
    public static void SaveFileWithFilename(String fileLink) {
        String content = fileLink.substring(7);
        int r = content.lastIndexOf(";");
        String filename = "";
        if (r != -1) {
            filename = content.substring(0, r);
            content = content.substring(r + 1);
        }
        byte[] fileSrc = ByteUtils.decodeBase64StringToByte(content);
        fileSrc = ByteUtils.unGZip(fileSrc);
        File file = FileUtils.fileChooser(null, new File(filename));
        if (file != null) {
            FileUtils.saveContent(fileSrc, file);
        }
    }

    public static void SaveImage(String imageLink) throws MalformedURLException, URISyntaxException {
        String imgPath = imageLink.replace("img://", "");
        byte[] fileSrc = FileUtils.readContent(Paths.get(new URL(imgPath).toURI()).toAbsolutePath().toString());
        String type = FileTypeUtil.getType(new ByteArrayInputStream(fileSrc));
        File file = FileUtils.fileChooser(null, new File("file." + type));
        if (file != null) {
            FileUtils.saveContent(fileSrc, file);
        }
    }

    public static class HyperlinkMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                // BUTTON3 == �Ҽ����
                String link = LinkUtils.getHyperlinkHref(e);
                if (link == null) return;
                if (link.startsWith("img://")) {
                    try {
                        LinkUtils.SaveImage(link);
                    } catch (MalformedURLException | URISyntaxException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                } else if (link.startsWith("file://")) {
                    LinkUtils.SaveFileWithFilename(link);
                }
            }
        }
    }

    public static void ShowImage(String imageLink) {
        String imgPath = imageLink.replace("img://", "");
        EventQueue.invokeLater(() -> {
            try {
                ImageFrame imgFrame = new ImageFrame(imgPath);
                imgFrame.setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }


    private static Element getHyperlinkElement(MouseEvent event) {
        JEditorPane editor = (JEditorPane) event.getSource();
        Position.Bias[] biasRet = new Position.Bias[1];
        int pos = editor.getUI().viewToModel2D(editor, event.getPoint(), biasRet);
        if (pos >= 0 && editor.getDocument() instanceof HTMLDocument) {
            HTMLDocument hdoc = (HTMLDocument) editor.getDocument();
            Element elem = hdoc.getCharacterElement(pos);
            if (elem.getAttributes().getAttribute(HTML.Tag.A) != null) {
                return elem;
            }
        }
        return null;
    }

    public static String getHyperlinkHref(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            Element h = getHyperlinkElement(e);
            if (h != null) {
                Object attribute = h.getAttributes().getAttribute(HTML.Tag.A);
                if (attribute instanceof AttributeSet) {
                    AttributeSet set = (AttributeSet) attribute;
                    return (String) set.getAttribute(HTML.Attribute.HREF);
                }
            }
        }
        return null;
    }

}
