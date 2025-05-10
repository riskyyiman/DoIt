package project.riskyiman.doit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

class OnboardingFragment2 : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding2, container, false)

        // Ambil ViewPager dari parent Activity
        viewPager = requireActivity().findViewById(R.id.viewPager)

        val continueButton: Button = view.findViewById(R.id.continueButton)
        continueButton.setOnClickListener {
            // Pindah ke halaman 3 saat tombol ditekan
            viewPager.currentItem = 2 // indeks ke-2 (mulai dari 0)
        }

        return view
    }
}
