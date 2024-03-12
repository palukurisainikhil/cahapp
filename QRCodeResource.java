package com.example.scan2cash.controllers;

import com.example.scan2cash.model.QRCodeRequestBody;
import com.example.scan2cash.model.QRCodeResponseBody;
import com.example.scan2cash.model.ValidateQRCodeRequestBody;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.StringUtils;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
public class QRCodeResource {
    @PostMapping(value = "/generateQRcode")
    public ResponseEntity<Object> generateQRCode(@RequestBody QRCodeRequestBody request) {
        String textToEncode = "id: " + request.getToken() + ", " + "mobileNumber: " + request.getMobileNumber() + ", " + "fundingAccount:" + request.getFundingAccount() + ", " + "amount" + request.getAmount() + ", " + "ATMKioskId" + request.getATMKioskId();
        int width = 200;
        int height = 200;
        QRCodeResponseBody qrCodeResponseBody = new QRCodeResponseBody();

        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.MARGIN, 5);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(textToEncode, BarcodeFormat.QR_CODE, width, height, hintMap);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] qrCodeByteArray = outputStream.toByteArray();

            qrCodeResponseBody.setQrCodeString(Base64.getEncoder().encodeToString(qrCodeByteArray));
//            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeByteArray);
            return new ResponseEntity<>(qrCodeResponseBody,HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping(value = "/validateQRCode")
    public ResponseEntity<Object> validateQRCode(@RequestBody ValidateQRCodeRequestBody validateQRCodeRequestBody){
        try {
            if(Objects.nonNull(validateQRCodeRequestBody.getBase64Decodedvalue()) && !"".equals(validateQRCodeRequestBody.getBase64Decodedvalue())){
                byte[] qrCodeData = Base64.getDecoder().decode(validateQRCodeRequestBody.getBase64Decodedvalue());
                ByteArrayInputStream bis = new ByteArrayInputStream(qrCodeData);
                BufferedImage bufferedImage = ImageIO.read(bis);

                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
                Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
                return ResponseEntity.ok(qrCodeResult.getText());
            }
            else{
                return ResponseEntity.badRequest().build();
            }
        } catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
