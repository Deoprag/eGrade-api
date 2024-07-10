package com.deopraglabs.egradeapi.util;

import com.deopraglabs.egradeapi.model.Role;
import com.deopraglabs.egradeapi.repository.CoordinatorRepository;
import com.deopraglabs.egradeapi.repository.ProfessorRepository;
import com.deopraglabs.egradeapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class EGradeUtils {

    @Autowired
    ProfessorRepository teacherRepo;

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    CoordinatorRepository coordinatorRepo;

    public static String formatCpf(String oldCpf) {
        return String.format(oldCpf, oldCpf.substring(0, 2) + "." + oldCpf.substring(3, 5) + "." + oldCpf.substring(6, 8) + "-" + oldCpf.charAt(9));
    }

    public Role getRole(String cpf) {
        try {
            if (coordinatorRepo.findByCpf(cpf) != null) {
                return Role.COORDENADOR;
            } else if (teacherRepo.findByCpf(cpf) != null) {
                return Role.PROFESSOR;
            } else if (studentRepo.findByCpf(cpf) != null) {
                return Role.ALUNO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Role.NONE;
    }

    public static String hashPassword(String password) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro ao gerar hash da senha: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        final String hashedPlainPassword = hashPassword(plainPassword);
        return hashedPlainPassword.equals(hashedPassword);
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String generateCode() {
        final StringBuilder code = new StringBuilder();
        int i = 0;
        while (i < 4) {
            Random rand = new Random();
            code.append(rand.nextInt(9));
            i++;
        }
        return code.toString();
    }

    public static Blob convertToBlob(FileInputStream imageFis) throws SQLException {
        try {
            byte[] bytes = new byte[imageFis.available()];
            imageFis.read(bytes);
            final Blob image = new SerialBlob(bytes);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileInputStream convertFromBlob(Blob image) throws Exception {
        final File tempFile = File.createTempFile("tempfile", null);
        try (InputStream inputStream = image.getBinaryStream();
            final FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return new FileInputStream(tempFile);
        } catch (Exception e) {
            tempFile.delete();
            throw e;
        }
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }

    public static Date stringToDate(String stringDate) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(stringDate);
    }

    public static Date stringToDate2(String stringDate) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.parse(stringDate);
    }

    public static LocalDateTime stringToLocalDateTime(String stringLocalDateTime) throws ParseException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        final LocalDateTime localDateTime = LocalDateTime.parse(stringLocalDateTime, formatter);
        return localDateTime;
    }

    public static LocalTime stringToLocalTime(String stringLocalTime) throws ParseException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        final LocalTime localTime = LocalTime.parse(stringLocalTime, formatter);
        return localTime;
    }

    public static boolean isCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("^(\\d)\\1*$")) {
            return false;
        }

        int[] pesosDigito1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosDigito2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * pesosDigito1[i];
        }

        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : (11 - resto);

        soma = 0;
        for (int i = 0; i < 10; i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * pesosDigito2[i];
        }

        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : (11 - resto);

        return (digito1 == Character.getNumericValue(cpf.charAt(9)) && digito2 == Character.getNumericValue(cpf.charAt(10)));
    }

    public static FileInputStream findFile(String path) throws FileNotFoundException {
        return new FileInputStream(new File(path));
    }

    public static byte[] getProfileImage() {
        try {
            final FileInputStream imageFis = findFile("src/main/resources/img/user.png");
            return imageFis.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
