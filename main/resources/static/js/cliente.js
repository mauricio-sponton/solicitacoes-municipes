$(document).ready(function () {
	moment.locale('pt-BR');
    var table = $('#table-clientes').DataTable({
    	"language":{
    		"lengthMenu": "Mostrando _MENU_ registros por páginas",
    		"zeroRecords": "Nenhum registro encontrado",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": " ",
            "search": "Pesquise: ",
            "searchPlaceholder": "Nome ou Email...",
            "paginate": {
                "first":      "Primeiro",
                "last":       "Ultimo",
                "next":       "Próximo",
                "previous":   "Anterior"
            } 
    	},
    	searching: true,
    	order: [[ 0, "asc" ]],
    	lengthMenu: [20],
        serverSide: true,
        responsive: {
            details: false
        },
        ajax: {
//            url: '/clientes/datatables/server',
			url: '/clientes/datatables/server',
            data: function( d ){
            	d.buscaCliente = $('#buscaCliente').val(),            	
            	d.buscaBairro = $('#buscaBairro').val(), 
            	d.buscaEmail = $('#buscaEmail').val(), 
            	d.buscaMes = $('#buscaMes').val(), 
            	d.buscaAno = $('#buscaAno').val(),
            	d.buscaDia = $('#buscaDia').val(),
            	d.apoiador = $('input[name=apoiador]:checked').val()                  
            }
        },
        
        drawCallback: function () {
    		  $('[data-toggle="popover-hover"]').popover({
  			      html: true,
  			      trigger: 'hover',
  			      placement: 'right'   
    		  });
    		  $('.paginate_button:not(.active)', this.api().table().container())          
    	         .on('click', function(){
    	        	 table.buttons().disable();
    	         });   
    	    },
    	   
          columnDefs: [ {
              targets: [0, 1],
              render: $.fn.dataTable.render.ellipsis( 20 )
            }, 
            {"width":"5%", "targets": [4]},
            {"width":"1%", "targets": [0]},
            
            ],
        columns: [
        	 {orderable: false,
                 "className":'details-control',             
                 "data":null,
                 "defaultContent": '<i class="fas fa-chevron-down" style="color:green; vertical-align: middle;"></i>'
             },
            {data: 'nome'},
            {orderable: true, data: 'email'},
            {orderable: false, data: 'apoiador', 
            	render: function(data, type, row){
            		var resultado = "";
            		if(row.apoiador == true){         			
            			resultado =  "Sim"
            		}else{
            			resultado = "Não"
            		}
            		if(row.apoiador_desc != ""  && row.apoiador_desc != null){
            			return '<strong data-toggle="popover-hover" data-content="Descrição do apoio: <b>'+ row.apoiador_desc + '</b>">' + resultado + '</strong>';

            		}else{
            			return resultado;
            		}
            	}},
          
            {orderable: false, 
             data: 'id',
                "render": function(id) {
                	var excluir = "";
                	if(autoridade != "autoridade"){
                		excluir = ''
                	}else{
//                		excluir = '<a id="btn-del-cliente" class="btn-tabela btn-tabela-excluir" href="/clientes/excluir/'+ 
						excluir = '<a id="btn-del-cliente" class="btn-tabela btn-tabela-excluir" href="/clientes/excluir/'+ 
                    	id +'" role="button" title="Excluir" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>'
                	}
//                    return '<a class="btn-tabela btn-tabela-editar" href="/clientes/editar/'+ 
 						return '<a class="btn-tabela btn-tabela-editar" href="/clientes/editar/'+ 
                    	id +'" role="button" title="Editar"><i class="fas fa-edit"></i></a>' +
                    	excluir +
//                    	'<a class="btn-tabela  btn-tabela-view" href="/clientes/visualizar/'+ 
						'<a class="btn-tabela  btn-tabela-view" href="/clientes/visualizar/'+ 
                       	id +'" role="button" title="Visualizar"><i class="fas fa-eye"></i></a>' +
                       	'<a class="btn-tabela  btn-tabela-view-download" target="_blank" href="/relatorios/pdf/codigo/'+ 
                   		id +'?code=municipe_id&tipo=municipe" role="button" title="Download"><i class="fas fa-download"></i></a>';
                }
            }
        ],
        dom: 'Bfrtip',
		buttons: [
			{
				text: '<i class="fas fa-edit"></i>',
				attr: {
					
					id: 'btn-editar-sm',
					type: 'button',
					class: "btn btn-tabela-responsivo btn-tabela-responsivo-editar",
					
				},
				enabled: false
			},
			{
				text: '<i class="fas fa-trash-alt"></i>',
				attr: {
					id: 'btn-excluir-sm',
					type: 'button',
					class: "btn btn-tabela-responsivo btn-tabela-responsivo-excluir",
					"data-toggle":"modal",
					"data-target":"#confirm-modal"
				},
			
				enabled: false
			},
			{
				text: '<i class="fas fa-eye"></i>',
				attr: {
					id: 'btn-visualizar-sm',
					type: 'button',
					class: "btn btn-tabela-responsivo  btn-tabela-responsivo-view"
				},
				enabled: false
			}, 
			{
				text: '<i class="fas fa-download"></i>',
				attr: {
					id: 'btn-download-sm',
					type: 'button',
					class: "btn btn-tabela-responsivo btn-tabela-responsivo-view"
					
				},
				enabled: false
			}, 
		]
    });
    table.buttons().disable();
   
    if(autoridade != "autoridade"){
		$('#btn-excluir-sm').hide()
	}
    
    $("#table-clientes thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});
    
    
    $("#table-clientes tbody").on('click', 'a', function() {
    	
		var tr = $('#table-clientes tbody').closest('tr');
        var row = table.row( tr );
        row.child( format(row.data()), 'no-padding' ).hide();
	});
	
	 $('#table-clientes tbody').on('click', 'tr', function () {
			
		 
	        var tr = $(this).closest('tr');
	        tr.addClass('tr-fundo')
	        var row = table.row( tr );
	 
	        var link = row.data().id;
			$('#btn-editar-sm').attr("href", '/clientes/editar/' + link);
			$('#btn-excluir-sm').attr("href", '/clientes/excluir/' + link);
			$('#btn-visualizar-sm').attr("href", '/clientes/visualizar/' + link);
 			$('#btn-download-sm').attr("href", '/relatorios/pdf/codigo/' + link + "?code=municipe_id&tipo=municipe");
			table.buttons().enable();
	       
			if ( row.child.isShown()) {
	            // This row is already open - close it
	        	 $('div.slider', row.child()).slideUp( function () {
	                 row.child.hide();
	                 tr.removeClass('shown');
	                 
	             } );
	        	 tr.removeClass('tr-fundo');
	        	 if(!tr.hasClass('tr-fundo')){
	        		 table.buttons().disable();
	        	 }
	        	
	 			   
	        	 $('#btn-editar-sm').attr("href", '/clientes/editar/' + link);
	 			$('#btn-excluir-sm').attr("href", '/clientes/excluir/' + link);
	 			$('#btn-visualizar-sm').attr("href", '/clientes/visualizar/' + link);
	 			$('#btn-download-sm').attr("href", '/relatorios/pdf/codigo/' + link + "?code=municipe_id&tipo=municipe");
	        }
	        else {
	        	if ( table.row( '.shown' ).length ) {
	                $('.details-control', table.row( '.shown' ).node()).click();
	        }
	            // Open this row
	        	 if(tr.hasClass('tr-fundo')){
	        		 table.buttons().enable();
	        	 }
	        	
	            row.child( format(row.data()), 'no-padding' ).show();	        
	            tr.addClass('shown');
	            $('div.slider', row.child()).slideDown();
	            $('#inside tbody').on('click', 'tr', function (e){	
	            	e.stopPropagation();	
	            			
	            })   
	            $('html, body').animate({
		              scrollTop: $(".slider").offset().top-200
		          }, 2000);
	            $('#btn-editar-sm').attr("href", '/clientes/editar/' + link);
				$('#btn-excluir-sm').attr("href", '/clientes/excluir/' + link);
				$('#btn-visualizar-sm').attr("href", '/clientes/visualizar/' + link);
				$('#btn-download-sm').attr("href", '/relatorios/pdf/codigo/' + link + "?code=municipe_id&tipo=municipe");
	        }
	    } );
	
	 $('#btn-editar-sm').on('click', function(){
			var link = $(this).attr('href');
			document.location.href = link;
	});
	 $('#btn-visualizar-sm').on('click', function(){
			var link = $(this).attr('href');
			document.location.href = link;
	});
	 $('#btn-download-sm').on('click', function(){
		 	var link = $(this).attr('href');
			window.open(link, '_blank');

			
			//document.location.href = link;
	});
	 $('#btn-excluir-sm').on('click', function(){
			url = $(this).attr('href');
	})
    
    if($('#data-inicial').val("")){
    	$('#data-final').prop('disabled', true);
    }
    
    $('#tipo, #data-inicial, #data-final').on('change', function(){
		$("#busca-estilo").text("");
		$('div #busca-estilo').removeAttr('class');
		$('#data-final').prop('disabled', false);
		var tipo = $("#tipo option:selected").val();
		var dataInicial = $("#data-inicial").val();
		var dataFinal = $("#data-final").val();
		if(tipo !="" || dataInicial != ""){
			$(".generico").hide();
			$("#mensagem").show();
			$("#busca-estilo").show();
			$.get( "/clientes/pesquisa/historico/" + idMunicipe ,
					{tipo : tipo, 
					 dataInicial: dataInicial, 
					 dataFinal: dataFinal
					},
					function( result ) {
				corpo(result);
			})
			 $('html, body').animate({
	              scrollTop: $("#scrollbar").offset().top-200
	          }, 2000);
		}else{
			$(".generico").show();
			$("#busca-estilo").hide();
			$("#mensagem").hide();
			 $('html, body').animate({
	              scrollTop: $("#scrollbar").offset().top-200
	          }, 2000);
		}
	
	})
    $("#reset").on('click', function(){
    	$('#data-inicial').val("");
    	$('#data-final').val("");
    	$('#tipo').val("");
		$(".generico").show();
		$("#busca-estilo").hide();
		$("#mensagem").hide();
		$('#falha').hide();
		$('#data-final').prop('disabled', true);
		 $('html, body').animate({
              scrollTop: $("#scrollbar").offset().top-200
          }, 2000);
	});
    $('#data-inicial, #data-final').on('change', function(){
    	var inicio = moment($('#data-inicial').val());
    	var fim = moment($('#data-final').val());
    	if( fim <= inicio){
    		$('#falha').show();
    		$('#falha span strong').html('A data final não pode ultrapassar a inicial')
      		$('#falha').show()	      		
        }    	
})
$('#buscaCliente, #buscaBairro, #buscaEmail, #buscaDia').on('keyup', function(){
	table.buttons().disable();
	table.ajax.reload();
});
    
    $('#buscaMes, #buscaAno, #buscaDia').change(function (){
    	table.ajax.reload();
    	table.buttons().disable();
     });
    
    $("#buscaDia").on('keyup', function(){
    	
    	if($(this).val() < 1 || $(this).val() > 31){
    		$('[data-toggle="popover"]').popover();    		
    		$(this).val("");
    		  
    	}
    	table.ajax.reload();
    	table.buttons().disable();
    })
  
    $("#buscaAno, #relatorioAno").datepicker({
        format: "yyyy",
        viewMode: "years", 
        minViewMode: "years"
    });

  
    $('input[name=apoiador]').on('change', function(){
    	table.ajax.reload();
    	table.buttons().disable();
    });
    
    $('input[type="search"], #limpar').on('click', function(e){    
    	e.preventDefault()
    	$('input[name=apoiador]').prop('checked', false)    	
    	$('#buscaCliente, #buscaBairro, #buscaEmail, #buscaMes, #buscaAno, #buscaDia').val("");
    	table.ajax.reload();
    	table.buttons().disable();
    	
    });
    
    $("#abrirRelatorioModal, #abrirRelatorioModalExcel").on('click', function(){
		$('#relatorioNome, #relatorioNomeExcel').val($('#buscaCliente').val());
		$('#relatorioEmail, #relatorioEmailExcel').val($('#buscaEmail').val());
		$('#relatorioBairro, #relatorioBairroExcel').val($('#buscaBairro').val());
		$('#relatorioMes, #relatorioMesExcel').val($('#buscaMes').val())
		$('#relatorioAno, #relatorioAnoExcel').val($('#buscaAno').val());
		$('#relatorioDia, #relatorioDiaExcel').val($('#buscaDia').val());
			//var checkedResultado = "";
			
			$.each($('input[name=apoiador]'), function(k, v){
				if($(v).is(':checked')){				
					checkedApoiador = $(v).prop('checked', true).val()
				}
			})
			
			$.each($('input[name=relatorioApoiador]'), function(k, v){
				if($(v).val() == checkedApoiador){
					$(v).prop('checked', true)
				}
			})
	});
    $('#limpar-modal').on('click', function(e){
    	e.preventDefault()
    	$('#relatorioEmail, #relatorioNome, #relatorioMes, #relatorioAno, #relatorioBairro, #relatorioDia').val("");
    	 $('input[name=relatorioApoiador]').prop('checked', false);
    	$('#falha-relatorio').hide()
    });
    $('#limpar-modalExcel').on('click', function(e){
    	e.preventDefault()
    	$('#relatorioEmailExcel, #relatorioNomeExcel, #relatorioMesExcel, #relatorioAnoExcel, #relatorioBairroExcel, #relatorioDiaExcel').val("");
    	 $('input[name=relatorioApoiador]').prop('checked', false);
    	$('#falha-relatorio').hide()
    });
  
    
}); 


function format ( d ) {

	var arrayTels=[]
	$.each(telefones_tabela, function(k,v){
		if(v.cliente.id == d.id && arrayTels.length <=1){
			arrayTels.push("<td><b>Telefone: </b>" + v.numero + " (" + v.marcador + ")" + "</td>");
		}
		
	});
	
	var rg = "";
	var data = moment(d.dataNascimento).format("DD/MM/YYYY");
	var complemento ="";
	var apoio_desc = "";
	var apoio = "";
	
	if(d.endereco.complemento == ""){
		complemento = ""
	}else{
		complemento = " (" + d.endereco.complemento + ")";
	}
	if(d.rg != ""){
		rg = d.rg;
	}else{
		rg = "Não Cadastrado";
	}
	if(d.apoiador == true){
		apoio = "Sim";
	}else{
		apoio ="Não";
	}
	if(d.apoiador_desc != "" && d.apoiador_desc != null){
		apoio_desc= d.apoiador_desc
	}else{
		apoio_desc = "Não cadastrado"
	}
	
//	if(arrayTels.length == 1){
//		$('#tel').addClass("oi")
//	}
	
	return '<div class="slider"><table id="inside-clientes" class="table" style="table-layout: fixed; width: 100%;">'+
	'<tr><td scope="row" class="wrapword"><b>Nome: </b>' + d.nome +'</td>' +
	'<td class="wrapword"><b>Email: </b>' + d.email + '</td></tr>'+
	'<tr><td scope="row" class="wrapword"><b>Data de Nascimento: </b>' + data + '</td>'+
	'<td class="wrapword"><b>RG: </b>' + rg + '</td></tr>'+
	'<tr><td scope="row" class="wrapword"><b>Apoiador? </b>' + apoio + '</td>'+
	'<td class="wrapword"><b>Descrição do apoio: </b>' + apoio_desc + '</td></tr>'+
	'<tr id="tel">'+ arrayTels.join(' ') +'</tr>'+
	'<tr><td class="wrapword" scope="row" colspan="2"><b>Endereço: </b>' + d.endereco.logradouro + ' ' + d.endereco.numero + ', ' + d.endereco.bairro.descricao +
	' - ' + d.endereco.cidade + ' /' + d.endereco.uf + complemento + '</td></tr>'+
	'</table></div>';
}
function corpo(result){
	
	$.each(result, function(k,v){
		
		var replace = v.descricao.toString().replace(/\;/g, '<br>')
		var titulo = "";
		
		$("#mensagem").hide();
		if(v.tipo == 'CLIENTE_NEW' ){
			titulo = 'Munícipe cadastrado no sistema';
		}
		if(v.tipo == 'CLIENTE_EDIT' ){
			titulo = 'Informações do munícipe alteradas';
		}
		if(v.tipo == 'SOLICITACAO_ABERTA' ){
			titulo = 'Nova Solicitação';
		}
		$("#busca-estilo").append('<div class="container-esquerdo esquerdo"><span>'+  moment(v.data).format('DD/MM/YYYY')+'</span><br><span>'+ moment(v.hora, 'HH:mm:ss').format('HH:mm')+
				'</span></div><div class = "container-time right"><div class="content"><div class="titulo-historico"><span>'+titulo+'</span></div>' +
		 '<p><span style="display:block">'+ replace +'</span>' +
		 '</div></div>'
		).hide().fadeIn('slow');
		

	
	})
}