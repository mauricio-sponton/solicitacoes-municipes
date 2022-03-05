$(document).ready(function () {
	moment.locale('pt-BR');	
	
    var table = $('#table-leis').DataTable({
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
              targets: [2,1],
              render: $.fn.dataTable.render.ellipsis( 30 )
            },
            {"width":"2%", "targets": [0]},
            {"width":"10%", "targets": [4,3]}],
        ajax: {
            url: '/leis/datatables/server',
            data: function( d ){
            	d.buscaAssunto = $('#buscaAssunto').val(),     	         	
            	d.buscaData = $('#buscaData').val(),           	
            	d.status = $('#status').val(),
            	d.buscaDataFinal = $('#buscaDataFinal').val()
            	
            }
            
        },      
        columns: [
        	 {orderable: false,
                 "className":'details-control',             
                 "data":null,
                 "defaultContent": '<i class="fas fa-chevron-down" style="color:green; vertical-align: middle;"></i>'
             },
            {data: 'assunto'},
            {orderable: false, data: 'status', render: 
            	function ( data, type, row ){
	            	if(row.status == 'CRIADA'){
	        			return '<strong>Projeto Criado</strong>';
	        		}
            		if(row.status == 'PROPOSITURA'){
            			return '<strong>Propositura</strong>';
            		}
            		if(row.status == 'VETADO_PREFEITO'){
            			return '<strong>Vetado - Prefeitura</strong>';
            		}
            		if(row.status == 'VETADO_VEREADORES'){
            			return '<strong>Vetado - Câmara</strong>';
            		}
            		if(row.status == 'APROVADO_PREFEITO'){
            			return '<strong>Aprovado - Prefeitura</strong>';
            		}
            		if(row.status == 'APROVADO_VEREADORES'){
            			return '<strong>Aprovado - Câmara</strong>';
            		}
            		if(row.status == 'NOVA_VOTACAO'){
            			return '<strong>Votação - Câmara</strong>';
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
	                render: function(id) {
					var excluir = "";
					if(autoridade != "autoridade"){
                		excluir = ''
                	}else{
						excluir = '<a id="btn-del-cliente" class="btn-tabela btn-tabela-excluir" href="/leis/excluir/'+ 
	                		id +'" role="button" title="Excluir" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>'
	
					}
	                    return '<a class="btn-tabela btn-tabela-editar" href="/leis/editar/'+ 
	                    	id +'" role="button" title="Editar"><i class="fas fa-edit"></i></a>' +
	                      excluir +
	                		'<a class="btn-tabela  btn-tabela-view" href="/leis/visualizar/'+ 
	                       	id +'" role="button" title="Visualizar"><i class="fas fa-eye"></i></a>'
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
			} 
		]
    });
    
    table.buttons().disable();
    var alvo = '#table-leis'
	var objeto = "/leis"
    
    $("#table-leis thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});

    fecharRowAoClicarEmBotao(alvo)
	tabelaResponsiva(alvo, table, 3, objeto);
    
    $('#buscaAssunto').on('keyup', function(){
    	table.buttons().disable();
    	table.ajax.reload();
    });

    if($('#buscaData').val("")){
    	$('#buscaDataFinal').prop('disabled', true);
    }
    
    $('#status').on('change', function(){
    	table.ajax.reload();
    	table.buttons().disable();
    })
    
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
    	$('#buscaAssunto, #status, #buscaData, #buscaDataFinal').val("");
    	table.ajax.reload();
    	table.buttons().disable();
    	
    });
});    
function format ( d ) {
	
	var data = moment(d.data).format("DD/MM/YYYY");
	var status = "";
	
	switch(d.status){
	case "PROPOSITURA":
		status = "Propositura";
		break;
	case "VETADO_PREFEITO":
		status = "Vetado - Prefeitura";
		break;
	case "APROVADO_PREFEITO":
		status = "Aprovado - Prefeitura";
		break;
	case "NOVA_VOTACAO":
		status = "Votação - Câmara";
		break;
	case "VETADO_VEREADORES":
		status = "Vetado - Câmara";
		break;
	case "APROVADO_VEREADORES":
		status = "Aprovado - Câmara";
		break;
	case "CRIADA":
		status = "Projeto Criado";
		break;
	}
	
	return '<div class="slider"><table id="inside-reunioes" class="table" style="table-layout: fixed; width: 100%;">'+
	'<tr><td scope="row" class="wrapword"><b>Assunto: </b>' + d.assunto +'</td>' +
	'<td class="wrapword"><b>Data: </b>' + data + '</td></tr>' +
	'<tr><td scope="row" class="wrapword" colspan="2"><b>Situação: </b>' + status +'</td>' +
	'</tr>' +
	'</table></div>';
}