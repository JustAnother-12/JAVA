package utils;

public class FormatDate {
    public static String convertDateFormat(String inputDate) {
        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return inputDate; // Nếu xảy ra lỗi, trả về định dạng ban đầu.
        }
    }
}
