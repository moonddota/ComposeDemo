package com.example.composedemo.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.common.topBar
import com.example.composedemo.common.signin.EmailState
import com.example.composedemo.common.signin.PasswordState
import com.example.composedemo.common.signin.TextFieldState
import com.example.composedemo.ui.MainActions
import com.example.composedemo.viewmodel.MyViewModel

@Composable
fun RegisterPage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {

    Scaffold(
        modifier = modifier,
        topBar = {
            topBar(
                title = stringResource(id = R.string.sign_register),
                showBack = false,
                click = { myViewModel.showLoging.postValue(true) })
        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                val focusRequester = remember { FocusRequester() }
                val emailState = remember { EmailState() }
                AccountNumber(emailState, onImeAction = { focusRequester.requestFocus() })
                Spacer(modifier = Modifier.height(16.dp))
                val passwordState = remember { PasswordState() }
                RegisterPassword(
                    label = stringResource(id = R.string.password),
                    passwordState = passwordState,
                    modifier = Modifier.focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(16.dp))
                val rePasswordState = remember { PasswordState() }
                RegisterPassword(
                    label = stringResource(id = R.string.repassword),
                    passwordState = rePasswordState,
                    modifier = Modifier.focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(100.dp))
                Button(
                    onClick = { myViewModel.register(actions, emailState.text, passwordState.text,rePasswordState.text) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(50.dp, 0.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text)),
                    shape = RoundedCornerShape(30.dp),
                    enabled = emailState.isValid && passwordState.isValid && rePasswordState.isValid
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_register),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    )

}

@Composable
fun RegisterPassword(
    label: String,
    passwordState: TextFieldState,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = passwordState.text,
        onValueChange = {
            passwordState.text = it
            passwordState.enableShowErrors()
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(50.dp, 20.dp, 50.dp, 0.dp)
            .onFocusChanged { focusState ->
                val focused = focusState == FocusState.Active
                passwordState.onFocusChange(focused)
                if (!focused) {
                    passwordState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2
                )
            }
        },
        visualTransformation = VisualTransformation.None,
        isError = passwordState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
    passwordState.getError()?.let { error -> TextFieldError(textError = error) }
}