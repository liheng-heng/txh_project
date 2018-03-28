package com.txh.im;

/**
 * Created by Administrator on 2017/2/21.
 */

public class Api {

    /**
     * API入口
     * 分为 ：
     * 开发环境
     * 测试环境
     * 以及最终发版的正式环境
     */
    //头像--上传服务器
    public static String head_image = "http://upload.netpub.tianxiahuo.cn/api/upload";
//-------------------------------------------------------------------------------------------

    //.NET 开发环境
//    public static String head_net = "http://192.168.1.47:9001/Api/Invoke";
    //.NET 测试环境
    public static String head_net = "http://api2.nettestNew.tianxiahuo.cn/Api/Invoke";
//    public static String head_net = "http://api.nettestNew.tianxiahuo.cn/Api/Invoke";
    //.NET 正式地址
//    public static String head_net = "http://api.netpush.tianxiahuo.cn/api/Invoke";

    //.H5测试
    public static String head_html = "http://h5.new.tianxiahuo.cn";
    //.H5正式
//    public static String head_html = "http://h5.netpush.tianxiahuo.cn";


    /**
     * 登录注册  个人中心
     */
    //1.根据类型发送短信验证码
    public static final String GetInfo_SendMsgCodeByType = "PZ.TX_App_User.api.GetInfo.SendMsgCodeByType";
    //2.校验短信验证码（用户注册）
    public static final String GetInfo_CheckMsgCodeForUserRegister = "PZ.TX_App_User.api.GetInfo.CheckMsgCodeForUserRegister";
    //3.用户注册
    public static final String GetInfo_UserRegister = "PZ.TX_App_User.api.GetInfo.UserRegister";
    //4.用户密码登录
    public static final String GetInfo_UserPwdLogin = "PZ.TX_App_User.api.GetInfo.UserPwdLogin";
    //5.用户通过短信验证码登录
    public static final String GetInfo_PhoneMsgCodeLogin = "PZ.TX_App_User.api.GetInfo.PhoneMsgCodeLogin";
    //6.校验短信验证码（重置登录密码）
    public static final String GetInfo_CheckMsgCodeForSetLoginPwd = "PZ.TX_App_User.api.GetInfo.CheckMsgCodeForSetLoginPwd";
    //7.通过短信验证码重置登录密码
    public static final String GetInfo_SetLoginPwdByMsgCode = "PZ.TX_App_User.api.GetInfo.SetLoginPwdByMsgCode";
    //8.获取APP底部菜单数据（有用户登录信息验证）
    public static final String GetInfo_GetButtomMenuData = "PZ.TX_App_User.api.GetInfo.GetButtomMenuData";
    //9.获取APP默认页面数据
    public static final String GetInfo_GetAppDefaultData = "PZ.TX_App_User.api.GetInfo.GetAppDefaultData";
    //12.获取“我的”页面信息（有用户登录信息验证）
    public static final String GetInfo_GetMyPerson = "PZ.TX_App_User.api.GetInfo.GetMyPerson";
    //13.获取“我的资料”页面信息（有用户登录信息验证）
    public static final String GetInfo_GetMyInfo = "PZ.TX_App_User.api.GetInfo.GetMyInfo";
    //14.重置密码（有用户登录信息验证，通过老密码修改新密码）
    public static final String GetInfo_ChangeLoginPwdByOld = "PZ.TX_App_User.api.GetInfo.ChangeLoginPwdByOld";
    //15.根据父级ID和地区类型获取地区列表（有用户登录信息验证）
    public static final String GetInfo_GetLocationsByPId = "PZ.TX_App_User.api.GetInfo.GetLocationsByPId";
    //16.保存我的资料信息并返回新的我的资料页面所需数据（有用户登录信息验证）
    public static final String GetInfo_SaveMyInfo = "PZ.TX_App_User.api.GetInfo.SaveMyInfo";
    //17.保存新头像地址（修改头像）（有用户登录信息验证）
    public static final String GetInfo_SaveImagesHead = "PZ.TX_App_User.api.GetInfo.SaveImagesHead";
    //20.获取收货地址列表（有用户登录信息验证）
    public static final String GetInfo_GetUserAddressList = "PZ.TX_App_User.api.GetInfo.GetUserAddressList";
    //21.根据地址ID获取地址详细信息（有用户登录信息验证）
    public static final String GetInfo_GetUAddressById = "PZ.TX_App_User.api.GetInfo.GetUAddressById";
    //22.根据收货地址ID设置默认收货地址（有用户登录信息验证）
    public static final String GetInfo_SetUAddressDefaultById = "PZ.TX_App_User.api.GetInfo.SetUAddressDefaultById";
    //23.根据收货地址ID删除收货地址（有用户登录信息验证）
    public static final String GetInfo_DeleteUAddressById = "PZ.TX_App_User.api.GetInfo.DeleteUAddressById";
    //24.新增或修改收货地址（有用户登录信息验证）
    public static final String GetInfo_SaveUAddress = "PZ.TX_App_User.api.GetInfo.SaveUAddress";
    //29.身份切换（有用户登录信息验证）（买卖）
    public static final String GetInfo_ChangeUnitType = "PZ.TX_App_User.api.GetInfo.ChangeUnitType";
    //31.根据协议类型获取协议内容
    public static final String GetInfo_GetAgreementByType = "PZ.TX_App_User.api.GetInfo.GetAgreementByType";
    //36.获取App分享配置信息（有用户登录信息验证）
    public static final String GetInfo_GetAppShareInfo = "PZ.TX_App_User.api.GetInfo.GetAppShareInfo";
    //41.退出登录（有用户登录信息验证）
    public static final String GetInfo_ExitLogin = "PZ.TX_App_User.api.GetInfo.ExitLogin";

    /**
     * 商城相关接口
     */
    //1.获取商城首页数据（有用户登录信息验证）
    public static final String Mall_SearchDataForMallFPage = "PZ.TX_App_User.api.Mall.SearchDataForMallFPage";
    //2.获取搜索历史关键字记录（有用户登录信息验证）
    public static final String Mall_GetUserSearchHistory = "PZ.TX_App_User.api.Mall.GetUserSearchHistory";
    //3.清空指定搜索类型的历史记录（有用户登录信息验证）
    public static final String Mall_CleanSearchHistory = "PZ.TX_App_User.api.Mall.CleanSearchHistory";
    //4.获取店铺可用于聊天的人员列表（有用户登录信息验证）
    public static final String Mall_GetUserListForChat = "PZ.TX_App_User.api.Mall.GetUserListForChat";
    //5.获取商品一级分类列表（有用户登录信息验证）
    public static final String Mall_GetItemCategory = "PZ.TX_App_User.api.Mall.GetItemCategory";
    //6.在商户中搜索商品（有用户登录信息验证）
    public static final String Mall_SearchItemPageList = "PZ.TX_App_User.api.Mall.SearchItemPageList";
    //7.改变购物车中商品数量或添加到购物车（有用户登录信息验证）
    public static final String Mall_ChangeShopCartItemNum = "PZ.TX_App_User.api.Mall.ChangeShopCartItemNum";
    //8.获取最新的购物车和货架商品数量（有用户登录信息验证）
    public static final String Mall_GetLatestCartShelfNum = "PZ.TX_App_User.api.Mall.GetLatestCartShelfNum";
    //9.获取商户中指定商品的详情（有用户登录信息验证）
    public static final String Mall_GetShopItemDetail = "PZ.TX_App_User.api.Mall.GetShopItemDetail";
    //12.购物车去结算（有用户登录信息验证）
    public static final String TX_App_User_GetCartPreSubmitData = "PZ.TX_App_User.api.Mall.GetCartPreSubmitData";
    //10.获取购物车页面所需数据（有用户登录信息验证）
    public static final String TX_App_User_GetShopCartData = "PZ.TX_App_User.api.Mall.GetShopCartData";
    //11.从购物车中删除选择的商品（有用户登录信息验证）
    public static final String TX_App_User_RemoveItemsFromCart = "PZ.TX_App_User.api.Mall.RemoveItemsFromCart";
    //13.结算界面提交订单（有用户登录信息验证）
    public static final String Mall_SubmitOrder = "PZ.TX_App_User.api.Mall.SubmitOrder";
    //14.订单详情页面（有用户登录信息验证）
    public static final String Mall_GetOrderDetail = "PZ.TX_App_User.api.Mall.GetOrderDetail";
    //15.卖家身份时获取我关联的商户列表（有用户登录信息验证）
    public static final String Mall_GetShopListOfUser = "PZ.TX_App_User.api.Mall.GetShopListOfUser";
    //16.获取我的订单列表类型菜单（顶部tab）（有用户登录信息验证）
    public static final String Mall_GetOrderListTypes = "PZ.TX_App_User.api.Mall.GetOrderListTypes";
    //17.订单分页搜索列表（有用户登录信息验证）
    public static final String Mall_GetOrderPageList = "PZ.TX_App_User.api.Mall.GetOrderPageList";
    //18.订单操作（接单、拒绝接单、发货、确认收货、删除订单、再次下单等）（有用户登录信息验证）
    public static final String Mall_OpOrder = "PZ.TX_App_User.api.Mall.OpOrder";
    //20.根据大类获取商品品牌与子类列表
    public static final String Mall_GetSubCategoryAndBrand = "PZ.TX_App_User.api.Mall.GetSubCategoryAndBrand";
    //21.获取促销公告
    public static final String Mall_GetActivityInfo = "PZ.TX_App_User.api.Mall.GetActivityInfo";
    //1.首页消息列表用户信息
    public static final String TX_App_SNS_IndexMessage = "PZ.TX_App_SNS.api.FirstPage.IndexMessage";

    /**
     * 朋友相关
     */
    //2.搜索用户
    public static final String TX_App_SNS_Search = "PZ.TX_App_SNS.api.FirstPage.Search";

    //3.搜索好友
    public static final String TX_App_SNS_SearchFriend = "PZ.TX_App_SNS.api.FirstPage.SearchFriend";

    //4.添加好友
    public static final String TX_App_SNS_AddFriend = "PZ.TX_App_SNS.api.FirstPage.AddFriend";

    //5.获取用户好友列表
    public static final String TX_App_SNS_GetFriendList = "PZ.TX_App_SNS.api.FirstPage.GetFriendList";

    //6.获取用户好友详情
    public static final String TX_App_SNS_GetFriendDetail = "PZ.TX_App_SNS.api.FirstPage.GetFriendDetail";

    //12.获取好友请求列表
    public static final String TX_App_SNS_GetAskList = "PZ.TX_App_SNS.api.FirstPage.GetAskList";

    //13.接受添加好友操作
    public static final String TX_App_SNS_TakeFriend = "PZ.TX_App_SNS.api.FirstPage.TakeFriend";

    //14.删除好友请求
    public static final String TX_App_SNS_DeleteAsk = "PZ.TX_App_SNS.api.FirstPage.DeleteAsk";

    //15.获取用户好友单位详情
    public static final String TX_App_SNS_FriendUnitDetail = "PZ.TX_App_SNS.api.FirstPage.FriendUnitDetail";

    //16.修改好友备注
    public static final String TX_App_SNS_EditUserMark = "PZ.TX_App_SNS.api.FirstPage.EditUserMark";

    //17.修改好友单位备注
    public static final String TX_App_SNS_EditUnitMark = "PZ.TX_App_SNS.api.FirstPage.EditUnitMark";

    //18.扫描二维码
    public static final String TX_App_SNS_ScanQRCode = "PZ.TX_App_SNS.api.FirstPage.ScanQRCode";

    //19.推荐好友列表
    public static final String TX_App_SNS_PushFriend = "PZ.TX_App_SNS.api.FirstPage.PushFriend";

    //20.获取用户未处理请求数量
    public static final String TX_App_SNS_GetAskNotReadCount = "PZ.TX_App_SNS.api.FirstPage.GetAskNotReadCount";

    //21.设置用户请求为已读
    public static final String TX_App_SNS_SetAskReaded = "PZ.TX_App_SNS.api.FirstPage.SetAskReaded";

    //22.获取推荐用户详细信息
    public static final String TX_App_SNS_PushFriendDetail = "PZ.TX_App_SNS.api.FirstPage.PushFriendDetail";

    //23.获取聊天用户信息
    public static final String TX_App_SNS_GetMessageUser = "PZ.TX_App_SNS.api.FirstPage.GetMessageUser";

    //24.获取聊天用户单位ID//PZ_SNS
    public static final String TX_App_SNS_GetShopUnitId = "PZ.TX_App_SNS.api.FirstPage.GetShopUnitId";

    //30.根据用户ID和商家类型获取用户关联的商家列表（包含二维码信息）（有用户登录信息验证）
    public static final String TX_App_User_GetUserMapUnitInfo = "PZ.TX_App_User.api.GetInfo.GetUserMapUnitInfo";

    /**
     * 启动页  版本控制
     */
    //1.获取启动页数据
    public static final String AppSys_GetStartPageInfo = "PZ.TX_App_User.api.AppSys.GetStartPageInfo";
    //2.获取版本更新信息（有用户登录信息验证）
    public static final String AppSys_GetVersionUpdate = "PZ.TX_App_User.api.AppSys.GetVersionUpdate";

    /**
     * PZ_PriceGroup
     */
    //1.获取设置过单店单价的门店分页列表（有用户登录信息验证）
    public static final String PriceGroup_GetUPriceUnitPageList = "PZ.TX_App_User.api.PriceGroup.GetUPriceUnitPageList";
    //2.获取可见的价格分组（有用户登录信息验证）
    public static final String PriceGroup_GetPriceGroupList = "PZ.TX_App_User.api.PriceGroup.GetPriceGroupList";
    //3.禁用价格分组（有用户登录信息验证）
    public static final String PriceGroup_ProhibitGroup = "PZ.TX_App_User.api.PriceGroup.ProhibitGroup";
    //7.价格分组中的商品价格列表页面外部数据（页面标题、左侧一级商品分类、价格分组名称）（有用户登录信息验证）
    public static final String PriceGroup_GetItemCategoryForGroup = "PZ.TX_App_User.api.PriceGroup.GetItemCategoryForGroup";
    //8.价格分组中的商品分页列表（有用户登录信息验证）
    public static final String PriceGroup_SearchItemPageListForGroup = "PZ.TX_App_User.api.PriceGroup.SearchItemPageListForGroup";
    //9.获取商品分类下品牌列表（有用户登录信息验证）--品牌
    public static final String PriceGroup_GetItemBrand = "PZ.TX_App_User.api.PriceGroup.GetItemBrand";
    //10.获取商品一级分类下的二级分类（品类）列表（有用户登录信息验证）--品类
    public static final String PriceGroup_GetChildCategory = "PZ.TX_App_User.api.PriceGroup.GetChildCategory";
    //11.修改价格分组名称（有用户登录信息验证）
    public static final String PriceGroup_UpdateGroupName = "PZ.TX_App_User.api.PriceGroup.UpdateGroupName";
    //12.修改某个商品分组价格（有用户登录信息验证）
    public static final String PriceGroup_UpdateGPrice = "PZ.TX_App_User.api.PriceGroup.UpdateGPrice";
    //13.单店单价中门店分页列表（有用户登录信息验证）
    public static final String PriceGroup_GetUnitPLForPrice = "PZ.TX_App_User.api.PriceGroup.GetUnitPLForPrice";
    //14.向单店单价中添加门店或从分组中删除门店（有用户登录信息验证）
    public static final String PriceGroup_AddOrDelUnitOfPrice = "PZ.TX_App_User.api.PriceGroup.AddOrDelUnitOfPrice";
    //15.分组中未添加、已添加门店分页列表（有用户登录信息验证）
    public static final String PriceGroup_GetUnitPLForGroup = "PZ.TX_App_User.api.PriceGroup.GetUnitPLForGroup";
    //16.向分组中添加门店或从分组中删除门店（有用户登录信息验证）
    public static final String PriceGroup_AddOrDelUnitOfGroup = "PZ.TX_App_User.api.PriceGroup.AddOrDelUnitOfGroup";
    //17.单店单价商品分类列表页面外部数据（页面标题、左侧商品分类、分组名称、店铺名称）（有用户登录信息验证）
    public static final String PriceGroup_GetCategoryForUPrice = "PZ.TX_App_User.api.PriceGroup.GetCategoryForUPrice";
    //18.单店单价中针对某个门店的商品分页列表（有用户登录信息验证）
    public static final String PriceGroup_SearchItemPLForUPrice = "PZ.TX_App_User.api.PriceGroup.SearchItemPLForUPrice";
    //19.删除单店单价（还原价格分组价）（有用户登录信息验证）
    public static final String PriceGroup_DeleteUPrice = "PZ.TX_App_User.api.PriceGroup.DeleteUPrice";
    //20.设置单店单价（有用户登录信息验证）
    public static final String PriceGroup_SetUPrice = "PZ.TX_App_User.api.PriceGroup.SetUPrice";
    //21.获取有效的价格分组（有用户登录信息验证）
    public static final String PriceGroup_GetValidGroup = "PZ.TX_App_User.api.PriceGroup.GetValidGroup";
    //22.保存代下单界面上给门店选择的价格分组（有用户登录信息验证）
    public static final String PriceGroup_SaveUnitGroup = "PZ.TX_App_User.api.PriceGroup.SaveUnitGroup";
    //23.启用被禁用的价格分组（有用户登录信息验证）
    public static final String PriceGroup_EnableGroup = "PZ.TX_App_User.api.PriceGroup.EnableGroup";

    /**
     * pz_center
     */
    //32.扫描二维码登录
    public static final String PZ_Center_ScanQRCode = "PZ.TX_App_User.api.GetInfo.ScanQRCode";

    /**
     * PZ_SNS_Near
     */
    //1.获取附近的商铺
    public static final String Near_GetShopList = "PZ.TX_App_SNS.api.Near.GetShopList";
    //2.获取附近页面头部
    public static final String Near_GetNearHead = "PZ.TX_App_SNS.api.Near.GetNearHead";

    /**
     * PZ_Coupon 优惠券
     */
    //3.获取最新三张待提醒的用户优惠券列表（异步调用，如果返回为空不提醒）（有用户登录信息验证）
    public static final String Coupon_GetRemindCouponList = "PZ.TX_App_User.api.Coupon.GetRemindCouponList";

}
