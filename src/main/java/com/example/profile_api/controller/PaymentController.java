
package com.example.profile_api.controller;




import com.example.profile_api.config.VNPayConfig;
import com.example.profile_api.dto.PaymentResDTO;

import com.example.profile_api.dto.TransactionStatusDTO;
import com.example.profile_api.model.Payment;
import com.example.profile_api.model.Service;
import com.example.profile_api.service.PaymentService;
import com.example.profile_api.service.ServiceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.ServiceNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    private ServiceService serviceService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public ResponseEntity<?> createPayment(@RequestParam("serviceID") Integer serviceID,HttpServletRequest request) throws UnsupportedEncodingException, ServiceNotFoundException {
        Service service = serviceService.getServiceById(serviceID)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));
        long amount = service.getBasePrice().longValue()*100;
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version",VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(request));  // Lấy địa chỉ IP từ request
        vnp_Params.put("vnp_OrderType", "other"); // Thay thế bằng loại đơn hàng phù hợp
        vnp_Params.put("vnp_ReturnUrl",VNPayConfig.vnp_ReturnUrl );

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("ok");
        paymentResDTO.setMessage("Successfully created payment");
        paymentResDTO.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
    }
    @GetMapping("/payment_infor")
    public ResponseEntity<?> transaction(
            @RequestParam(value="vnp_Amount") Double amount,
            @RequestParam(value="vnp_BankCode") String bankcode,
            @RequestParam(value="vnp_OrderInfo") String order,
            @RequestParam(value="vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_PayDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date payDate,  // Không để required = false
            @RequestParam(value = "vnp_TxnRef",required = false) String txnRef,
            @RequestParam(value="vnp_TransactionNo") String vnp_TransactionNo

        )
    {
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();

        if (paymentService.existsByVnp_TransactionNo(vnp_TransactionNo)) {
            transactionStatusDTO.setStatus("failed");
            transactionStatusDTO.setMessage("Duplicate transaction.");
            return ResponseEntity.badRequest().body(transactionStatusDTO);
        }
    if(responseCode.equals("00")){
        transactionStatusDTO.setStatus("ok");
        transactionStatusDTO.setMessage("Successfully");
        transactionStatusDTO.setData("");
    Payment payment = new Payment();
    payment.setTotalAmount(amount);
    payment.setPaymentDate(payDate);
    payment.setBankCode(bankcode);
    payment.setStatus(responseCode);
    payment.setVnp_TransactionNo(vnp_TransactionNo);
    paymentService.createPayment(payment);


    }else{
        transactionStatusDTO.setStatus("No");
        transactionStatusDTO.setMessage("Fail");
        transactionStatusDTO.setData("");
         }
    return ResponseEntity.status(HttpStatus.OK).body(transactionStatusDTO);
    }
    @GetMapping("/vnpay_return")

    public ResponseEntity<?> vnpayReturn(HttpServletRequest request) {
        // 1. Lấy các tham số trả về từ VNPAY
        Map<String, String> vnpayData = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            vnpayData.put(paramName, request.getParameter(paramName));

        }

        // 2. Kiểm tra chữ ký của VNPAY để đảm bảo tính toàn vẹn của dữ liệu
        if (vnpayData.containsKey("vnp_SecureHash")) {
            String vnp_SecureHash = vnpayData.get("vnp_SecureHash");
            vnpayData.remove("vnp_SecureHash");
            String secureHash = VNPayConfig.hashAllFields(vnpayData);
            if (secureHash.equals(vnp_SecureHash)) {
                // 3. Xử lý kết quả giao dịch
                String vnp_ResponseCode = vnpayData.get("vnp_ResponseCode"); // Mã kết quả giao dịch

                if ("00".equals(vnp_ResponseCode)) {
                    // Giao dịch thành công
                    // - Cập nhật trạng thái đơn hàng trong database
                    // - Hiển thị thông báo thành công cho người dùng
                    return ResponseEntity.ok("Giao dịch thành công!");

                } else {
                    // Giao dịch thất bại
                    // - Xử lý lỗi và hiển thị thông báo cho người dùng
                    return ResponseEntity.badRequest().body("Giao dịch thất bại: " + vnpayData.get("vnp_Message"));
                }
            } else {
                // Chữ ký không hợp lệ
                return ResponseEntity.badRequest().body("Chữ ký không hợp lệ!");
            }
        } else {
            return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ!");
        }
    }
}
