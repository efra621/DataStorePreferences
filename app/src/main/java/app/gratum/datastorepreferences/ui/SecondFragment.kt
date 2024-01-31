package app.gratum.datastorepreferences.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.gratum.datastorepreferences.R
import app.gratum.datastorepreferences.data.DefaultUserRepository
import app.gratum.datastorepreferences.databinding.ActivityMainBinding
import app.gratum.datastorepreferences.databinding.FragmentSecondBinding
import app.gratum.datastorepreferences.databinding.FragmentUserBinding
import app.gratum.datastorepreferences.model.UserViewState
import app.gratum.datastorepreferences.viewmodel.UserViewModel
import app.gratum.datastorepreferences.viewmodel.UserViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SecondFragment : Fragment(R.layout.fragment_second) {

    private lateinit var binding: FragmentSecondBinding
    private lateinit var viewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSecondBinding.bind(view)

        val userRepository = DefaultUserRepository(requireContext())
        viewModel = ViewModelProvider(this, UserViewModelFactory(userRepository))
            .get(UserViewModel::class.java)

        viewModel.getUserProfile()
        initFlows()

    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userViewState.collectLatest { state ->
                    when (state) {
                        is UserViewState.Loading -> {
                            Log.d("???", "Loading")
                            // Show loading state
                        }

                        is UserViewState.Success -> {
                            Log.d("???", "Succes")
                            binding.textView.text = state.userModel.name
                            binding.textView2.text =
                                if (state.userModel.rol) "Es admin" else "No es admin"
                        }

                        is UserViewState.Error -> {
                            // Show error state
                            Log.d("???", "Error")
                            Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

}