package com.hktorab;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller {
    public TextArea txtArea;
    public TextField height;
    public TextField width;

    public void generateQR(ActionEvent actionEvent) {
        try {
            LocalDateTime l = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
            String name = "./" + l.format(myFormatObj) + ".png";
            generateQRCodeImage(txtArea.getText(), Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), name);
            System.out.println("Finish");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateQRCodeImage(String text, int width, int height, String filePath) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            Path path = FileSystems.getDefault().getPath(filePath);

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("HurraH !!!");
            alert.setHeaderText(null);
            alert.setContentText("You've successfully created the QR!");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !!");
            alert.setHeaderText("QR failed to create.");
            alert.setContentText(null);
            alert.showAndWait();
        }

    }


    public void profile(MouseEvent mouseEvent) {
        try {
            String url = "https://www.hktorab.com";
            String os = System.getProperty("os.name").toLowerCase();
            Runtime rt = Runtime.getRuntime();
            if (os.indexOf("win") >= 0) {

                // this doesn't support showing urls in the form of "page.html#nameLink"
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

            } else if (os.indexOf("mac") >= 0) {

                rt.exec("open " + url);

            } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};

                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");

                rt.exec(new String[]{"sh", "-c", cmd.toString()});

            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
