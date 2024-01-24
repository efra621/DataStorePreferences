package app.gratum.datastorepreferences.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.gratum.datastorepreferences.R
import app.gratum.datastorepreferences.data.DefaultUserRepository
import app.gratum.datastorepreferences.databinding.FragmentUserBinding
import app.gratum.datastorepreferences.model.UserViewState
import app.gratum.datastorepreferences.viewmodel.UserViewModel
import app.gratum.datastorepreferences.viewmodel.UserViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserBinding.bind(view)
        val userRepository = DefaultUserRepository(requireContext())
        viewModel = ViewModelProvider(this, UserViewModelFactory(userRepository))
            .get(UserViewModel::class.java)

        setupUI()
        initFlows()
    }

    private fun setupUI() {
        binding.button.setOnClickListener {
            val nameUser = binding.nameUser.text.toString()
            val isChecked = binding.checkBox.isChecked

            viewModel.saveUser(nameUser, isChecked)
        }

        viewModel.getUserProfile()

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

