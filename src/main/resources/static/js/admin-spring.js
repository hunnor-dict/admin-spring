var langs = {
		"hu": "magyar",
		"nb": "bokmål"
};

var letters = {
		"hu": {
			"a": "A-Á",
			"b": "B",
			"c": "C",
			"cs": "CS",
			"d": "D",
			"dz": "DZ",
			"dzs": "DZS",
			"e": "E-É",
			"f": "F",
			"g": "G",
			"h": "J",
			"i": "I-Í",
			"j": "J",
			"k": "K",
			"l": "L",
			"ly": "LY",
			"m": "M",
			"n": "N",
			"ny": "NY",
			"o": "O-Ó",
			"ö": "Ö-Ő",
			"p": "P",
			"q": "Q",
			"r": "R",
			"s": "S",
			"sz": "SZ",
			"t": "T",
			"ty": "TY",
			"u": "U-Ú",
			"ü": "Ü-Ű",
			"v": "V",
			"w": "W",
			"x": "X",
			"y": "Y",
			"z": "Z",
			"zs": "ZS",
		},
		"nb": {
			"a": "A",
			"b": "B",
			"c": "C",
			"d": "D",
			"e": "E",
			"f": "F",
			"g": "G",
			"h": "H",
			"i": "I",
			"j": "J",
			"k": "K",
			"l": "L",
			"m": "M",
			"n": "N",
			"o": "O",
			"p": "P",
			"q": "Q",
			"r": "R",
			"s": "S",
			"t": "T",
			"u": "U",
			"v": "V",
			"w": "W",
			"x": "X",
			"y": "Y",
			"z": "Z",
			"æ": "Æ",
			"ø": "Ø",
			"å": "Å",
		}
};

function Navigation() {

	this.loadLangs = function() {

		var langSelector = $("#list-lang-selector");
		langSelector.empty();

		$.each(langs, function(value, label) {
			langSelector.append($("<option>").attr("value", value).text(label));
		});

	};

	this.loadLetters = function() {

		var langSelector = $("#list-lang-selector");
		var langSelection = langSelector.val();

		var letterSelector = $("#list-letter-selector");
		letterSelector.empty();

		$.each(letters[langSelection], function(value, label) {
			letterSelector.append($("<option>").attr("value", value).text(label));
		});

	};

	this.bindLangChange = function() {
		var _this = this;
		$("#list-lang-selector").change(function() {
			_this.loadLetters();
		});
	};

	this.bindListByLetter = function() {
		var _this = this;
		$("#list-submit").click(function() {
			_this.loadList();
		});
	};

	this.loadList = function() {

		var _this = this;

		var lang = $("#list-lang-selector").val();
		var letter = $("#list-letter-selector").val();

		var container = $("#list-results");
		container.empty();
		container.append($("<p>").text("Lista betöltése (" + lang + "/" + letter + ")"));

		var url = "/list?lang=" + lang.toUpperCase() + "&letter=" + letter;

		$.get(url, function(data) {
			var container = $("#list-results");
			container.empty();
			var list = $("<ol>");
			data.forEach(function(element, index) {
				_this.loadListItem(list, element, lang);
			});
			container.append($("<p>").append($("<strong>").text(data.length)).append(" szócikk:"));
			container.append(list);
		}).fail(function() {
			var container = $("#list-results");
			container.empty();
			container.append($("<p>").text("Hiba"));
		});

	};

	this.loadListItem = function(list, element, lang) {

		var _this = this;

		var li = $("<li>");

		if (lang == "nb") {
			var bob = $("<a>")
					.text("[B]")
					.attr("target", "_bob")
					.addClass("bob")
					.attr("href", "https://ordbok.uib.no/perl/ordbok.cgi?OPP=" + element.grunnform + "&bokmaal=+&ordbok=begge");
			li.append(bob);
			li.append(" ");
		}

		var entry = $("<a>")
				.text(element.grunnform)
				.addClass("form")
				.addClass("stat" + element.status)
				.attr("href", "#")
				.attr("data-entry-id", element.entryId);
		li.append(entry);
		li.append(" ");

		entry.click(function(event) {
			_this.loadForm(lang, element.entryId);
		});

		if (lang == "nb") {
			var paradigmer = $("<small>")
					.text(element.paradigmer);
			li.append(paradigmer);
		}

		list.append(li);

	};

	this.loadForm = function(lang, id) {

		event.preventDefault();

		var url = "/entry?lang=" + lang.toUpperCase() + "&id=" + id;

		$.get(url, function(entry) {
			editor.loadContent(entry.translation);
			$("#entry-id").val(entry.id);
			$("#entry-status").val(entry.status);
		}).fail(function() {
			console.log("Error loading entry");
		});

	};

}

function Editor() {

	this.bindForm = function() {
		var _this = this;
		$("#entry-save").click(function() {
			_this.saveEntry();
		});
	}

	this.saveEntry = function() {

		var url = "/save";
		var data = {};

		var status = $("#entry-status").val();
		data.status = status;

		$.ajax({
			type: "POST",
			url: url,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			success: function() {
				console.log("Entry saved");
			},
			failure: function() {
				console.log("Error saving entry");
			}
		});

	};

	this.loadEditor = function() {
		var textArea = document.getElementById("entry-translation");
		this.cmEditor = CodeMirror.fromTextArea(textArea, {
			lineNumbers: true,
			mode: "xml",
			autoCloseTags: true
		});
	};

	this.loadContent = function(content) {
		this.cmEditor.getDoc().setValue(content);
	};

}

var navigation = new Navigation();

var editor = new Editor();

$(document).ready(function() {

	navigation.loadLangs();
	navigation.loadLetters();
	navigation.loadList();

	navigation.bindLangChange();
	navigation.bindListByLetter();

	editor.bindForm();

	editor.loadEditor();

});
