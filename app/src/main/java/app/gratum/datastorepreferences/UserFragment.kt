package app.gratum.datastorepreferences

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.gratum.datastorepreferences.databinding.FragmentUserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var binding: FragmentUserBinding
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserBinding.bind(view)

        binding.button.setOnClickListener {
            // Obtener los valores cuando el botón sea presionado
            val nameUser = binding.nameUser.text.toString()
            val isChecked = binding.checkBox.isChecked

            // Lanzar la corrutina
            viewLifecycleOwner.lifecycleScope.launch {
                saveValues(nameUser, isChecked)
            }
        }

        binding.button2.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                getUserProfile()?.collect {
                    withContext(Dispatchers.Main) {
                        binding.textView.text = it.name
                        if (it.rol) {
                            binding.textView2.text = "Es admin"
                        }else{
                            binding.textView2.text = "No es admin"
                        }
                    }
                }
            }
        }
    }

    private suspend fun saveValues(user: String, checked: Boolean) {
        context?.dataStore?.edit { preferences ->
            preferences[stringPreferencesKey(KEY_USER)] = user
            preferences[booleanPreferencesKey(KEY_CHECKED)] = checked
        }
    }

    private fun getUserProfile() = context?.dataStore?.data?.map { preferences ->
        UserModel(
            name = preferences[stringPreferencesKey(KEY_USER)].orEmpty(),
            rol = preferences[booleanPreferencesKey(KEY_CHECKED)] ?: false
        )
    }

    companion object {
        private const val KEY_USER = "user_key"
        private const val KEY_CHECKED = "checked_key"
    }
}
