package com.example.composedemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.common.PlayAppBar
import com.example.composedemo.common.signin.EmailState
import com.example.composedemo.common.signin.PasswordState
import com.example.composedemo.common.signin.TextFieldState
import com.example.composedemo.util.getHtmlText
import com.example.composedemo.util.toast
import com.example.composedemo.viewmodel.MyViewModel

@ExperimentalFoundationApi
@Composable
fun LoginPage(actions: MainActions, myViewModel: MyViewModel) {

    LoginPages(actions, myViewModel)
}

@Composable
fun LoginPages(actions: MainActions, myViewModel: MyViewModel) {
    Scaffold(
        topBar = {
            PlayAppBar(getHtmlText("登录"), click = {
                actions.upPress()
            })
        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                val focusRequester = remember { FocusRequester() }
                val emailState = remember { EmailState() }
                AccountNumber(emailState, onImeAction = { focusRequester.requestFocus() })
                Spacer(modifier = Modifier.height(16.dp))
                val passwordState = remember { PasswordState() }
                Password(
                    label = stringResource(id = R.string.password),
                    passwordState = passwordState,
                    modifier = Modifier.focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(100.dp))
                Button(
                    onClick = { myViewModel.login(actions,emailState.text, passwordState.text) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(50.dp, 0.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text)),
                    shape = RoundedCornerShape(30.dp),
                    enabled = emailState.isValid && passwordState.isValid
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                TextButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(50.dp, 100.dp, 50.dp, 0.dp),
                    onClick = {
                        toast("忘记密码")
                    }) {
                    Text(text = stringResource(id = R.string.forgot_password))
                }
            }
        }
    )
}

@Composable
fun AccountNumber(
    emailState: TextFieldState = remember { EmailState() },
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = emailState.text,
        onValueChange = {
            emailState.text = it
        },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.body2
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp, 200.dp, 50.dp, 0.dp)
            .onFocusChanged { focusState ->
                val focused = focusState == FocusState.Active
                emailState.onFocusChange(focused)
                if (!focused) {
                    emailState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        isError = emailState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
    emailState.getError()?.let { error -> TextFieldError(textError = error) }
}

@Composable
fun TextFieldError(textError: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp, 5.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}

@Composable
fun Password(
    label: String,
    passwordState: TextFieldState,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val showPassword = remember { mutableStateOf(false) }
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
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_visibility_24),
                        contentDescription = stringResource(id = R.string.hide_password)
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_visibility_off_24),
                        contentDescription = stringResource(id = R.string.show_password)
                    )
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
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