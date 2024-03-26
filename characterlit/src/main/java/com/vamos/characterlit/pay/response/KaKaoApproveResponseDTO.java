package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class KaKaoApproveResponseDTO {

    private String aid;
    private String tid;
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private String item_name;
    private String item_code;
    private int quantity;
    private Date created_at;
    private Date approved_at;
}

class Amount{
    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;
    private int green_deposit;
}
