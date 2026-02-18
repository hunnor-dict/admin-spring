var formEditor;
var transEditor;

function loadLetters(lang) {
	var letters;
	if (lang === "nb") {
		letters = {"a": "A", "b": "B", "c": "C", "d": "D", "e": "E", "f": "F", "g": "G", "h": "H", "i": "I", "j": "J", "k": "K", "l": "L", "m": "M", "n": "N", "o": "O", "p": "P", "q": "Q", "r": "R", "s": "S", "t": "T", "u": "U", "v": "V", "w": "W", "x": "X", "y": "Y", "z": "Z", "ae": "Æ", "oe": "Ø", "aa": "Å"};
	} else {
		letters = {"a": "A-Á", "b": "B", "c": "C", "cs": "CS", "d": "D", "dz": "DZ", "dzs": "DZS", "e": "E-É", "f": "F", "g": "G", "gy": "GY", "h": "H", "i": "I-Í", "j": "J", "k": "K", "l": "L", "ly": "LY", "m": "M", "n": "N", "ny": "NY", "o": "O-Ó", "oe": "Ö-Ő", "p": "P", "q": "Q", "r": "R", "s": "S", "sz": "SZ", "t": "T", "ty": "TY", "u": "U-Ú", "ue": "Ü-Ű", "v": "V", "w": "W", "x": "X", "y": "Y", "z": "Z", "zs": "ZS"};
	}
	$("#letter option").each(function(index, option) {
		$(option).remove();
	});
	$.each(letters, function(key, value) {
		$("#letter").append($("<option>", { value: key }).text(value));
	});
}

function listLetter(lang, letter, term) {
	var listUrl = "/priv/list?lang=" + lang;
	if (term === "") {
		listUrl += "&letter=" + letter;
	} else {
		listUrl += "&term=" + encodeURIComponent(term);
	}
	if ($("#stat").is(":checked")) {
		listUrl += "&stat=1";
	}
	$("#priv-list-results").html("Várj...");
	$.ajax({
		url: listUrl,
		success: function(result) {
			$("#priv-list-results").html(result);
		},
		error: function() {
			$("#priv-list-results").html("Hiba.");
		}
	});
}

function loadEntry(lang, id, term) {
	var entryUrl = "/priv/edit?lang=" + lang + "&id=" + id;
	if (id === "N" && term !== "") {
		entryUrl += "&term=" + encodeURIComponent(term);
	}
	$.ajax({
		url: entryUrl,
		success: function(result) {
			$("#priv-edit-form-container").html(result);
			initForm();
			initTrans();
		},
		error: function() {
			$("#priv-edit").html("Hiba.");
		}
	});
}

function adjustHeight() {
	if (!formEditor) {
		return;
	}
	if ($(window).height() < 850) {
		formEditor.setSize(null, 120);
	} else {
		formEditor.setSize(null, 300);
	}
}

function initForm() {
	if (formEditor) {
		formEditor.toTextArea();
	}
	formEditor = CodeMirror.fromTextArea(document.getElementById("forms"), {
		mode: "application/xml",
		lineNumbers: true,
		lineWrapping: true,
		autoCloseTags: true
	});
	var activeLine = formEditor.getCursor().line;
	formEditor.addLineClass(activeLine, "background", "activeline");
	formEditor.on("cursorActivity", function(cm) {
		cm.removeLineClass(activeLine, "background", "activeline");
		activeLine = cm.getCursor().line;
		cm.addLineClass(activeLine, "background", "activeline");
	});
	adjustHeight();
}

function initTrans() {
	if (transEditor) {
		transEditor.toTextArea();
	}
	transEditor = CodeMirror.fromTextArea(document.getElementById("trans"), {
		mode: "application/xml",
		lineNumbers: true,
		lineWrapping: true,
		autoCloseTags: true
	});
	var activeLine = transEditor.getCursor().line;
	transEditor.addLineClass(activeLine, "background", "activeline");
	transEditor.on("cursorActivity", function(cm) {
		cm.removeLineClass(activeLine, "background", "activeline");
		activeLine = cm.getCursor().line;
		cm.addLineClass(activeLine, "background", "activeline");
	});
}

function insertTag(tag) {
	var insertString = "";
	if (tag === "senseGrp") {
		insertString = "<senseGrp>\n  <sense>\n    <trans></trans>\n  </sense>\n</senseGrp>\n";
	}
	if (tag === "sense") {
		insertString = "  <sense>\n    <trans></trans>\n  </sense>\n";
	}
	if (tag === "trans") {
		insertString = "    <trans></trans>\n";
	}
	if (tag === "lbl") {
		insertString = "    <lbl></lbl>\n";
	}
	if (tag === "eg") {
		insertString = "    <eg>\n      <q></q>\n      <trans></trans>\n    </eg>\n";
	}
	transEditor.replaceRange(insertString, {line: transEditor.getCursor().line, ch: 0});
}

function saveEntry() {
	$("#entry-submit").attr("disabled", true);
	var entryUrl = "/priv/save";
	var entryId = $("#id").val();
	var entryLang = $("#entrylang").val();
	$.post(entryUrl, {
		entrylang: $("#entrylang").val(),
		id: $("#id").val(),
		entry: $("#entry").val(),
		pos: $("#pos").val(),
		forms: formEditor.getValue(),
		trans: transEditor.getValue(),
		status: $("#status").val()
	}, function(data) {
		$("#priv-edit-results").html(data);
		if (entryId === "N") {
			var newId = $("#addition").val();
			if (newId !== "" && newId !== null && newId !== undefined) {
				loadEntry(entryLang, newId, "");
			}
		}
	}).fail(function() {
		$("#priv-edit-results").html("Hiba.");
	}).always(function() {
		$("#entry-submit").removeAttr("disabled");
	});
}

function deleteEntry() {
	if (confirm("Biztos?")) {
		var entryUrl = "/priv/delete";
		$.post(entryUrl, {
			entrylang: $("#entrylang").val(),
			id: $("#id").val(),
			entry: $("#entry").val()
		}, function(data) {
			$("#priv-edit-results").html(data);
			loadEntry($("#entrylang").val(), "N", "");
		}).fail(function() {
			$("#priv-edit-results").html("Hiba.");
		});
	}
}
