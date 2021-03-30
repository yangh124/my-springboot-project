package com.yh.common.enums.exception;

import lombok.Getter;


public enum ResponseCodeEnum {
    // 成功
    SUCCESS(0, "操作成功"),
    SYSTEM_EXCEPTION(1, "系统异常"),
    REQUEST_NOT_IN_TOKEN(2, "没有token!"),

    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),

    //活动错误
    ACTIVE_NOT_EXIST(100100,"该活动不存在！"),
    ACTIVE_USER_EXIST(100101,"该用户已加入该活动！"),
    ACTIVE_JOIN_FOR_FAILURE(100102,"加入活动失败！"),
    ACTIVE_IN_GOODS_UNEXIST(100103,"该商品不在活动内！"),
    ACTIVE_NOT_HAVE_PLATFORM(100104,"该平台不在分组内！"),
    ACTIVE_NOT_TO_NULL(100105,"您没有参加任何活动！"),
    ACTIVE_LOVE_NOT_ENOUGH(100106,"您的爱心数不够！"),
    ACTIVE_TO_NULL(100107,"没有活动发布哦！"),

    //互助错误
    HELP_NOT_EXIST(100200,"该互助不存在！"),
    HELP_USER_EXIST(100201,"该用户已加入该互助！"),
    HELP_JOIN_FOR_FAILURE(100202,"加入互助失败！"),
//    HELP_PLATFORM_EXIST(100103,"该平台已存在在分组内！"),
//    HELP_NOT_HAVE_PLATFORM(100104,"该平台不在分组内！"),


    //商品错误
    GOODS_NOT_EXIST(100300,"该商品不存在！"),
    GOODS_NOT_EXIST_OF_LIST(100301,"没有商品发布哦！"),
    GOODS_DONT_DELETE(100302,"该宝贝不能减少了哦！"),
    GOODS_DONT_ADD(100303,"该宝贝不能增加了哦!"),
    GOODS_REPLY_ERROR(100304,"商品评论失败，请稍后再试!"),
    GOODS_LEFTOVER_ERROR(100305,"商品库存更新失败!"),
    GOODS_LEFTOVER_NOT_ENOUGH(100307,"该商品剩余数不足！"),
    GOODS_LOVE_MORE_MONEY(100308,"积分小于实际支付金额！"),
    GOODS_SPECIFI_NOT_ONLINE(100309,"请选择详细的商品规格！"),
    GOODS_SPE_ERROR(100310,"添加商品规格异常！"),
    GOODS_BUY_ERROR(100311,"添加商品购买信息异常！"),
    GOODS_BASE_ERROR(100312,"添加商品基本信息异常！"),
    GOODS_UN_COMPLETE(100313,"商品信息不完整，请联系工作人员处理！"),

    GOODS_BUY_CEILING(100303,"您已达到购买该宝贝的上限了哦!"),
    GOODS_BUY_CEILING_DAY(100304,"您当日以达到购买该宝贝的上限了哦!"),
    GOODS_BUY_CEILING_WEEK(100305,"您本周以达到购买该宝贝的上限了哦!"),
    GOODS_BUY_CEILING_MOUTH(100306,"您本月以达到购买该宝贝的上限了哦!"),


    //用户错误
    USER_NOT_EXIST(100200,"该用户不存在！"),
    USER_IS_EXIST(100300,"该账号已存在！"),

    //订单错误
    ORDER_NOT_EXIST(100300,"该订单不存在！"),
    ORDER_NOT_YOU(100301,"订单不属于你！"),
    ORDER_TO_NUll(100302,"您还没有订单哦！"),
    ORDER_TO_ERROR(100303,"订单异常，请联系管理员处理！"),
    ORDER_NOT_MORE_COUPON(100304,"该优惠券不满足使用条件!"),
    USER_HARVESTADDRESS_TO_ERROR(100210,"订单绑定收货地址异常！"),
    ORDER_CANCEL_ERROR(100211,"订单取消异常！"),
    ORDER_BUY_LIMIT(100212,"限购商品超出购买数量"),

    //基金错误
    FUND_NOT_EXIST(100400,"该订单不存在！"),
    FUND_TO_NULL(100401,"目前没有捐赠哦！"),

    //商家错误
    MERCHANT_NOT_EXIST(100500,"该订单不存在！"),
    MERCHANT_TO_NULL(100501,"目前没有商家哦！"),

    //领取优惠券相关
    COUPON_NOT_EXIT(100600, "优惠券不存在"),
    COUPON_NOT_ENOUGH(100601, "优惠券已经领完了"),
    COUPON_GET_REPEAT(100602, "你已经领取过该优惠券"),
    COUPON_NOT_START(100603, "优惠券还没到领取时间"),
    COUPON_GET_FAIL(100604, "优惠券领取失败"),
    COUPON_GET_END(100605, "抱歉，今日已领完！"),

    // 第三方系统错误
    FULU_PHONE_CHARGE_ERROR(100700,""),

    // 参数错误
    PARAM_ERROR(100800, "参数错误"),
    PARAM_PAGE_START_ERROR(100801, "分页开始行必须为不小于0的整数"),
    PARAM_PAGE_ROWS_ERROR(100802, "分页行数必须为不小于0的整数"),
    INPUT_TXT_WRONGFUL(100803,"输入格式不合法(请查看是否用颜文字)"),
    INPUT_TXT_CHAT(100804,"输入格式不合法(请查看是否用敏感字符)"),


    SIGN_FAIL(100900,"签到失败！"),
    SIGN_REPEAT(100901,"今日已签到!"),
    SIGN_LIMIT(100902,"本月签到次数已达上限!"),


    // 权限错误
    PERMISSION_ERROR(100000, "权限错误"),
    PERMISSION_NOT_LOGIN(100001, "您尚未登录，请登录后操作"),
    PERMISSION_NOT_ALLOW(100002, "您无权操作此接口"),
    PERMISSION_NOT_ADMIN(100103, "您没有管理员权限，无法进行此项操作"),


    // 用户错误
    USER_ERROR(100200, "用户错误"),
    USER_NOT_EXISTS(100201, "用户不存在"),
    USER_ACCOUNT_PASSWORD_ERROR(100202, "用户帐号或者密码错误"),

    // 验证码异常
    CHECK_CODE_ERROR(100500, "验证码错误"),
    CHECK_CODE_REQUEST_TOO_OFTEN(100501, "验证码获取太频繁，请稍等"),
    CHECK_CODE_REQUEST_TOO_NOT_EXITS(100502, "验证码过期或不存在，请稍后再试!"),

    // 业务临时调整异常
    BUSINESS_CLOSED(900100, "业务待开放，敬请期待！"),
    BUSINESS_NOT_SUPPORT(900101, "业务升级，不再支持此操作，请升级到最新APP"),

    // UNKNOWN
    NOT_EXISTS(999998, "不存在"),
    UNKNOWN(999999, "其实我也不知道什么错误");
    ;

    @Getter
    private final int code;

    @Getter
    private final String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
