package project.riskyiman.doit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

class OnboardingFragment : Fragment() {

    companion object {
        private const val ARG_LAYOUT_ID = "layoutId"

        fun newInstance(layoutId: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putInt(ARG_LAYOUT_ID, layoutId)
            fragment.arguments = args
            return fragment
        }
    }

    private var layoutId: Int = 0
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            layoutId = it.getInt(ARG_LAYOUT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)

        // Ambil viewPager dari Activity
        viewPager = requireActivity().findViewById(R.id.viewPager)

        when (layoutId) {
            R.layout.fragment_onboarding2 -> {
                val continueButton = view.findViewById<Button>(R.id.continueButton)
                continueButton.setOnClickListener {
                    viewPager.currentItem = 2 // Pindah ke halaman ke-3
                }
            }

            R.layout.fragment_onboarding3 -> {
                val continueButton = view.findViewById<Button>(R.id.continueButton)
                continueButton.setOnClickListener {
                    // Pindah ke CreateAccountActivity
                    val intent = Intent(requireContext(), CreateAccountActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        return view
    }
}
