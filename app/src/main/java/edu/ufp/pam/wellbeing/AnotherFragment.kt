package edu.ufp.pam.wellbeing
// AnotherFragment.kt
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AnotherFragment : Fragment() {

    companion object {
        // This method is used to create a new instance of the fragment
        fun newInstance(): AnotherFragment {
            return AnotherFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_another, container, false)
    }
}
