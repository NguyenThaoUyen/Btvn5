package com.example.btvn5

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.text.Layout
import android.view.Menu
import android.widget.Toast
import com.example.btvn5.Frament.NowplayingFragment
import com.example.btvn5.Frament.TopRateFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tab_Layout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = "List Movie"

        viewPager = findViewById(R.id.main_views)
        tab_Layout = findViewById(R.id.tablayout)
        setStaticPageAdapter()
        tab_Layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                //   viewPager.notifyAll();
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // setAdapter();
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) {
                    supportFragmentManager.popBackStack()
                }
                ft.commit()
            }

        })
    }


    private fun setStaticPageAdapter() {
        val myViewPagerAdapter: ViewStaticPageAdapter = ViewStaticPageAdapter(supportFragmentManager)
        myViewPagerAdapter.addFragment(NowplayingFragment(), "Now Playing")
        myViewPagerAdapter.addFragment(TopRateFragment(), "Top Rate")
        viewPager.adapter = myViewPagerAdapter
        tab_Layout.setupWithViewPager(viewPager, true)


    }
    class ViewStaticPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        val fragments: MutableList<Fragment> = ArrayList<Fragment>()
        val titles: MutableList<String> = ArrayList<String>()
        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles.get(position)
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.movie_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery = query.toString()
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("keyword", searchQuery)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }

        })
        return true
    }

}
