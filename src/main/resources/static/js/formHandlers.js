// 二重押下防止
function handleOnSubmit(form) {
    const button = form.querySelector('button[type="submit"]');
    button.disabled = true;
    return true;
}

// confirm
function handleConfirm(form, label) {
	if (confirm(label)) {
		return handleOnSubmit(form);
	}
	return false;
}
