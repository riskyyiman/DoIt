package project.riskyiman.doit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class OnboardingFragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding3, container, false)

        val continueButton = view.findViewById<Button>(R.id.continueButton)

        continueButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateAccountActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
