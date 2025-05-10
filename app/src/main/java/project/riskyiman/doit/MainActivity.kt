package project.riskyiman.doit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)

        val fragments = listOf(
            OnboardingFragment.newInstance(R.layout.fragment_onboarding1),
            OnboardingFragment.newInstance(R.layout.fragment_onboarding2),
            OnboardingFragment.newInstance(R.layout.fragment_onboarding3)
        )

        val adapter = OnboardingAdapter(this, fragments)
        viewPager.adapter = adapter

        // Handle Window Insets untuk edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
