package com.shlsoft.navdrawerlayout

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shlsoft.navdrawerlayout.fragments.*
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), DrawerAdapter.OnItemSelectedListener {
    private lateinit var screenTitles: Array<String>
    private lateinit var screenDrawables: Array<Drawable?>

    private var slidingRootNav: SlidingRootNav? = null

    companion object{
        private const val POS_MAIN = 0
        private const val POS_ACCOUNT = 1
        private const val POS_ABOUT = 2
        private const val POS_OUR_APPS = 3
        private const val POS_SHARE_APP = 4
        private const val POS_LOG_OUT = 6

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        installFragment(MainFragment())


        slidingRootNav = SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject()

        screenDrawables = loadScreenIcons()
        screenTitles = loadScreenTitles()

        val adapter = DrawerAdapter(
                listOf(
                        createItemFor(POS_MAIN),
                        createItemFor(POS_ACCOUNT),
                        createItemFor(POS_ABOUT),
                        createItemFor(POS_OUR_APPS),
                        createItemFor(POS_SHARE_APP),
                        createItemFor(POS_LOG_OUT)
                ) as List<DrawerItem<DrawerAdapter.ViewHolder>>
        )

        adapter.setListener(this)
        val rv = findViewById<RecyclerView>(R.id.drawer_list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        //Default value
        adapter.setSelected(0)
    }


    private fun loadScreenTitles(): Array<String>{
        return resources.getStringArray(R.array.screenTitles)
    }

    private fun loadScreenIcons() : Array<Drawable?>{
        val typedArray = resources.obtainTypedArray(R.array.screenIcons)
        val icons = arrayOfNulls<Drawable>(typedArray.length())
        for(i in 0 until typedArray.length()){
            val id = typedArray.getResourceId(i, 0)
            if(id != 0){
                icons[i] = ContextCompat.getDrawable(this,id)
            }
        }
        typedArray.recycle()
        return icons
    }

    private fun createItemFor(position: Int) : DrawerItem<*>{
      return SimpleItem(screenDrawables[position]!!, screenTitles[position])
              .withIconTint(color(R.color.black))
              .withTextTint(color(R.color.black))
              .withSelectedIconTint(color(R.color.yellow))
              .withSelectedTextTint(color(R.color.yellow))
    }
    @ColorInt
    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(this, res)
    }

    override fun onItemSelected(position: Int) {
        if(position == POS_MAIN){
            title = "Bosh sahifa"
            installFragment(MainFragment())
        }
        else if(position == POS_ACCOUNT){
            title = "Mening profilim"
            installFragment(ProfileFragment())
        }
        else if(position == POS_ABOUT){
            title = "Biz haqimizda"
            installFragment(InfoFragment())
        }
        else if(position == POS_OUR_APPS){
            title = "Dasturlarimiz"
            installFragment(AppsFragment())
        }
        else if(position == POS_SHARE_APP){
            title = "Ulashish"
            installFragment(ShareFragment())
        }
        else if(position == POS_LOG_OUT - 1){
            finish()
        }

        slidingRootNav!!.closeMenu()
    }

    fun installFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onBackPressed() {
        finish()
    }
}