package cr.ac.menufragmentcurso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

lateinit var drawerLayout: DrawerLayout


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        var toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.mostrarBarra,
            R.string.cerrarBarra
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        var navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment: Fragment
        when(item.itemId){
            R.id.nav_camera -> fragment = CameraFragment.newInstance(param1 = "camera")
            R.id.nav_gallery -> fragment = GalleryFragment.newInstance(param1 = "galeria")
            else -> fragment = HomeFragment.newInstance(param1 = "home")
        }


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
        setTitle(R.string.homeFragment)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}