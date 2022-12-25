package com.example.tts_test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tts_test.core.extensions.log
import com.example.tts_test.domain.repositories.HackathonInterface
import com.example.tts_test.domain.use_cases.SummarizeTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val hackathonInterface: HackathonInterface
): ViewModel() {

    private val summarizedTextData: MutableLiveData<String> = MutableLiveData("")
    val summarizedTextLiveData: LiveData<String> = summarizedTextData

    fun summarizeText(text: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultText = SummarizeTextUseCase(hackathonInterface).execute(text)
            if(resultText.isBlank()) {
                log("tts", "Empty result")
            } else {
                log("tts", "Got result: $resultText")
                summarizedTextData.postValue(resultText)
            }
        }
    }
}