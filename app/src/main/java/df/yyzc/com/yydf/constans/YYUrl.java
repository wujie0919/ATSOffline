package df.yyzc.com.yydf.constans;

import df.yyzc.com.yydf.base.YYDFApp;

/**
 * Created by zhangyu on 16-4-19.
 * <p/>
 * 所有请求地址
 */
public class YYUrl {


    /**
     * 登录
     */
    public static String loginURL = YYDFApp.BASEIP + "home/login/login";

    /**
     * 通过用户skey获取用户信息
     */
    public static String getUserInfoById = YYDFApp.BASEIP + "home/user/getUserInfoById";


    /**
     * 未开始订单列表
     */
    public static String notBeginOrderList = YYDFApp.BASEIP + "home/order/notBeginOrderList";


    /**
     * 我的订单列表
     */
    public static String myOrderList = YYDFApp.BASEIP + "home/index/myOrderList";
    /**
     * 确认接单
     */
    public static String pickOrderCar = YYDFApp.BASEIP + "home/order/pickOrderCar";

    /**
     * 订单详情
     */
    public static String orderDetail = YYDFApp.BASEIP + "home/order/detail";


    /**
     * 鸣笛操作
     */
    public static String findCar = YYDFApp.BASEIP + "home/car/findCar";

    /**
     * 开门操作
     */
    public static String openCar = YYDFApp.BASEIP + "home/car/openCar";

    /**
     * 关门操作
     */
    public static String closeCar = YYDFApp.BASEIP + "home/car/closeCar";

    /**
     * 整备订单-确认整备
     */
    public static String serviceOrder = YYDFApp.BASEIP + "home/order/serviceOrder";


    /**
     * 整备订单-车辆上线
     */
    public static String finishOrder = YYDFApp.BASEIP + "home/order/finishOrder";
    /**
     * 接车订单-完成订单
     */
    public static String completeOrder = YYDFApp.BASEIP + "home/order/completeOrder";
    /**
     * 接车订单-确认接车
     */
    public static String commitGetCar = YYDFApp.BASEIP + "/home/order/commitGetCar";

    /**
     * 站点列表
     */
    public static String queryList = YYDFApp.BASEIP + "home/station/queryList";

    /**
     * 站点列表(还车，推荐站点)
     */
    public static String selectStationListRecommend = YYDFApp.BASEIP + "home/station/selectStationListRecommend";

    /**
     * 站点问题列表
     */
    public static String queryStationProblemImgs = YYDFApp.BASEIP + "home/station/queryStationProblemImgs";

    /**
     * 接车订单-获取地服订单已检查的项目
     */
    public static String queryOrderCheckList = YYDFApp.BASEIP + "home/order/queryOrderCheckList";


    /**
     * 获取车辆检查项目
     */
    public static String queryCheckItemList = YYDFApp.BASEIP + "home/order/queryCheckItemList";
    /**
     * 获取检查项大类下所有的检查项(如已检查过，包含已检查数据)
     */
    public static String queryItemsCheckedList = YYDFApp.BASEIP + "home/order/queryItemsCheckedList";


    /**
     * 接车订单-检查地服订单
     * <p/>
     * 提交
     */
    public static String checkOrderCar = YYDFApp.BASEIP + "home/order/checkOrderCar";


    /**
     * 地服订单取消订单
     */
    public static String cancelOrder = YYDFApp.BASEIP + "home/order/cancelOrder";


    /**
     * 地服人员列表
     */
    public static String userQueryList = YYDFApp.BASEIP + "home/user/queryList";

    /**
     * 地服订单派单操作
     */
    public static String assignOrder = YYDFApp.BASEIP + "home/order/assignOrder";

    /**
     * 地服订单派单操作
     */
    public static String updateNoOnlineReasonState = YYDFApp.BASEIP + "home/order/updateNoOnlineReasonState";
    /**
     * 站点上传记录接口
     */
    public static String queyStationsImgByUserId = YYDFApp.BASEIP + "home/station/queyStationsImgByUserId";
    /**
     * 停车费用上传记录
     */
    public static String saveStoping = YYDFApp.BASEIP + "home/order/saveStoping";

    /**
     * 站点 问题反馈 保存
     */
    public static String saveStationProblemImgs = YYDFApp.BASEIP + "home/station/saveStationProblemImgs";
    /**
     * 站点图片更新
     */
    public static String saveStationsImg = YYDFApp.BASEIP + "home/station/saveStationsImg";

    /**
     * 站点 问题已解决
     */
    public static String updateStationProblemImgs = YYDFApp.BASEIP + "home/station/updateStationProblemImgs";

    /**
     * 停车费用详情
     */
    public static String detailStoping = YYDFApp.BASEIP + "home/order/detailStoping";

}
