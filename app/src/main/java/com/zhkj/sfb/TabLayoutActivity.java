package com.zhkj.sfb;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;
import com.zhkj.sfb.adapter.FragmentAdapter;
import com.zhkj.sfb.common.BasetActivity;
import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends BasetActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("施肥规程");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        initViewPager();
    }

    private void initViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("小麦");
        titles.add("玉米");
        titles.add("棉花");
        titles.add("大豆");
        titles.add("花生");
        titles.add("油菜");
        titles.add("苹果");
        titles.add("梨");
        titles.add("桃");
        titles.add("葡萄");
        titles.add("草莓");
        titles.add("番茄");
        titles.add("辣椒");
        titles.add("茄子");
        titles.add("黄瓜");
        titles.add("马铃薯");
        titles.add("西瓜");
        titles.add("白菜");
        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        for(int i=0;i<titles.size();i++){
            if(i==0){
                fragments.add(new WeatsFragment());
            }
            if(i==1){
                fragments.add(new CornFragment());
            }
            if(i==2){
                fragments.add(new CottonFragment());
            }
            if(i==3){
                fragments.add(new BeansFragment());
            }
            if(i==4){
                fragments.add(new PeanutFragment());
            }
            if(i==5){
                fragments.add(new RapeFragment());
            }
            if(i==6){
                fragments.add(new AppleFragment());
            }
            if(i==7){
                fragments.add(new PearFragment());
            }
            if(i==8){
                fragments.add(new PeachFragment());
            }
            if(i==9){
                fragments.add(new GrapeFragment());
            }
            if(i==10){
                fragments.add(new StrawberryFragment());
            }
            if(i==11){
                fragments.add(new TomatoFragment());//番茄
            }
            if(i==12){
                fragments.add(new PepperFragment());
            }
            if(i==13){
                fragments.add(new EggplantFragment());
            }
            if(i==14){
                fragments.add(new CucumberFragment());
            }
            if(i==15){
                fragments.add(new PotatoFragment());
            }
            if(i==16){
                fragments.add(new WatermelonFragment());
            }
            if(i==17){
                fragments.add(new CabbageFragment());
            }
        }

        FragmentAdapter mFragmentAdapteradapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapteradapter);
    }
}
