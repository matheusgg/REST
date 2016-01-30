var HOST_ADDRESS = "http://localhost:8080/cap7-rest-project/rest/";

$(document).ready(function() {
	listaClientes();
});

function listaClientes() {
	$.ajax({
		url : HOST_ADDRESS + "clientes"
	}).done(function(data) {
		var clientes = data.clientes.clientes;
		var table = $("#tabelaClientes tbody");
		table.empty();

		for (var i = 0; i < clientes.length; i++) {
			var cliente = clientes[i];
			var id = cliente.id;
			/*
			 * Como o nome da propriedade href é um identificador javascript
			 * invalido (@href), o valor dela é recuperado atraves da chave que
			 * o representa.
			 */
			var visualizar = "<button onclick=\"visualizaCliente('" + cliente.link["@href"] + "')\">Visualizar</button>";
			var remover = "<button onclick=\"removeCliente('" + id + "', this)\">Remover</button>";

			var line = "<tr><td>" + id + "</td><td>" + cliente.nome + "</td><td>" + visualizar + "</td><td>" + remover + "</td></tr>";
			table.append(line);
		}
	});
}

function visualizaCliente(link) {
	$.ajax({
		url : link,
	}).done(function(data) {
		var cliente = data.cliente;
		$("#dtlId").text(cliente.id);
		$("#dtlNome").text(cliente.nome);
		$("#dtlLink").empty().append("<a target='_blank' href='" + cliente.link["@href"] + "'>Link</a>");
		$("#detalhes").removeClass("hide");
	});
}

function removeCliente(id, btn) {
	var option = confirm("Deseja realmente remover o cliente selecionado?");

	if (option) {
		$.ajax({
			url : HOST_ADDRESS + "clientes/" + id,
			type : "DELETE"
		}).done(function(data) {
			alert("Cliente removido com sucesso!");
			$(btn).parent().parent().remove();
		});
	}
}

function cadastraCliente() {
	var clienteJson = $("#formNovoCliente").serializeJSON();

	if (clienteJson.nome != "") {
		var data = "{\"cliente\":" + JSON.stringify(clienteJson) + "}";

		$.ajax({
			url : HOST_ADDRESS + "clientes",
			type : "POST",
			contentType : 'application/json',
			data : data
		}).done(function(data) {
			alert("Cliente cadastrado com sucesso!");
			listaClientes();
			hideForm("fieldNovoCliente");
			$("#formNovoCliente")[0].reset();
		}).fail(function(data) {
			alert("Ocorreu um erro no cadastro do cliente. Por favor, tente novamente: " + data.status + " " + data.statusText);
		});

	} else {
		alert("Por favor, informe o nome do novo cliente.");
	}
}

function showForm(elemento) {
	$("#" + elemento).removeClass("hide");
	$("#" + elemento).addClass("show");
}

function hideForm(elemento) {
	$("#" + elemento).removeClass("show");
	$("#" + elemento).addClass("hide");
}