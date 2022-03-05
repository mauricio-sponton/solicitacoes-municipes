


$(document).ready(function () {
	var statusUrl = "";
	moment.locale('pt-BR');
	var collapsedGroups = {};
    var table = $('#table-solicitacoes').DataTable({
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
    	lengthMenu: [20],  
        serverSide: true,
        responsive: {
            details: false
        },
        ajax: {
            url: '/solicitacoes/datatables/server',
            data: function( d ){
            	d.buscaAssunto = $('#buscaAssunto').val(),     	
            	d.buscaCliente = $('#buscaCliente').val(),
            	d.buscaUsuario = $('#buscaUsuario').val(),
            	d.buscaBairro = $('#buscaBairro').val(),
            	d.buscaData = $('#buscaData').val(),
            	d.controle = $('input[name=controle]:checked').val(),
            	d.status = $('input[name=status]:checked').val(),
            	d.buscaDataFinal = $('#buscaDataFinal').val(),
            	d.resultado = $('input[name=resultado]:checked').val(),
            	d.aviso = $('input[name=aviso]:checked').val(),
            	d.indicada = $('input[name=indicada]:checked').val()
         
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
			
            targets: [1, 2, 3, 4, 5],
            render: $.fn.dataTable.render.ellipsis( 10 )
			 
          },
          {"width":"5%", "targets": [8, 5]},
          {"width":"1%", "targets": [0]}],
        columns: [
        	 {orderable: false,
                 "className":'details-control',             
                 "data":null,
                 "defaultContent": '<i class="fas fa-chevron-down" style="color:green; vertical-align: middle;"></i>'
             },
             {data: 'id'},
            {data: 'cliente.nome'},
            {data: 'bairro.descricao'},
            {data: 'assunto.descricao'},
            {data: 'indicado', render: function(data){
            	if(data == true){
            		return "Sim";
            	}else{
            		return "Não";
            	}
            }},
            {orderable: false, data: 'status', render: 
            	function ( data, type, row ){
            		var colorido = data.substr(0,1).toUpperCase() + data.substr(1).toLowerCase();
            		if(row.status == 'ABERTO'){
            			return '<strong style="color:green">' + colorido + '</strong>';
            		}
            		if(row.status == 'ATRASADO'){
            			return '<strong style="color:red">' + colorido + '</strong>';
            		}
            		if(row.status == 'PENDENTE' || row.status == 'FINALIZADO'){
            			var color = "";
            			var id = row.id;
            			var aviso = "";
            			var resultado = "";
            			$.each(solucoes, function( index, value ) {       
            				if(id == value.solicitacao.id){
            					resultado = value.resultado;
            					aviso = value.aviso == true ? "Sim" : "Não";            		
							}
						});	
            			color = row.status == "PENDENTE" ? "orange" : "blue";
            			return '<strong data-toggle="popover-hover" data-content="Munícipe avisado: <b>'+ aviso+ '</b><br> Resultado: <b>'+ resultado +'</b>" id="legal" style="color:'+ color +'">' + colorido + '</strong>';

            		}
            		
            	}
            },
            {orderable: false, data: 'data', render:
                function( data ) {
                    return moment(data).format('DD/MM/YYYY');
                }
            },
            
            {orderable: false, 
             data: 'id',
                "render": function(data, type, row) {
                	var code = "";   
                	var editar ="";
                	var excluir =""
                	if(row.status == "PENDENTE" || row.status == "FINALIZADO"){
                		code = "solicitacao_id_solucao";
                	}else{
                		code = "solicitacao_id";
                	}
                	if(autoridade != "autoridade"){
                		excluir = ''
                	}else{
                		excluir = '<a id="btn-del-cliente" class="btn-tabela  btn-tabela-excluir" href="/solicitacoes/excluir/'+ 
                        row.id +'" role="button" title="Excluir" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>'
                	}
                	if(autoridade != "autoridade" && row.status == "FINALIZADO"){
                    	editar = '<a class="btn-tabela btn-tabela-editar isDisabled" href="/solicitacoes/editar/'+ 
                        row.id +'" role="button"><i class="fas fa-edit"></i></a>'
                		//$('.btn-tabela-responsivo-editar').addClass('isDisabled');
                	}else{
                		editar = '<a class="btn-tabela btn-tabela-editar" href="/solicitacoes/editar/'+ 
                        row.id +'" role="button"  title="Editar"><i class="fas fa-edit"></i></a>'
                	}
                	
                    return editar 
                    + excluir
                	+ '<a class="btn-tabela  btn-tabela-view" href="/solicitacoes/visualizar/'+ 
                	row.id +'" role="button" title="Visualizar"><i class="fas fa-eye"></i></a>'
                   		+ '<a class="btn-tabela  btn-tabela-view-download" target="_blank" href="/relatorios/pdf/codigo/'+ 
                   		row.id +'?code='+ code +'&tipo=solicitacao" role="button" title="Download"><i class="fas fa-download"></i></a>';
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
    
    $("#table-solicitacoes thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});
    
    $("#table-solicitacoes tbody").on('click', 'a', function() {
    	
		var tr = $('#table-solicitacoes tbody').closest('tr');
        var row = table.row( tr );
        row.child( formatSolicitacoes(row.data()), 'no-padding' ).hide();
	});
	
	 $('#table-solicitacoes tbody').on('click', 'tr', function () {
	        var tr = $(this).closest('tr');
	        tr.addClass('tr-fundo')
	        var row = table.row( tr );
	       
	        if(autoridade != "autoridade" && row.data().status == "FINALIZADO"){
        		$('#btn-editar-sm').addClass('isDisabled')
        	}
			if(row.data().status != "FINALIZADO"){
				
        		$('#btn-editar-sm').removeClass('isDisabled')
        	}
	        var code = "";           
        	if(row.data().status == "PENDENTE" || row.data().status == "FINALIZADO"){
        		code = "solicitacao_id_solucao";
        	}else{
        		code = "solicitacao_id";
        	}
        	
	        var link = row.data().id;
			$('#btn-editar-sm').attr("href", '/solicitacoes/editar/' + link);
			
			$('#btn-excluir-sm').attr("href", '/solicitacoes/excluir/' + link);
			$('#btn-visualizar-sm').attr("href", '/solicitacoes/visualizar/' + link);
 			$('#btn-download-sm').attr("href", '/relatorios/pdf/codigo/' + link + "?code="+code+"&tipo=solicitacao");
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
	        	
	        	 if(autoridade != "autoridade" && row.data().status == "FINALIZADO"){
	         		$('#btn-editar-sm').addClass('isDisabled')
	         	}
	 			if(row.data().status != "FINALIZADO"){
	 				
	         		$('#btn-editar-sm').removeClass('isDisabled')
	         	}
	        	 $('#btn-editar-sm').attr("href", '/solicitacoes/editar/' + link);
	        	
	 			$('#btn-excluir-sm').attr("href", '/solicitacoes/excluir/' + link);
	 			$('#btn-visualizar-sm').attr("href", '/solicitacoes/visualizar/' + link);
	  			$('#btn-download-sm').attr("href", '/relatorios/pdf/codigo/' + link + "?code="+code+"&tipo=solicitacao");
	        }
	        else {
	        	if ( table.row( '.shown' ).length ) {
	                $('.details-control', table.row( '.shown' ).node()).click();
	        }
	            // Open this row
	        	 if(tr.hasClass('tr-fundo')){
	        		 table.buttons().enable();
	        	 }
	        	
	            row.child( formatSolicitacoes(row.data()), 'no-padding' ).show();	        
	            tr.addClass('shown');
	            $('div.slider', row.child()).slideDown();
	            $('#inside tbody').on('click', 'tr', function (e){	
	            	e.stopPropagation();	
	            			
	            })   
	            $('html, body').animate({
		              scrollTop: $(".slider").offset().top-200
		          }, 2000);
	            if(autoridade != "autoridade" && row.data().status == "FINALIZADO"){
	        		$('#btn-editar-sm').addClass('isDisabled')
	        	}
				if(row.data().status != "FINALIZADO"){
					
	        		$('#btn-editar-sm').removeClass('isDisabled')
	        	}
	            $('#btn-editar-sm').attr("href", '/solicitacoes/editar/' + link);
	          
				$('#btn-excluir-sm').attr("href", '/solicitacoes/excluir/' + link);
				$('#btn-visualizar-sm').attr("href", '/solicitacoes/visualizar/' + link);
	 			$('#btn-download-sm').attr("href", '/relatorios/pdf/codigo/' + link + "?code="+code+"&tipo=solicitacao");
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
	});
	 $('#btn-excluir-sm').on('click', function(){
			url = $(this).attr('href');
	})
    
//    $('#table-solicitacoes').on('click', 'tr.dtrg-group', function () {
//        var name = $(this).data('name');
//        collapsedGroups[name] = !collapsedGroups[name];     
//        table.draw(false);
//    }); 
    $('#buscaAssunto, #buscaCliente, #buscaUsuario, #buscaBairro').on('keyup', function(){
    	table.buttons().disable();
    	table.ajax.reload();
    });

    if($('#buscaData').val("")){
    	$('#buscaDataFinal').prop('disabled', true);
    }
    
    $('input[name=status], input[name=resultado], input[name=aviso], input[name=indicada]').on('change', function(){
    	table.ajax.reload();
    	table.buttons().disable();
    })
    if(statusUrl != null){
	if(statusUrl == "atrasadas"){
    	$('#atrasado').prop('checked', true);
    	table.ajax.reload();
    }
    if(statusUrl == "pendentes"){
    	$('#pendente').prop('checked', true);
    	table.ajax.reload();
    }
}
    
    
    $('#buscaData, #buscaDataFinal').on('change', function(){
    	$('#buscaDataFinal').prop('disabled', false)
    	table.ajax.reload();
    	table.buttons().disable();
    });
    $('.fechar').on('click', function(){
		$('#falha').hide()
	})
	  $('#buscaDataFinal, #buscaData').on('change', function(){
	    	var inicio = moment($('#buscaData').val());
	    	var fim = moment($('#buscaDataFinal').val());
	    	if( fim <= inicio){
	    		$('#falha span strong').html('A data final não pode ultrapassar a inicial')
	      		$('#falha').show()
	      		
	        }
	    	
	    })
	
    $('input[type="search"], #limpar').on('click', function(){
    	$('#buscaDataFinal').prop('disabled', true)
    	$('input[name=controle]').prop('checked', false)
    	$('input[name=status]').prop('checked', false)
    	$('input[name=resultado]').prop('checked', false)
    	$('input[name=aviso]').prop('checked', false)
    	$('input[name=indicada]').prop('checked', false)
    	$('#buscaAssunto, #buscaCliente, #buscaUsuario, #buscaData, #buscaDataFinal, #buscaBairro').val("");
    	table.ajax.reload();
    	table.buttons().disable();
    	
    });
    
    $("#abrirRelatorioModal").on('click', function(){
    	$('#modal-titulo-pdf').html('Gerar PDF')
    })
     $("#abrirRelatorioModalExcel").on('click', function(){
    	 $('#modal-titulo-excel').html('Gerar Excel')
    })
	$("#abrirRelatorioModal, #abrirRelatorioModalExcel").on('click', function(){
		$('#relatorioCliente, #relatorioClienteExcel').val($('#buscaCliente').val());
		$('#relatorioAssunto, #relatorioAssuntoExcel').val($('#buscaAssunto').val());
		$('#relatorioBairro, #relatorioBairroExcel').val($('#buscaBairro').val());
		$('#relatorioUsuario, #relatorioUsuarioExcel').val($('#buscaUsuario').val());
		$('#relatorioDataInicial, #relatorioDataInicialExcel').val($('#buscaData').val())
		$('#relatorioDataFinal, #relatorioDataFinalExcel').val($('#buscaDataFinal').val());
			var checked = ""
			var checkedAviso = "";
			var checkedResultado = "";
			var checkedIndicada = "";
			$.each($('input[name=status]'), function(k, v){
				if($(v).is(':checked')){				
					checked = $(v).prop('checked', true).val().toUpperCase()
				}
			})
			$.each($('input[name=resultado]'), function(k, v){
				if($(v).is(':checked')){				
					checkedResultado = $(v).prop('checked', true).val()
				}
			})
			$.each($('input[name=aviso]'), function(k, v){
				if($(v).is(':checked')){				
					checkedAviso = $(v).prop('checked', true).val()
				}
			})
			$.each($('input[name=indicada]'), function(k, v){
				if($(v).is(':checked')){				
					checkedIndicada = $(v).prop('checked', true).val()
				}
			})
			$.each($('input[name=relatorioStatus]'), function(k, v){
				if($(v).val() == checked){
					$(v).prop('checked', true)
				}
			})
			
			$.each($('input[name=relatorioResultado]'), function(k, v){
				if($(v).val() == checkedResultado){
					$(v).prop('checked', true)
				}
			})
			$.each($('input[name=relatorioAviso]'), function(k, v){
				if($(v).val() == checkedAviso){
					$(v).prop('checked', true)
				}
			})
			$.each($('input[name=relatorioIndicada]'), function(k, v){
				if($(v).val() == checkedIndicada){
					$(v).prop('checked', true)
				}
			})
	});
    $('#limpar-modal').on('click', function(e){
    	e.preventDefault()
    	$('#relatorioAssunto, #relatorioCliente, #relatorioUsuario, #relatorioDataInicial, #relatorioDataFinal, #relatorioBairro').val("");
    	 $('input[name=relatorioStatus], input[name=relatorioResultado], input[name=relatorioAviso], input[name=relatorioIndicada]').prop('checked', false);
    	 $('input[value=solicitacoes-nogroup]').prop('checked', true);
    	 $('#carregaGroup').val("solicitacoes-nogroup")
    	$('#falha-relatorio').hide()
    });
    $('#limpar-modalExcel').on('click', function(e){
    	e.preventDefault()
    	$('#relatorioAssuntoExcel, #relatorioClienteExcel, #relatorioUsuarioExcel, #relatorioDataInicialExcel, #relatorioDataFinalExcel, #relatorioBairroExcel').val("");
    	 $('input[name=relatorioStatus], input[name=relatorioResultado], input[name=relatorioAviso], input[name=relatorioIndicada]').prop('checked', false);
    	$('#falha-relatorioExcel').hide()
    });
    $('#relatorioDataFinal, #relatorioDataInicial').on('change', function(){
    	var inicio = moment($('#relatorioDataInicial').val());
    	var fim = moment($('#relatorioDataFinal').val());
    	if( fim <= inicio){
    		$('#falha-relatorio span strong').html('A data final não pode ultrapassar a inicial')
      		$('#falha-relatorio').show()
      		
        }   	
    });
    $("#modal-relatorio-excel").on("hidden.bs.modal", function() {
    	$('#falha-relatorioExcel').hide();
    	$('#relatorioAssuntoExcel, #relatorioClienteExcel, #relatorioUsuarioExcel, #relatorioDataInicialExcel, #relatorioDataFinalExcel, #relatorioBairroExcel').val("");
   	 $('input[name=relatorioStatus], input[name=relatorioResultado], input[name=relatorioAviso], input[name=relatorioIndicada]').prop('checked', false);
	});
    $("#modal-relatorio-dinamico").on("hidden.bs.modal", function() {
    	$('#relatorioAssunto, #relatorioCliente, #relatorioUsuario, #relatorioDataInicial, #relatorioDataFinal, #relatorioBairro').val("");
   	 $('input[name=relatorioStatus], input[name=relatorioResultado], input[name=relatorioAviso], input[name=relatorioIndicada]').prop('checked', false);
   	 $('input[value=solicitacoes-nogroup]').prop('checked', true);
   	 $('#carregaGroup').val("solicitacoes-nogroup")
   	$('#falha-relatorio').hide()
	})
     $('#relatorioDataFinalExcel, #relatorioDataInicialExcel').on('change', function(){
    	var inicio = moment($('#relatorioDataInicialExcel').val());
    	var fim = moment($('#relatorioDataFinalExcel').val());
    	if( fim <= inicio){
    		$('#falha-relatorioExcel span strong').html('A data final não pode ultrapassar a inicial')
      		$('#falha-relatorioExcel').show()
      		
        }   	
    })
    $('input[name=group]').on('change', function(){
    	if($(this).val() == "solicitacoes-status"){
    		 $('input[name=relatorioStatus]').prop('checked', false);
    		  $('#controleStatus').slideUp();
    		 
    	}else{
    		 $('#controleStatus').slideDown();
    	}
    	$('#carregaGroup').attr("value", $(this).val());
    });
    
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
			$.get( "/solicitacoes/pesquisa/historico/" + solicitacao ,
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
    	$('#data-final').prop('disabled', true);
    	$('#data-final').val("");
    	$('#tipo').val("");
		$(".generico").show();
		$("#busca-estilo").hide();
		$("#mensagem").hide();
		$('#falha').hide();
		 $('html, body').animate({
              scrollTop: $("#scrollbar").offset().top-200
          }, 2000);
	})
	$('#data-inicial, #data-final').on('change', function(){
	    	var inicio = moment($('#data-inicial').val());
	    	var fim = moment($('#data-final').val());
	    	if( fim <= inicio){
	    		$('#falha').show();
	    		$('#falha span strong').html('A data final não pode ultrapassar a inicial')
	      		$('#falha').show()	      		
	        }    	
	})
  
});  
 
function corpo(result){
	
	$.each(result, function(k,v){
		
		var replace = v.descricao.toString().replace(/\;/g, '<br>')
		var titulo = "";
		
		$("#mensagem").hide();
		if(v.tipo == 'SOLUCAO_EDIT' ){
			titulo = 'Solução Editada';
		}
		if(v.tipo == 'SOLICITACAO_EDIT' ){
			titulo = 'Solicitação Editada';
		}
		if(v.tipo == 'SOLICITACAO_ABERTA' ){
			titulo = 'Solicitação Aberta';
		}
		if(v.tipo == 'SOLICITACAO_PENDENTE' ){
			titulo = 'Solicitação Pendente';
		}
		if(v.tipo == 'SOLICITACAO_ATRASADA' ){
			titulo = 'Solicitação Atrasada';
		}
		$("#busca-estilo").append('<div class="container-esquerdo esquerdo"><span>'+  moment(v.data).format('DD/MM/YYYY')+'</span><br><span>'+ moment(v.hora, 'HH:mm:ss').format('HH:mm')+
				'</span></div><div class = "container-time right"><div class="content"><div class="titulo-historico"><span>'+titulo+'</span></div>' +
		 '<p><span style="display:block">'+ replace +'</span>' +
		 '</div></div>'
		).hide().fadeIn('slow');
		

	
	})
}
function formatSolicitacoes ( d ) {

	
	
	var data = moment(d.data).format("DD/MM/YYYY");
	var status = d.status.substring(0,1).toUpperCase() + d.status.substring(1).toLowerCase();
	var indicada = "";
	
	if(d.indicado == true){
		indicada = "Sim"
	}else{
		indicada = "Não"
	}
	
	if(d.status == "ABERTO" || d.status == "ATRASADO"){
		return '<div class="slider"><table id="inside" class="table" style="table-layout: fixed; width: 100%">'+
		'<tr><td scope="row" class="wrapword"><b>Munícipe: </b>' + d.cliente.nome +'</td>' +
		'<td class="wrapword"><b>Status: </b>' + status + '</td></tr>'+
		'<tr><td scope="row" class="wrapword"><b>Bairro: </b>' + d.bairro.descricao + '</td>'+
		'<td class="wrapword"><b>Assunto: </b>' + d.assunto.descricao + '</td></tr>'+
		'<tr><td scope="row" class="wrapword"><b>Usuário: </b>' + d.usuario + '</td>'+
		'<td class="wrapword"><b>Data: </b>' + data + '</td></tr>'+
		
		'<tr><td scope="row" class="wrapword" colspan="2"><b>Indicada?: </b>' + indicada + '</td></tr>'+
		
		'</table></div>';
	}else{
		var aviso = "";
		var resultado = "";
		$.each(solucoes, function( index, value ) {       
			if(d.id == value.solicitacao.id){
				resultado = value.resultado;
				aviso = value.aviso == true ? "Sim" : "Não";            		
			}
		});	
		return '<div class="slider"><table id="inside" class="table" style="table-layout: fixed; width: 100%">'+
		'<tr><td scope="row" class="wrapword"><b>Munícipe: </b>' + d.cliente.nome +'</td>' +
		'<td class="wrapword"><b>Status: </b>' + status + '</td></tr>'+
		'<tr><td scope="row" class="wrapword"><b>Bairro: </b>' + d.bairro.descricao + '</td>'+
		'<td class="wrapword"><b>Assunto: </b>' + d.assunto.descricao + '</td></tr>'+
		'<tr><td scope="row" class="wrapword"><b>Usuário: </b>' + d.usuario + '</td>'+
		'<td class="wrapword"><b>Data: </b>' + data + '</td></tr>'+
		'<tr><td scope="row" class="wrapword"><b>Munícipe Avisado?: </b>' + aviso + '</td>'+
		'<td class="wrapword"><b>Resultado: </b>' + resultado + '</td></tr>'+
		'<tr><td scope="row" class="wrapword" colspan="2"><b>Indicada?: </b>' + indicada + '</td></tr>'+
		
		'</table></div>';
		
	}
	
}
