package com.txh.im.photo;
import java.util.ArrayList;
import java.util.List;

public class IntentConstants
{
	//相册中图片对象集合
	public static final String EXTRA_IMAGE_LIST = "image_list";
	//相册名称
	public static final String EXTRA_BUCKET_NAME = "buck_name";
	//可添加的图片数量
	public static final String EXTRA_CAN_ADD_IMAGE_SIZE = "can_add_image_size";
	//当前选择的照片位置
	public static final String EXTRA_CURRENT_IMG_POSITION = "current_img_position";

	public static final String APPLICATION_NAME = "myApp";
	//单次最多发送图片数
	public static final int MAX_IMAGE_SIZE = 4;
	//首选项:临时图片
	public static final String PREF_TEMP_IMAGES = "pref_temp_images";

	//相机返回码
	public static final int TAKE_PICTURE = 000;
	//相册返回码
	public static final int PHOTO_ALBUM = 111;
	//相片返回码
	public static final int PHOTO_GRAPH = 222;
	//拍照路径
	public static String PHOTO_URL = "";
	//拍照路径
	public static  boolean PHOTO_FLAG = false;

	public static List<ImageItem> PHOTO_LIST = new ArrayList<>();
}
