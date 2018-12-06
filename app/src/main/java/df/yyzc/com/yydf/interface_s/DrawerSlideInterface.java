package df.yyzc.com.yydf.interface_s;

public interface DrawerSlideInterface {

    /*
     * float slideOffset 在0~1之间的数值
     */
    void onDrawerSlide(float slideOffset);

    /**
     * 全部展示
     */
    void onDrawerShow();

}
